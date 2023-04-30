package io.horizon.uca.log;

import io.horizon.eon.cache.CStore;
import io.horizon.specification.component.HLogger;
import io.horizon.uca.log.internal.BridgeAnnal;
import io.horizon.uca.log.internal.Log4JAnnal;

import java.util.Objects;

/**
 * Unite Logging system connect to vert.x, io.vertx.zero.io.vertx.zero.io.vertx.up.io.vertx.up.io.vertx.up.util kit of Vertx-Zero
 */
public interface Annal extends HLogger {

    static Annal get(final Class<?> clazz) {
        final Class<?> cacheKey = Objects.isNull(clazz) ? Log4JAnnal.class : clazz;
        return CStore.CC_ANNAL_EXTENSION.pick(() -> new BridgeAnnal(clazz), cacheKey);
    }
}
