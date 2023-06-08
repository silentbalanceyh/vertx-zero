package io.vertx.mod.ambient.uca.digital;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Serial {
    /*
     * Number Generation
     */
    Future<JsonArray> generate(JsonObject condition, Integer count);

    Future<Boolean> reset(JsonObject condition, Long defaultValue);
}
