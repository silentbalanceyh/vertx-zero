package io.vertx.tp.optic.feature;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

/**
 * Configuration extracting here to get
 *
 * key1 = data
 * key2 = data
 * key3 = data
 *
 * This node will be mount to X_APP record.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Modulat {
    /*
     * Fetch the data by `appId` to get
     *
     * key = data
     *
     * - Here the key is `uiConfig` of each `X_BAG`
     * --- 1) type = EXTENSION
     * --- 2) store of uiConfig
     */
    Future<ConcurrentMap<String, JsonObject>> extension(String appId);
}
