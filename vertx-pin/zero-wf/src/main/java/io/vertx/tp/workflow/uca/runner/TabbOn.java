package io.vertx.tp.workflow.uca.runner;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface TabbOn {
    /*
     * Recorder for ADD / UPDATE / DELETE
     */
    Future<JsonObject> traceAsync(JsonObject previous, JsonObject current, JsonObject config);

    Future<JsonArray> traceAsync(JsonArray previous, JsonArray current, JsonObject config);
}
