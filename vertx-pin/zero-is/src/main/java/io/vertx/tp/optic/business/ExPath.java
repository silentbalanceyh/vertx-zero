package io.vertx.tp.optic.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.uca.AbstractExPath;
import io.vertx.tp.is.uca.FsKit;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
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
        return FsKit.queryDirectory(data, KName.STORE_PATH).compose(queried -> {
            final ConcurrentMap<ChangeFlag, JsonArray> compared = FsKit.compareDirectory(data, queried);
            /*
             * 1. ADD queue ( attach `directoryId` in processed )
             * 2. UPDATE queue ( attach `directoryId` )
             */
            final List<Future<JsonArray>> futures = new ArrayList<>();
            futures.add(this.commandMkdir(compared.getOrDefault(ChangeFlag.ADD, new JsonArray()), config));
            futures.add(this.commandMkdir(compared.getOrDefault(ChangeFlag.UPDATE, new JsonArray()), queried));
            return Ux.thenCombineArray(futures);
        });
    }

    @Override
    public Future<JsonArray> dirLs(final String directoryId) {
        /*
         * Fetch data which `parentId` = `directoryId`
         */
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.PARENT_ID, directoryId);
        return FsKit.queryDirectory(condition);
    }

    @Override
    public Future<JsonArray> dirLsR(final String sigma) {
        final JsonObject condition = Ux.whereAnd();
        condition.put("parentId,n", "");
        condition.put(KName.SIGMA, sigma);
        return FsKit.queryDirectory(condition);
    }
}
