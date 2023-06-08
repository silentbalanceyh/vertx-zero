package io.vertx.mod.ui.refine;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.init.UiPin;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.util.Ut;

import java.util.function.Supplier;

import static io.vertx.mod.ui.refine.Ui.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class UiCache {

    private static final Annal LOGGER = Annal.get(UiCache.class);

    public static Future<JsonObject> cacheControl(final JsonObject body,
                                                  final Supplier<Future<JsonObject>> executor) {
        return getCache(UiPin::keyControl, body, executor);
    }

    public static Future<JsonArray> cacheOps(final JsonObject body,
                                             final Supplier<Future<JsonArray>> executor) {
        return getCache(UiPin::keyOps, body, executor);
    }

    private static <T> Future<T> getCache(
        final Supplier<String> poolFn,
        final JsonObject body,
        final Supplier<Future<T>> executor) {
        final String keyPool = poolFn.get();
        if (Ut.isNotNil(keyPool)) {
            final String uiKey = String.valueOf(body.hashCode());
            return Rapid.<String, T>t(keyPool).cached(uiKey, executor);
        } else {
            LOG.Ui.info(LOGGER, "Ui Cached has been disabled!");
            return executor.get();
        }
    }
}
