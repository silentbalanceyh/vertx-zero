package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.refine.Is;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
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
        return Is.fsDocument(data, config).compose(Is::directoryOut);
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
        return Is.directoryQr(condition);
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
        return Is.directoryQr(condition);
    }

    /*
     * Update all the X_DIRECTORY information here.
     *  1. active = false
     *  2. updatedBy = user
     */
    @Override
    public Future<JsonArray> trashIn(final JsonArray directoryJ, final ConcurrentMap<String, String> fileMap) {

        return null;
    }

    @Override
    public Future<JsonArray> trashOut(final JsonArray directoryJ, final ConcurrentMap<String, String> fileMap) {
        return null;
    }
}
