package io.horizon.spi.extension;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.function.Function;

/**
 * ## 「Init」Initializing Uniform Interface
 *
 * OOB Data initialization for The whole application.
 * 1) XApp: EmApp Data
 * 2) XSource: Data Source Data
 * 3) Extension: Configuration For Initializer Extension for other flow.
 */
public interface Init {

    /*
     * Initializer generate method.
     */
    static Init generate(final Class<?> clazz) {
        return Pool.CC_INIT.pick(() -> Ut.instance(clazz), clazz.getName()); //
        // Fn.po?l(Pool.INIT_POOL, clazz.getName(), () -> Ut.instance(clazz));
    }

    /*
     * Executor Constructor
     */
    Function<JsonObject, Future<JsonObject>> apply();

    /*
     * Unique condition for current object
     */
    default JsonObject whereUnique(final JsonObject input) {
        /* Default situation, nothing to do */
        return new JsonObject();
    }

    /*
     * Executor result hooker
     */
    default JsonObject result(final JsonObject input, final JsonObject appJson) {
        /* Default situation, return to appJson */
        return appJson;
    }
}
