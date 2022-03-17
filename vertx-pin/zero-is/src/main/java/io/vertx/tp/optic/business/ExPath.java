package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.uca.command.FsHelper;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExPath extends AbstractExPath {

    @Override
    public Future<JsonArray> dirMk(final JsonArray data, final JsonObject config) {
        /*
         * 1. Fetch all data of IDirectory
         * -- The condition is `storePath` instead of other information
         * 2. Build the map of `storePath = IDirectory`, here will put `directoryId` into each data
         */
        return FsHelper.directoryQuery(data, KName.STORE_PATH, false).compose(queried -> {
            final ConcurrentMap<ChangeFlag, JsonArray> compared = FsHelper.directoryDiff(data, queried);
            /*
             * 1. ADD queue ( attach `directoryId` in processed )
             * 2. UPDATE queue ( attach `directoryId` )
             * 3. NONE queue ( existing )
             */
            final List<Future<JsonArray>> futures = new ArrayList<>();
            futures.add(this.commandMkdir(compared.getOrDefault(ChangeFlag.ADD, new JsonArray()), config));
            futures.add(this.commandMkdir(compared.getOrDefault(ChangeFlag.UPDATE, new JsonArray()), queried));
            futures.add(Ux.future(compared.getOrDefault(ChangeFlag.NONE, new JsonArray())));
            return Ux.thenCombineArray(futures)
                .compose(Ut.ifJArray(KName.METADATA, KName.VISIT_GROUP, KName.VISIT_ROLE, KName.VISIT_MODE));
        });
    }

    @Override
    public Future<JsonArray> dirLs(final String sigma, final String parentId) {
        /*
         *  1. sigma must be not null
         *  2. directoryId is null when `parentId`
         */
        Objects.requireNonNull(sigma);
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.SIGMA, sigma);
        condition.put(KName.ACTIVE, Boolean.TRUE);
        if (Ut.notNil(parentId)) {
            condition.put(KName.PARENT_ID, parentId);
        }
        return FsHelper.directoryQuery(condition);
    }

    @Override
    public Future<JsonArray> dirTrashed(final String sigma) {
        Objects.requireNonNull(sigma);
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.SIGMA, sigma);
        condition.put(KName.ACTIVE, Boolean.FALSE);
        return FsHelper.directoryQuery(condition);
    }
}
