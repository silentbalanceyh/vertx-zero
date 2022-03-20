package io.vertx.tp.optic.business;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.refine.Is;
import io.vertx.tp.is.uca.command.Fs;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.Instant;
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
        if (Ut.notNil(parentId)) {
            condition.put(KName.PARENT_ID, parentId);
        }
        return Is.directoryQr(condition).compose(Ux::futureA).compose(Is::dataOut);
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
                return Ux.thenCombineT(futures);
            }))
            .compose(nil -> Ux.future(directoryJ));
    }

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
                // trashIn
                renameMap = Is.trashIn(storeSet);
            }
            futures.add(fs.rename(renameMap));
        });
        return Ux.thenCombineT(futures).compose(nil -> Ux.futureT());
    }

    private Future<ConcurrentMap<Fs, Set<String>>> directoryD(final JsonArray directoryD) {
        final JsonObject criteria = new JsonObject();
        criteria.put(KName.KEY + ",i", Ut.toJArray(Ut.valueSetString(directoryD, KName.KEY)));
        return Ux.Jooq.on(IDirectoryDao.class).deleteByAsync(criteria).compose(removed -> {
            // Grouped directory by runComponent
            final ConcurrentMap<String, JsonArray> grouped =
                Ut.elementGroup(directoryD, KName.Component.RUN_COMPONENT);


            final ConcurrentMap<Fs, Set<String>> directoryMap = new ConcurrentHashMap<>();
            final ConcurrentMap<Fs, JsonArray> groupedComponents = Is.fsGroup(grouped, JsonArray::isEmpty);
            groupedComponents.forEach((fs, jarray) -> directoryMap.put(fs, Ut.toSet(jarray)));
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
        return Ux.Jooq.on(IDirectoryDao.class).updateAsync(directories).compose(updated -> {
            final ConcurrentMap<String, List<String>> grouped =
                Ut.elementGroup(updated, IDirectory::getRunComponent, IDirectory::getStorePath);


            final ConcurrentMap<Fs, Set<String>> directoryMap = new ConcurrentHashMap<>();
            final ConcurrentMap<Fs, List<String>> groupedComponents = Is.fsGroup(grouped, List::isEmpty);
            groupedComponents.forEach((fs, list) -> directoryMap.put(fs, new HashSet<>(list)));
            return Ux.future(directoryMap);
        });
    }
}
