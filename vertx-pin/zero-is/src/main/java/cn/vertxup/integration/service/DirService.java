package cn.vertxup.integration.service;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.uca.Fs;
import io.vertx.tp.is.uca.FsHelper;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DirService implements DirStub {
    @Override
    public Future<JsonObject> create(final JsonObject directoryJ) {
        return FsHelper.componentRun(directoryJ, fs -> {
            // JsonArray serialization
            final JsonObject inputJ = this.inJ(directoryJ);
            // IDirectory Building
            final IDirectory directory = fs.initialize(inputJ);
            // Insert into database
            return Ux.Jooq.on(IDirectoryDao.class).insertJAsync(directory)
                // JsonArray Deserialization
                .compose(this::outJ)
                // Actual Directory Processing
                .compose(fs::mkdir);
        });
    }

    /*
     * actual = true
     * 1. Delete folder records.
     * 1. Remove the folder actually
     *
     * actual = false
     * 1. Move `file` to `.Trash` folder instead of other operations.
     * 2. Mark each folder with prefix `DELETE`
     * 3. Update folder records `deleted = true`
     */
    @Override
    public Future<Boolean> remove(final String key, final boolean actual) {
        if (actual) {
            // Hard Removing
            return this.removePermanent(key);
        } else {
            // Soft Removing
            return null;
        }
    }

    private Future<Boolean> removePermanent(final String key) {
        final UxJooq jq = Ux.Jooq.on(IDirectoryDao.class);
        return jq.<IDirectory>fetchByIdAsync(key)
            .compose(directory -> this.fetchTree(directory).compose(queried -> {
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
            .compose(removed -> FsHelper.componentRun(removed, Fs::rm))
            .compose(nil -> Ux.futureT());
    }

    @Override
    public Future<JsonObject> update(final String key, final JsonObject directoryJ) {
        return null;
    }

    private Future<List<IDirectory>> fetchTree(final IDirectory directory) {
        if (Objects.isNull(directory)) {
            return Ux.futureL();
        } else {
            final JsonObject condition = Ux.whereAnd();
            condition.put(KName.SIGMA, directory.getSigma());
            condition.put(KName.STORE_PATH + ",s", directory.getStorePath());
            return Ux.Jooq.on(IDirectoryDao.class).fetchAsync(condition);
        }
    }

    private JsonObject inJ(final JsonObject directoryJ) {
        // Cannot deserialize value of type `java.lang.String` from Array value (token `JsonToken.START_ARRAY`)
        Ut.ifString(directoryJ,
            KName.METADATA,
            KName.VISIT_GROUP,
            KName.VISIT_ROLE,
            KName.VISIT_MODE
        );
        return directoryJ;
    }

    private Future<JsonObject> outJ(final JsonObject response) {
        return Ut.ifJObject(
            KName.METADATA,
            KName.VISIT_GROUP,
            KName.VISIT_ROLE,
            KName.VISIT_MODE
        ).apply(response);
    }
}
