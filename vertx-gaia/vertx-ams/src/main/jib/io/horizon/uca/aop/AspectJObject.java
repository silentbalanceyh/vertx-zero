package io.horizon.uca.aop;

import io.horizon.util.HUt;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author lang : 2023-05-27
 */
class AspectJObject {

    // Full
    // ----- Before ---- Executor ---- After ( Configuration )
    // -----   o    ----    o     ----   o   (  o  )
    @SuppressWarnings("all")
    <T> Function<T, Future<T>> wrapAop(
        final Before before, final Function<T, Future<T>> executor, final After after,
        final JsonObject configuration) {
        Objects.requireNonNull(executor);
        final JsonObject config = HUt.valueJObject(configuration);
        return input -> {
            // Default to input
            Future<T> stepFuture = Future.succeededFuture(input);
            if (Objects.nonNull(before)) {
                final JsonObject beforeConfig = HUt.valueJObject(config, before.getClass().getName());
                // Execute Before on input
                if (input instanceof JsonObject) {
                    stepFuture = before.beforeAsync((JsonObject) input, beforeConfig)
                        .compose(json -> Future.succeededFuture((T) json));
                } else if (input instanceof JsonArray) {
                    stepFuture = before.beforeAsync((JsonArray) input, beforeConfig)
                        .compose(array -> Future.succeededFuture((T) array));
                }
            }
            // Executor Default
            stepFuture = stepFuture.compose(executor);
            if (Objects.nonNull(after)) {
                final JsonObject afterConfig = HUt.valueJObject(config, after.getClass().getName());
                // Execute After on output
                if (input instanceof JsonObject) {
                    stepFuture = stepFuture
                        .compose(out -> after.afterAsync((JsonObject) out, afterConfig))
                        .compose(out -> Future.succeededFuture((T) out));
                } else if (input instanceof JsonArray) {
                    stepFuture = stepFuture
                        .compose(out -> after.afterAsync((JsonArray) out, afterConfig))
                        .compose(out -> Future.succeededFuture((T) out));
                }
            }
            return stepFuture;
        };
    }
}
