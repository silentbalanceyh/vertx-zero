package io.horizon.spi.business;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.horizon.atom.common.Kv;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.is.refine.Is;
import io.vertx.mod.is.uca.command.Fs;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExPath implements ExIo {

    @Override
    public Future<JsonArray> docInitialize(final JsonArray data, final JsonObject config) {
        /*
         * 1. Fetch all data of IDirectory
         * -- The condition is `storePath` instead of other information
         * 2. Build the map of `storePath = IDirectory`, here will put `directoryId` into each data
         */
        return Is.fsDocument(data, config).compose(Is::dataOut);
    }


    // ----------------- Directory Interface ----------------------
    /*
     * Fetch running directory records
     *  1. active = true
     *  2. sigma is matching
     * When parentId will trigger following two situations:
     * -- parentId = null ( Fetch All )
     * -- parentId is not null ( Fetch Children )
     *
     * WHERE SIGMA = ? AND ACTIVE = true
     * WHERE SIGMA = ? AND ACTIVE = true AND PARENT_ID = ?
     */
    @Override
    public Future<JsonArray> dirRun(final String sigma, final String parentId) {
        Objects.requireNonNull(sigma);
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.SIGMA, sigma);
        condition.put(KName.ACTIVE, Boolean.TRUE);
        if (Ut.isNotNil(parentId)) {
            condition.put(KName.PARENT_ID, parentId);
        }
        return Is.directoryQr(condition).compose(Ux::futureA).compose(Is::dataOut);
    }

    @Override
    public Future<JsonObject> dirBy(final String sigma, final String directory) {
        Objects.requireNonNull(sigma);
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.SIGMA, sigma);
        condition.put(KName.CODE, directory);
        return Ux.Jooq.on(IDirectoryDao.class).fetchJOneAsync(condition)
            .compose(Is::dataOut);
    }

    @Override
    public Future<ConcurrentMap<String, JsonObject>> dirBy(final Set<String> keys) {
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        return Ux.Jooq.on(IDirectoryDao.class).fetchJAsync(condition)
            .compose(Is::dataOut)
            .compose(directories -> {
                final ConcurrentMap<String, JsonObject> map
                    = Ut.elementMap(directories, KName.KEY);
                return Ux.future(map);
            });
    }

    /*
     * Fetch all inactive directory records
     *  1. active = false
     *  2. sigma is matching
     *
     * WHERE SIGMA = ? AND ACTIVE = false
     */
    @Override
    public Future<JsonArray> dirTrash(final String sigma) {
        Objects.requireNonNull(sigma);
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.SIGMA, sigma);
        condition.put(KName.ACTIVE, Boolean.FALSE);
        return Is.directoryQr(condition).compose(Ux::futureA).compose(Is::dataOut);
    }
    // ----------------- File Interface ----------------------


    @Override
    public Future<Boolean> fsUpload(final String directoryId, final ConcurrentMap<String, String> fileMap) {
        return Is.fsComponent(directoryId).compose(fs -> fs.upload(fileMap));
    }

    @Override
    public Future<Boolean> fsRemove(final String directoryId, final ConcurrentMap<String, String> fileMap) {
        return Is.fsComponent(directoryId).compose(fs -> fs.rm(fileMap.values()));
    }

    @Override
    public Future<Buffer> fsDownload(final String directoryId, final ConcurrentMap<String, String> fileMap) {
        return Is.fsComponent(directoryId).compose(fs -> fs.download(new HashSet<>(fileMap.values())));
    }

    @Override
    public Future<Buffer> fsDownload(final String directoryId, final String storePath) {
        return Is.fsComponent(directoryId).compose(fs -> fs.download(storePath));
    }

    // ----------------- Mix Interface ----------------------

    @Override
    public Future<JsonObject> update(final String directoryId, final String user) {
        return Is.directoryBranch(directoryId, user).compose(Ux::futureJ);
    }

    /*
     * Update all the X_DIRECTORY information here.
     *  1. active = false
     *  2. updatedBy = user
     */
    @Override
    public Future<JsonArray> trashIn(final JsonArray directoryJ, final ConcurrentMap<String, String> fileMap) {
        // active = false
        return this.directoryU(directoryJ, false)
            .compose(directoryMap -> Is.fsGroup(fileMap).compose(attachmentMap -> this.trashDo(
                directoryMap, attachmentMap, false
            )))
            .compose(nil -> Ux.future(directoryJ));
    }

    @Override
    public Future<JsonArray> trashOut(final JsonArray directoryJ, final ConcurrentMap<String, String> fileMap) {
        // active = true
        return this.directoryU(directoryJ, true)
            .compose(directoryMap -> Is.fsGroup(fileMap).compose(attachmentMap -> this.trashDo(
                directoryMap, attachmentMap, true
            )))
            .compose(nil -> Ux.future(directoryJ));
    }

    @Override
    public Future<JsonArray> purge(final JsonArray directoryJ, final ConcurrentMap<String, String> fileMap) {
        return this.directoryD(directoryJ)
            .compose(directoryMap -> Is.fsGroup(fileMap).compose(attachmentMap -> {
                final ConcurrentMap<Fs, Set<String>> combine = Is.fsCombine(directoryMap, attachmentMap);
                final List<Future<Boolean>> futures = new ArrayList<>();
                combine.forEach((fs, storeSet) -> {
                    final ConcurrentMap<String, String> renameMap = Is.trashIn(storeSet);
                    futures.add(fs.rm(renameMap.values()));
                });
                return Fn.combineT(futures);
            }))
            .compose(nil -> Ux.future(directoryJ));
    }

    @Override
    public Future<Boolean> rename(final JsonObject directoryJ, final Kv<String, String> renameKv) {
        final String directoryId = directoryJ.getString(KName.KEY);
        final UxJooq jq = Ux.Jooq.on(IDirectoryDao.class);
        final String updatedBy = directoryJ.getString(KName.UPDATED_BY);
        return jq.<IDirectory>fetchByIdAsync(directoryId)
            .compose(directory -> {
                directory.setUpdatedAt(LocalDateTime.now());
                directory.setUpdatedBy(updatedBy);
                return jq.updateAsync(directory)
                    .compose(updated -> Is.directoryBranch(directoryId, updatedBy));
            })
            .compose(directory -> Is.fsComponent(directory.getKey()))
            .compose(fs -> fs.rename(renameKv));
    }

    @Override
    public Future<JsonArray> dirTree(final String sigma, final List<String> paths) {
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.STORE_PATH + ",i", Ut.toJArray(paths));
        condition.put(KName.SIGMA, sigma);
        final UxJooq jq = Ux.Jooq.on(IDirectoryDao.class);
        return jq.fetchJAsync(condition);
    }

    @Override
    public Future<JsonObject> verifyIn(final JsonArray directoryA, final JsonObject params) {
        Ut.valueToString(directoryA, KName.VISIT_MODE);
        return Is.directoryLeaf(directoryA, params).compose(Ux::futureJ);
    }

    // ----------------- Private Interface ----------------------
    private Future<Boolean> trashDo(final ConcurrentMap<Fs, Set<String>> directoryMap,
                                    final ConcurrentMap<Fs, Set<String>> fileMap, final boolean active) {
        /*
         * Combine two map
         */
        final ConcurrentMap<Fs, Set<String>> combine = Is.fsCombine(directoryMap, fileMap);

        /*
         * Trash Split
         */
        final List<Future<Boolean>> futures = new ArrayList<>();
        combine.forEach((fs, storeSet) -> {
            final ConcurrentMap<String, String> renameMap;
            if (active) {
                // trashOut
                renameMap = Is.trashOut(storeSet);
            } else {
                /*
                 *  .Trash initialize to create new folder under store root path
                 * This code is critical for successfully creating
                 */
                fs.initTrash();
                // trashIn
                renameMap = Is.trashIn(storeSet);
            }
            futures.add(fs.rename(renameMap));
        });
        return Fn.combineT(futures).compose(nil -> Ux.futureT());
    }

    private Future<ConcurrentMap<Fs, Set<String>>> directoryD(final JsonArray directoryD) {
        /*
         * Complex Condition
         * 1. Directory
         * 2. Sub Directory ( Start with StorePath )
         */
        final JsonObject criteria = Ux.whereOr();
        // Directory
        criteria.put(KName.KEY + ",i", Ut.toJArray(Ut.valueSetString(directoryD, KName.KEY)));
        final JsonObject children = Ux.whereOr();
        Ut.itJArray(directoryD).forEach(json -> {
            final String storePath = json.getString(KName.STORE_PATH);
            if (Ut.isNotNil(storePath)) {
                final JsonObject child = Ux.whereAnd();
                child.put(KName.STORE_PATH + ",s", json.getString(KName.STORE_PATH));
                child.put(KName.ACTIVE, Boolean.FALSE);
                children.put("$" + json.getString(KName.CODE) + "$", child);
            }
        });
        criteria.put("$CH$", children);

        /*
         * Remove
         */
        return Ux.Jooq.on(IDirectoryDao.class).deleteByAsync(criteria).compose(removed -> {
            // Grouped directory by runComponent
            final ConcurrentMap<String, JsonArray> grouped =
                Ut.elementGroup(directoryD, KName.Component.RUN_COMPONENT);


            final ConcurrentMap<Fs, Set<String>> directoryMap = new ConcurrentHashMap<>();
            final ConcurrentMap<Fs, JsonArray> groupedComponents = Is.fsGroup(grouped, JsonArray::isEmpty);
            /*
             * Extract JsonArray data `storePath` into set for removing here.
             */
            groupedComponents.forEach((fs, jarray) -> {
                final Set<String> valueSet = new HashSet<>();
                Ut.itJArray(jarray).forEach(json -> valueSet.add(json.getString(KName.STORE_PATH)));
                directoryMap.put(fs, valueSet);
            });
            return Ux.future(directoryMap);
        });
    }

    private Future<ConcurrentMap<Fs, Set<String>>> directoryU(final JsonArray directoryJ, final Boolean active) {
        final JsonArray normalized = Is.dataIn(directoryJ);
        Ut.itJArray(normalized).forEach(json -> {
            json.put(KName.ACTIVE, active);
            json.put(KName.UPDATED_AT, Instant.now());
        });
        final List<IDirectory> directories = Ux.fromJson(normalized, IDirectory.class);
        return Ux.Jooq.on(IDirectoryDao.class).updateAsync(directories)
            .compose(updated -> {
                final ConcurrentMap<String, List<String>> grouped =
                    Ut.elementGroup(updated, IDirectory::getRunComponent, IDirectory::getStorePath);


                final ConcurrentMap<Fs, Set<String>> directoryMap = new ConcurrentHashMap<>();
                final ConcurrentMap<Fs, List<String>> groupedComponents = Is.fsGroup(grouped, List::isEmpty);
                groupedComponents.forEach((fs, list) -> directoryMap.put(fs, new HashSet<>(list)));
                return Ux.future(directoryMap);
            });
    }
}
