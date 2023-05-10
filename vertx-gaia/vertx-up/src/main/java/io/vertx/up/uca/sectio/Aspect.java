package io.vertx.up.uca.sectio;

import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * New Structure for configuration fixed
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Aspect {

    private static final Cc<String, Aspect> CC_ASPECT = Cc.openThread();
    private final AspectConfig config;

    private Aspect(final JsonObject components) {
        this.config = AspectConfig.create(components);
    }

    private Aspect(final AspectConfig config) {
        this.config = config;
    }

    public static Aspect create(final JsonObject components) {
        final JsonObject config = Ut.valueJObject(components);
        final String cacheKey = String.valueOf(config.hashCode());
        return CC_ASPECT.pick(() -> new Aspect(config), cacheKey);
        // Fn.po?lThread(POOL_ASPECT, () -> new Aspect(config), cacheKey);
    }

    public static Aspect create(final AspectConfig config) {
        Objects.requireNonNull(config);
        return CC_ASPECT.pick(() -> new Aspect(config), String.valueOf(config.hashCode()));
        // Fn.po?lThread(POOL_ASPECT, () -> new Aspect(config), String.valueOf(config.hashCode()));
    }

    // ====================== Single Api ========================
    // Around with Config
    // Around without Config
    public <T> Function<T, Future<T>> wrapAop(
        final Around around, final Function<T, Future<T>> executor,
        final JsonObject configuration) {
        Objects.requireNonNull(around);
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   o    ----    o     ----   o   (  o  )
        return this.wrapAop(around, executor, around, configuration);
    }

    public <T> Function<T, Future<T>> wrapAop(
        final Around around, final Function<T, Future<T>> executor) {
        Objects.requireNonNull(around);
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   o    ----    o     ----   o   (  x  )
        return this.wrapAop(around, executor, around, null);
    }

    // Before with Config
    // Before without Config
    public <T> Function<T, Future<T>> wrapAop(
        final Before before, final Function<T, Future<T>> executor,
        final JsonObject configuration) {
        Objects.requireNonNull(before);
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   o    ----    o     ----   x   (  o  )
        return this.wrapAop(before, executor, null, configuration);
    }

    public <T> Function<T, Future<T>> wrapAop(
        final Before before, final Function<T, Future<T>> executor) {
        Objects.requireNonNull(before);
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   o    ----    o     ----   x   (  x  )
        return this.wrapAop(before, executor, null, null);
    }


    // After with Config
    // After without Config
    public Function<JsonObject, Future<JsonObject>> wrapAop(
        final Function<JsonObject, Future<JsonObject>> executor, final After after,
        final JsonObject configuration) {
        Objects.requireNonNull(after);
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   x    ----    o     ----   o   (  o  )
        return this.wrapAop(null, executor, after, configuration);
    }

    public Function<JsonObject, Future<JsonObject>> wrapAop(
        final Function<JsonObject, Future<JsonObject>> executor, final After after) {
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   x    ----    o     ----   o   (  x  )
        return this.wrapAop(null, executor, after, null);
    }


    // Before/After with Config
    // Before/After without Config
    public <T> Function<T, Future<T>> wrapAop(
        final Before before, final Function<T, Future<T>> executor, final After after) {
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   o    ----    o     ----   o   (  x  )
        return this.wrapAop(before, executor, after, null);
    }

    // ====================== Internal Full Api ========================
    // Full
    // ----- Before ---- Executor ---- After ( Configuration )
    // -----   o    ----    o     ----   o   (  o  )
    @SuppressWarnings("all")
    public <T> Function<T, Future<T>> wrapAop(
        final Before before, final Function<T, Future<T>> executor, final After after,
        final JsonObject configuration) {
        Objects.requireNonNull(executor);
        final JsonObject config = Ut.valueJObject(configuration);
        return input -> {
            // Default to input
            Future<T> stepFuture = Ux.future(input);
            if (Objects.nonNull(before)) {
                final JsonObject beforeConfig = Ut.valueJObject(config, before.getClass().getName());
                // Execute Before on input
                if (input instanceof JsonObject) {
                    stepFuture = before.beforeAsync((JsonObject) input, beforeConfig)
                        .compose(json -> Ux.future((T) json));
                } else if (input instanceof JsonArray) {
                    stepFuture = before.beforeAsync((JsonArray) input, beforeConfig)
                        .compose(array -> Ux.future((T) array));
                }
            }
            // Executor Default
            stepFuture = stepFuture.compose(executor);
            if (Objects.nonNull(after)) {
                final JsonObject afterConfig = Ut.valueJObject(config, after.getClass().getName());
                // Execute After on output
                if (input instanceof JsonObject) {
                    stepFuture = stepFuture
                        .compose(out -> after.afterAsync((JsonObject) out, afterConfig))
                        .compose(out -> Ux.future((T) out));
                } else if (input instanceof JsonArray) {
                    stepFuture = stepFuture
                        .compose(out -> after.afterAsync((JsonArray) out, afterConfig))
                        .compose(out -> Ux.future((T) out));
                }
            }
            return stepFuture;
        };
    }

    // ====================== Standard API for CRUD ========================
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

    // ====================== Before/After API Only ========================
    public Function<JsonObject, Future<JsonObject>> wrapJBefore(final ChangeFlag... type) {
        return this.beforeFn(JsonObject::new, type);
    }

    public Function<JsonArray, Future<JsonArray>> wrapABefore(final ChangeFlag... type) {
        return this.beforeFn(JsonArray::new, type);
    }

    public Function<JsonObject, Future<JsonObject>> wrapJAfter(final ChangeFlag... type) {
        return this.afterFn(JsonObject::new, type);
    }

    public Function<JsonArray, Future<JsonArray>> wrapAAfter(final ChangeFlag... type) {
        return this.afterFn(JsonArray::new, type);
    }

    // ====================== Private Api ========================
    private <T> Function<T, Future<T>> beforeFn(
        final Supplier<T> supplierDefault,
        final ChangeFlag... type
    ) {
        return input -> {
            if (Objects.isNull(input)) {
                // Empty Input Captured
                return Ux.future(supplierDefault.get());
            }
            // Before
            return Fn.passion(input,
                this.plugin(this.config::nameBefore, Before::types, type));
        };
    }

    private <T> Function<T, Future<T>> afterFn(
        final Supplier<T> supplierDefault,
        final ChangeFlag... type) {
        return input -> {
            if (Objects.isNull(input)) {
                // Empty Input Captured
                return Ux.future(supplierDefault.get());
            }
            // After
            return Fn.passion(input,
                    this.plugin(this.config::nameAfter, After::types, type))
                // Job
                .compose(processed -> Fn.parallel(processed,
                    this.plugin(this.config::nameJob, After::types, type)));
        };
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
                .compose(inputT -> runner.apply(inputT)
                    .compose(processed -> Ux.futureC(inputT, processed))
                )
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
