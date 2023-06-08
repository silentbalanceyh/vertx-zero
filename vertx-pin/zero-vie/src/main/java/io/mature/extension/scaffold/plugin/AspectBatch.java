package io.mature.extension.scaffold.plugin;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 *
 */
public class AspectBatch extends AbstractAspect {

    @Override
    public Future<JsonArray> beforeAsync(final JsonArray input,
                                         final JsonObject options) {
        return Ux.future(input, this.queue.beforePlugins(options))
            .otherwise(Ux.otherwise(() -> input));
    }

    @Override
    public Future<JsonArray> afterAsync(final JsonArray input,
                                        final JsonObject options) {
        return Ux.future(input)
            .compose(processed -> Ux.future(processed, this.queue.afterPlugins(options)))   // Sync
            .compose(processed -> Ux.future(processed, this.queue.jobPlugins(options)))     // Async
            .otherwise(Ux.otherwise(() -> input));
    }
}
