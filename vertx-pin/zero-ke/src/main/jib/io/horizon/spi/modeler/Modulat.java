package io.horizon.spi.modeler;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

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
    Future<JsonObject> extension(JsonObject appJson);

    Future<JsonObject> extension(String appId);
}
