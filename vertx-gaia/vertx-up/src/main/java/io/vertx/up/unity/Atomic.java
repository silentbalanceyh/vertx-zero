package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.json.JsonArray;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    @SuppressWarnings("all")
    static Future<Boolean> nativeInit(final JsonArray components, final Vertx vertx) {
        /* Extract Component Class and calculate async and sync */
        final List<Future<Boolean>> async = new ArrayList<>();
        final List<Class<?>> sync = new ArrayList<>();
        Ut.itJArray(components).forEach(json -> {
            final String className = json.getString("component");
            final Class<?> clazz = Ut.clazz(className, null);
            if (Objects.nonNull(clazz)) {
                final boolean isAsync = json.getBoolean("async", Boolean.FALSE);
                if (isAsync) {
                    // Async:  Future<Boolean> init(Vertx vertx) | init()
                    final Future<Boolean> ret = (Future<Boolean>) invoke(clazz, vertx);
                    if (Objects.nonNull(ret)) {
                        async.add(ret);
                    }
                } else {
                    sync.add(clazz);
                }
            }
        });
        // Async First
        return Ux.thenCombineT(async).compose(ret -> {
            // Sync: void init(Vertx vertx) | init()
            final List<Future<Boolean>> futures = new ArrayList<>();
            sync.stream().map(each -> invokeSync(each, vertx)).forEach(futures::add);
            return Ux.thenCombineT(futures);
        }).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    private static Future<Boolean> invokeSync(final Class<?> clazz, final Vertx vertx) {
        invoke(clazz, vertx);
        return Future.succeededFuture(Boolean.TRUE);
    }

    @SuppressWarnings("all")
    private static Object invoke(final Class<?> clazz, final Vertx vertx) {
        return Fn.getJvm(() -> {
            final Method initMethod = Arrays.asList(clazz.getDeclaredMethods())
                .stream().filter(method -> "init".equals(method.getName()))
                .findFirst().orElse(null);
            if (Objects.isNull(initMethod)) {
                // No method
                return null;
            }
            final int counter = initMethod.getParameterTypes().length;
            if (0 == counter) {
                // public static void init()
                return initMethod.invoke(null);
            } else {
                // public static Future<Boolean> init(Vertx vertx)
                if (Objects.isNull(vertx)) {
                    return null;
                } else {
                    return initMethod.invoke(null, vertx);
                }
            }
        });
    }
}
