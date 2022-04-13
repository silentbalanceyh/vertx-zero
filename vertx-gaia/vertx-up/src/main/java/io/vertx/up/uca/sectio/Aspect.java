package io.vertx.up.uca.sectio;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * New Structure for configuration fixed
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Aspect {
    private static transient final ConcurrentMap<String, Aspect> POOL_ASPECT
        = new ConcurrentHashMap<>();
    private final AspectConfig config;

    private Aspect(final JsonObject components) {
        this.config = AspectConfig.create(components);
    }

    public static Aspect create(final JsonObject components) {
        final JsonObject config = Ut.valueJObject(components);
        final String cacheKey = String.valueOf(config.hashCode());
        return Fn.poolThread(POOL_ASPECT, () -> new Aspect(config), cacheKey);
    }

    public Function<JsonObject, Future<JsonObject>> wrapJCreate(
        final Function<JsonObject, Future<JsonObject>> executor) {
        return this.aopFn(JsonObject::new, executor, ChangeFlag.ADD);
    }

    public Function<JsonObject, Future<JsonObject>> wrapJSave(
        final Function<JsonObject, Future<JsonObject>> executor) {
        return this.aopFn(JsonObject::new, executor, ChangeFlag.ADD, ChangeFlag.UPDATE);
    }

    public Function<JsonObject, Future<JsonObject>> wrapJUpdate(
        final Function<JsonObject, Future<JsonObject>> executor) {
        return this.aopFn(JsonObject::new, executor, ChangeFlag.UPDATE);
    }

    public Function<JsonObject, Future<JsonObject>> wrapJDelete(
        final Function<JsonObject, Future<JsonObject>> executor) {
        return this.aopFn(JsonObject::new, executor, ChangeFlag.DELETE);
    }

    public Function<JsonArray, Future<JsonArray>> wrapACreate(
        final Function<JsonArray, Future<JsonArray>> executor) {
        return this.aopFn(JsonArray::new, executor, ChangeFlag.ADD);
    }

    public Function<JsonArray, Future<JsonArray>> wrapASave(
        final Function<JsonArray, Future<JsonArray>> executor) {
        return this.aopFn(JsonArray::new, executor, ChangeFlag.ADD, ChangeFlag.UPDATE);
    }

    public Function<JsonArray, Future<JsonArray>> wrapAUpdate(
        final Function<JsonArray, Future<JsonArray>> executor) {
        return this.aopFn(JsonArray::new, executor, ChangeFlag.UPDATE);
    }

    public Function<JsonArray, Future<JsonArray>> wrapADelete(
        final Function<JsonArray, Future<JsonArray>> executor) {
        return this.aopFn(JsonArray::new, executor, ChangeFlag.DELETE);
    }

    private <T> Function<T, Future<T>> aopFn(
        final Supplier<T> supplierDefault,
        final Function<T, Future<T>> runner,
        final ChangeFlag... type) {
        return input -> {
            if (Objects.isNull(input)) {
                // Empty Input Captured
                return Ux.future(supplierDefault.get());
            }
            // Before
            return Ux.future(input)
                // Run Before
                .compose(processed -> Fn.passion(processed,
                    this.plugin(this.config::nameBefore, Before::types, type)))
                // Runner
                .compose(runner)
                // After
                .compose(processed -> Fn.passion(processed,
                    this.plugin(this.config::nameAfter, After::types, type)))
                // Job
                .compose(processed -> Fn.parallel(processed,
                    this.plugin(this.config::nameJob, After::types, type)));
        };
    }

    // ========================= Plugin Initialize ============================

    private <P, T> List<Function<T, Future<T>>> plugin(final Supplier<List<Class<?>>> fnClass,
                                                       final Function<P, Set<ChangeFlag>> fnTypes, final ChangeFlag... type) {
        final List<Class<?>> pluginCls = fnClass.get();
        final List<Function<T, Future<T>>> executor = new ArrayList<>();
        pluginCls.stream().map(plugin -> {
            final P beforeFn = Ut.instance(plugin);
            final Set<ChangeFlag> supported = fnTypes.apply(beforeFn);
            // Expected:  supported
            // Limitation: type
            if (Arrays.stream(type).anyMatch(supported::contains)) {
                final JsonObject config = this.config.config(plugin);
                return this.<P, T>buildFn(beforeFn, config);
            } else {
                return null;
            }
        }).filter(Objects::nonNull).forEach(executor::add);
        return executor;
    }

    @SuppressWarnings("all")
    private <P, T> Function<T, Future<T>> buildFn(final P plugin, final JsonObject config) {
        return input -> {
            if (plugin instanceof Before) {
                // Run Before
                final Before before = (Before) plugin;
                if (input instanceof JsonObject) {
                    return before.beforeAsync((JsonObject) input, config)
                        .compose(json -> Ux.future((T) json));
                } else {
                    return before.beforeAsync((JsonArray) input, config)
                        .compose(array -> Ux.future((T) array));
                }
            } else {
                // Run After
                final After after = (After) plugin;
                if (input instanceof JsonObject) {
                    return after.afterAsync((JsonObject) input, config)
                        .compose(json -> Ux.future((T) json));
                } else {
                    return after.afterAsync((JsonArray) input, config)
                        .compose(array -> Ux.future((T) array));
                }
            }
        };
    }
}
