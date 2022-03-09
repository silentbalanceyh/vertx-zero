package io.vertx.tp.is.uca;

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
     * Command: mkdir
     */
    Future<JsonArray> mkdir(JsonArray data, JsonObject config);
}
