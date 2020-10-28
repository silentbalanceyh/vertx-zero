package io.vertx.up.unity;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

class Atomic {

    private static final Vertx VERTX;

    static {
        /* Prepare vertx instance for standalone execution */
        final VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTime(3000_000_000_000L);
        options.setMaxWorkerExecuteTime(3000_000_000_000L);
        options.setBlockedThreadCheckInterval(10000);
        VERTX = Vertx.vertx(options);
    }

    static Vertx nativeVertx() {
        return VERTX;
    }

    static WorkerExecutor nativeWorker(final String name, final Integer minutes) {
        return VERTX.createSharedWorkerExecutor(name, 2, minutes, TimeUnit.MINUTES);
    }

    static void nativeInit(final JsonObject init) {
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
