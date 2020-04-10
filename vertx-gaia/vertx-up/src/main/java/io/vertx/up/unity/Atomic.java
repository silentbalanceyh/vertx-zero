package io.vertx.up.unity;

import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;

class Atomic {

    static void initComponent(final JsonObject init) {
        /* Extract Component Class */
        final String className = init.getString("component");
        final Class<?> clazz = Ut.clazz(className);
        if (null != clazz) {
            /* Call init() method here */
            Fn.safeJvm(() -> {
                final Method initMethod = clazz.getDeclaredMethod("init");
                initMethod.invoke(null);
            });
        }
    }
}
