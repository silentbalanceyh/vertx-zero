package cn.vertxup.integration.service;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.refine.Is;
import io.vertx.tp.is.uca.command.Fs;
import io.vertx.tp.is.uca.updater.StoreMigration;
import io.vertx.tp.is.uca.updater.StoreRename;
import io.vertx.tp.is.uca.updater.StoreUp;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DirService implements DirStub {
    private static final ConcurrentMap<String, StoreUp> POOL_UP = new ConcurrentHashMap<>();

    @Override
    public Future<JsonObject> create(final JsonObject directoryJ) {
        return Is.fsRun(directoryJ, fs -> {
            // JsonArray serialization
            final JsonObject inputJ = Is.dataIn(directoryJ);
            // IDirectory Building
            final IDirectory directory = fs.initTree(inputJ);
            // Insert into database
            return Ux.Jooq.on(IDirectoryDao.class).insertJAsync(directory)
                // JsonArray Deserialization
                .compose(Is::dataOut)
                // Actual Directory Processing
                .compose(fs::mkdir);
        });
    }

    /*
     * 1. Delete current folder and all children:  Start with storePath
     * 2. Remove all folders of current folder include all files/directories under this folder
     *
     * Old Comment:
     *
     * actual = true
     * 1. Delete folder records.
     * 1. Remove the folder actually
     */
    @Override
    public Future<Boolean> remove(final String key) {
        final UxJooq jq = Ux.Jooq.on(IDirectoryDao.class);
        return jq.<IDirectory>fetchByIdAsync(key)
            .compose(directory -> Is.directoryQr(directory).compose(queried -> {
                final List<IDirectory> directories = new ArrayList<>();
                // Current Folder
                if (Objects.nonNull(directory)) {
                    directories.add(directory);
                }
                // All Children Folders
                directories.addAll(queried);
                return Ux.future(directories);
            }))
            // Delete Records
            .compose(Ux.Jooq.on(IDirectoryDao.class)::deleteJAsync)
            // Helper execute `rm` command to remove folders
            .compose(removed -> Is.fsRun(removed, Fs::rm))
            .compose(nil -> Ux.futureT());
    }

    /*
     * actual = false
     * 1. Move `file` to `.Trash` folder instead of other operations.
     * 2. Mark each folder with prefix `DELETE`
     * 3. Update folder records `deleted = true`
     */
    @Override
    public Future<Boolean> remove(final String key, final String userId) {
        final UxJooq jq = Ux.Jooq.on(IDirectoryDao.class);
        return jq.<IDirectory>fetchByIdAsync(key).compose(directory -> Is.directoryQr(directory).compose(queried -> {
            final List<IDirectory> directories = new ArrayList<>();
            // Current Folder
            if (Objects.nonNull(directory)) {
                directories.add(directory);
            }
            directories.addAll(queried);
            directories.forEach(item -> {
                item.setActive(Boolean.FALSE);
                item.setUpdatedBy(userId);
                item.setUpdatedAt(LocalDateTime.now());
            });
            return jq.updateAsync(directories).compose(Ux::futureJ);
        })).compose(directoryJ -> Is.fsRun(directoryJ, fs -> {
            /*
             * 1. .Trash folder ensure
             * 2. Call `rename` command in `Fs` interface
             */
            fs.initTrash();

            final Kv<String, String> kv = Is.trashIn(directoryJ);
            return fs.rename(kv).compose(renamed -> Ux.future(directoryJ));
        })).compose(removed -> Ux.futureT());
    }

    @Override
    public Future<JsonObject> updateBranch(final IDirectory directory) {
        if (Objects.isNull(directory)) {
            return Ux.futureJ();
        }
        final String parent = directory.getParentId();
        if (Ut.isNil(parent)) {
            return Ux.futureJ(directory);
        } else {
            // Update In Cycle for branch updating
            return Is.directoryBranch(parent, directory)
                .compose(nil -> Ux.futureJ(directory));
        }
    }

    @Override
    public Future<JsonObject> update(final String key, final JsonObject directoryJ) {
        /*
         * Here are updating action:
         * 1. integrationId changing
         * 2. storePath changing
         */
        final UxJooq jq = Ux.Jooq.on(IDirectoryDao.class);
        return jq.<IDirectory>fetchByIdAsync(key).compose(directory -> {
            // Query Null directory
            if (Objects.isNull(directory)) {
                return Ux.future();
            }
            // IntegrationId/RunComponent
            final String integrationId = directoryJ.getString(KName.INTEGRATION_ID);
            if (Ut.isDiff(integrationId, directory.getIntegrationId())) {
                /*
                 * Integration Modification, it means that you must migrate from
                 * one storage to another one storage.
                 */
                final StoreUp store = Fn.poolThread(POOL_UP, StoreMigration::new, StoreMigration.class.getName());
                Is.Log.infoWeb(this.getClass(), "Integration Changing: {0}", store.getClass());
                return store.migrate(directory, directoryJ);
            } else {
                // StorePath
                final String storePath = directoryJ.getString(KName.STORE_PATH);
                if (Ut.isDiff(storePath, directory.getStorePath())) {
                    /*
                     * Integration Not Changing, because of `storePath` changed, here are
                     * `rename` only
                     */
                    final StoreUp store = Fn.poolThread(POOL_UP, StoreRename::new, StoreRename.class.getName());
                    Is.Log.infoWeb(this.getClass(), "StorePath Changing: {0}", store.getClass());
                    return store.migrate(directory, directoryJ);
                }
            }
            return Ux.future(directory);
        }).compose(directory -> {
            if (Objects.isNull(directory)) {
                return Ux.futureJ();
            } else {
                final JsonObject data = Is.dataIn(directoryJ);
                final IDirectory updated = Ux.updateT(directory, data);
                return jq.updateAsync(key, updated)
                    // Update branch
                    .compose(this::updateBranch)
                    .compose(Is::dataOut);
            }
        });
    }
}
