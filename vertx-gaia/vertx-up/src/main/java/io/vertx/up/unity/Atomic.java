package io.vertx.up.unity;

import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.ZeroLauncher;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.jooq.JooqInfix;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

class Atomic {

    static Vertx nativeVertx() {
        return ZeroLauncher.nativeRef();
    }

    static WorkerExecutor nativeWorker(final String name, final Integer minutes) {
        return nativeVertx().createSharedWorkerExecutor(name, 2, minutes, TimeUnit.MINUTES);
    }

    static <T> Future<T> nativeWorker(final String name, final Handler<Promise<T>> handler) {
        final Promise<T> promise = Promise.promise();
        final WorkerExecutor executor = nativeWorker(name, 10);
        executor.executeBlocking(
            handler,
            post -> {
                // Fix Issue:
                // Task io.vertx.core.impl.TaskQueue$$Lambda$367/0x00000008004f3440@2b1e3784 rejected from
                // java.util.concurrent.ThreadPoolExecutor@1db1d316
                // [Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0]
                executor.close();
                promise.complete(post.result());
            }
        );
        return promise.future();
    }


    @SuppressWarnings("all")
    static Future<Boolean> nativeInit(final JsonObject initConfig, final Vertx vertx) {
        /* Extract Component Class and consider running at the same time */
        return Ux.future(initConfig)
            .compose(configuration -> {
                // 1. nativeComponents first
                final JsonArray components = Ut.valueJArray(initConfig, YmlCore.init.CONFIGURE);
                return nativeComponents(components, vertx);
            })
            .compose(nil -> {
                // 2. nativeBridge first
                final JsonArray bridges = Ut.valueJArray(initConfig, YmlCore.init.COMPILE);
                if (0 < bridges.size()) {
                    // JooqConfigurationException
                    JooqInfix.init(vertx);
                    return nativeBridge(bridges, vertx);
                } else {
                    return Ux.futureT();
                }
            });
    }

    private static Future<Boolean> nativeBridge(final JsonArray bridges, final Vertx vertx) {
        final List<JsonObject> ordered = bridges.stream()
            .filter(json -> json instanceof JsonObject)
            .map(json -> (JsonObject) json)
            // .sorted(Comparator.comparingInt(left -> left.getInteger(KName.ORDER, 0)))
            .toList();
        final Set<Function<Boolean, Future<Boolean>>> queue = new HashSet<>();
        // passion execute（按顺序执行bridge组件）
        ordered.forEach(params -> {
            final Class<?> componentCls = Ut.valueC(params, YmlCore.init.compile.COMPONENT);
            if (Objects.nonNull(componentCls)) {
                final Set<Method> methodSet = nativeBridgeMethod(componentCls);
                methodSet.forEach(method -> queue.add(json -> invokeComponent(componentCls, method, vertx)));
            }
        });
        return Fn.parallel(Boolean.TRUE, queue);
    }

    private static Set<Method> nativeBridgeMethod(final Class<?> clazz) {
        final Method[] methods = clazz.getDeclaredMethods();
        final Set<Method> methodSet = new HashSet<>();
        for (final Method method : methods) {
            /*
             * 1. name must start with `init`
             * 2. parameters definition must be 0 or 1
             * 3. 1 -> Return type must be Future<>
             *    0 -> Return type must be void
             */
            final int modifier = method.getModifiers();
            if (!Modifier.isPublic(modifier)) {
                continue;
            }
            if (!Modifier.isStatic(modifier)) {
                continue;
            }
            final String name = method.getName();
            if (!name.startsWith(YmlCore.init.__KEY)) {
                continue;
            }
            final int counter = method.getParameterTypes().length;
            if (1 < counter) {
                continue;
            }
            if (1 == counter) {
                final Class<?> type = method.getParameterTypes()[0];
                if (Vertx.class != type) {
                    continue;
                }
            }
            methodSet.add(method);
        }
        return methodSet;
    }

    @SuppressWarnings("all")
    private static Future<Boolean> nativeComponents(final JsonArray components, final Vertx vertx) {
        /* Extract Component Class and consider running at the same time */
        final List<Future<Boolean>> async = new ArrayList<>();
        Ut.itJArray(components).forEach(json -> {
            final String className = json.getString(YmlCore.init.configure.COMPONENT);
            final Class<?> clazz = Ut.clazz(className, null);
            if (Objects.nonNull(clazz)) {
                /*
                 * Re-Calc the workflow by `init` method
                 * 1. init(Vertx) first
                 * 2. init() Secondary
                 */
                final Future<Boolean> ret = invokeComponent(clazz, YmlCore.init.__KEY, vertx);
                if (Objects.nonNull(ret)) {
                    async.add(ret);
                }
            }
        });
        return Fn.combineB(async);
    }

    private static Future<Boolean> invokeComponent(final Class<?> clazz, final String methodName, final Vertx vertx) {
        Objects.requireNonNull(clazz);
        /*
         * Re-Calc the workflow by `init` method
         * 1. init(Vertx) first
         * 2. init() Secondary
         */
        final Method[] methods = clazz.getDeclaredMethods();
        final Method methodInit = Arrays.stream(methods)
            .filter(method -> methodName.equals(method.getName()))
            .findFirst().orElse(null);
        return invokeComponent(clazz, methodInit, vertx);
    }

    @SuppressWarnings("unchecked")
    private static Future<Boolean> invokeComponent(final Class<?> clazz, final Method methodInit, final Vertx vertx) {
        Objects.requireNonNull(clazz);
        if (Objects.nonNull(methodInit)) {
            final int counter = methodInit.getParameterTypes().length;
            final boolean isAsync = 0 < counter;
            if (isAsync) {
                // Async:  Future<Boolean> init(Vertx vertx) | init()
                return Fn.failOr(() -> (Future<Boolean>) methodInit.invoke(null, vertx));
            } else {
                // Sync:   void init(Vertx vertx) | init()
                return Fn.failOr(() -> {
                    methodInit.invoke(null);
                    return Future.succeededFuture(Boolean.TRUE);
                });
            }
        } else {
            // Empty Body ( Not invoking happened )
            return Ux.futureT();
        }
    }
}
