package io.vertx.tp.ambient.cache;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class AcCache {
    /*
     * Module Cache For future usage
     */
    public static Future<JsonObject> getModule(final JsonObject condition, final Supplier<Future<JsonObject>> executor) {
        return AcModule.getModule(condition, executor);
    }
}
