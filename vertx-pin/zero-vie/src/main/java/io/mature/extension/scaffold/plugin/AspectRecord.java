package io.mature.extension.scaffold.plugin;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/**
 *
 */
public class AspectRecord extends AbstractAspect {

    @Override
    public Future<JsonObject> beforeAsync(final JsonObject input,
                                          final JsonObject options) {
        return Ux.future(input, this.queue.beforePlugin(options))
            .otherwise(Ux.otherwise(() -> input));
    }

    @Override
    public Future<JsonObject> afterAsync(final JsonObject input,
                                         final JsonObject options) {
        return Ux.future(input)
            .compose(processed -> Ux.future(processed, this.queue.afterPlugin(options)))    // Sync
            .compose(processed -> Ux.future(processed, this.queue.jobPlugin(options)))      // Async
            .otherwise(Ux.otherwise(() -> input));
    }
}
