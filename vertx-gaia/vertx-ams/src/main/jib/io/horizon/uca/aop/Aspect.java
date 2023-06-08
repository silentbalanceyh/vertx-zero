package io.horizon.uca.aop;

import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.cache.Cc;
import io.horizon.util.HUt;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.function.Function;

/**
 * New Structure for configuration fixed
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Aspect {

    private static final Cc<String, Aspect> CC_ASPECT = Cc.openThread();
    private final transient AspectT executorT;
    private final transient AspectJObject executorJ;

    private Aspect(final JsonObject components) {
        this.executorT = new AspectT(AspectRobin.create(components));
        this.executorJ = new AspectJObject();
    }

    private Aspect(final AspectRobin config) {
        this.executorT = new io.horizon.uca.aop.AspectT(config);
        this.executorJ = new io.horizon.uca.aop.AspectJObject();
    }

    public static Aspect create(final JsonObject components) {
        final JsonObject config = HUt.valueJObject(components);
        final String cacheKey = String.valueOf(config.hashCode());
        return CC_ASPECT.pick(() -> new Aspect(config), cacheKey);
        // Fn.po?lThread(POOL_ASPECT, () -> new Aspect(config), cacheKey);
    }

    public static Aspect create(final AspectRobin config) {
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
        return this.executorJ.wrapAop(around, executor, around, configuration);
    }

    public <T> Function<T, Future<T>> wrapAop(
        final Around around, final Function<T, Future<T>> executor) {
        Objects.requireNonNull(around);
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   o    ----    o     ----   o   (  x  )
        return this.executorJ.wrapAop(around, executor, around, null);
    }

    // Before with Config
    // Before without Config
    public <T> Function<T, Future<T>> wrapAop(
        final Before before, final Function<T, Future<T>> executor,
        final JsonObject configuration) {
        Objects.requireNonNull(before);
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   o    ----    o     ----   x   (  o  )
        return this.executorJ.wrapAop(before, executor, null, configuration);
    }

    public <T> Function<T, Future<T>> wrapAop(
        final Before before, final Function<T, Future<T>> executor) {
        Objects.requireNonNull(before);
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   o    ----    o     ----   x   (  x  )
        return this.executorJ.wrapAop(before, executor, null, null);
    }


    // After with Config
    // After without Config
    public Function<JsonObject, Future<JsonObject>> wrapAop(
        final Function<JsonObject, Future<JsonObject>> executor, final After after,
        final JsonObject configuration) {
        Objects.requireNonNull(after);
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   x    ----    o     ----   o   (  o  )
        return this.executorJ.wrapAop(null, executor, after, configuration);
    }

    public Function<JsonObject, Future<JsonObject>> wrapAop(
        final Function<JsonObject, Future<JsonObject>> executor, final After after) {
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   x    ----    o     ----   o   (  x  )
        return this.executorJ.wrapAop(null, executor, after, null);
    }


    // Before/After with Config
    // Before/After without Config
    public <T> Function<T, Future<T>> wrapAop(
        final Before before, final Function<T, Future<T>> executor, final After after) {
        // ----- Before ---- Executor ---- After ( Configuration )
        // -----   o    ----    o     ----   o   (  x  )
        return this.executorJ.wrapAop(before, executor, after, null);
    }

    // ====================== Standard API for CRUD ========================
    public Function<JsonObject, Future<JsonObject>> wrapJCreate(
        final Function<JsonObject, Future<JsonObject>> executor) {
        return this.executorT.aopFn(JsonObject::new, executor, ChangeFlag.ADD);
    }

    public Function<JsonObject, Future<JsonObject>> wrapJSave(
        final Function<JsonObject, Future<JsonObject>> executor) {
        return this.executorT.aopFn(JsonObject::new, executor, ChangeFlag.ADD, ChangeFlag.UPDATE);
    }

    public Function<JsonObject, Future<JsonObject>> wrapJUpdate(
        final Function<JsonObject, Future<JsonObject>> executor) {
        return this.executorT.aopFn(JsonObject::new, executor, ChangeFlag.UPDATE);
    }

    public Function<JsonObject, Future<JsonObject>> wrapJDelete(
        final Function<JsonObject, Future<JsonObject>> executor) {
        return this.executorT.aopFn(JsonObject::new, executor, ChangeFlag.DELETE);
    }

    public Function<JsonArray, Future<JsonArray>> wrapACreate(
        final Function<JsonArray, Future<JsonArray>> executor) {
        return this.executorT.aopFn(JsonArray::new, executor, ChangeFlag.ADD);
    }

    public Function<JsonArray, Future<JsonArray>> wrapASave(
        final Function<JsonArray, Future<JsonArray>> executor) {
        return this.executorT.aopFn(JsonArray::new, executor, ChangeFlag.ADD, ChangeFlag.UPDATE);
    }

    public Function<JsonArray, Future<JsonArray>> wrapAUpdate(
        final Function<JsonArray, Future<JsonArray>> executor) {
        return this.executorT.aopFn(JsonArray::new, executor, ChangeFlag.UPDATE);
    }

    public Function<JsonArray, Future<JsonArray>> wrapADelete(
        final Function<JsonArray, Future<JsonArray>> executor) {
        return this.executorT.aopFn(JsonArray::new, executor, ChangeFlag.DELETE);
    }

    // ====================== Before/After API Only ========================
    public Function<JsonObject, Future<JsonObject>> wrapJBefore(final ChangeFlag... type) {
        return this.executorT.beforeFn(JsonObject::new, type);
    }

    public Function<JsonArray, Future<JsonArray>> wrapABefore(final ChangeFlag... type) {
        return this.executorT.beforeFn(JsonArray::new, type);
    }

    public Function<JsonObject, Future<JsonObject>> wrapJAfter(final ChangeFlag... type) {
        return this.executorT.afterFn(JsonObject::new, type);
    }

    public Function<JsonArray, Future<JsonArray>> wrapAAfter(final ChangeFlag... type) {
        return this.executorT.afterFn(JsonArray::new, type);
    }
}
