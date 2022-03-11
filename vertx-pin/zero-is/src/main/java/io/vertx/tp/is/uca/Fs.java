package io.vertx.tp.is.uca;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * File System Here for integration
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Fs {
    /*
     * Default value processing
     */
    IDirectory initialize(JsonObject directory);

    /*
     * 1. Sync Data between ( Actual / Database )
     * 2. Command: mkdir
     */
    Future<JsonArray> synchronize(JsonArray data, JsonObject config);

    /*
     * Command: mkdir
     * - JsonArray
     * - JsonObject
     */
    Future<JsonArray> mkdir(JsonArray data);

    Future<JsonObject> mkdir(JsonObject data);
}
