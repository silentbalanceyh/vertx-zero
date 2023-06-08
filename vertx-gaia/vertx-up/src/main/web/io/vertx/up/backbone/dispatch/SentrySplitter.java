package io.vertx.up.backbone.dispatch;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.agent.Depot;
import io.vertx.up.backbone.Sentry;
import io.vertx.up.util.Ut;

/**
 * Validation for request based on JSR303 Bean Validation
 * 1. Basic Parameters: @QueryParam, @PathParam
 * 2. Extend Parameters: @BodyParam -> JsonObject, JsonArray
 * 3. POJO Parameters: @BodyParam -> POJO
 */
public class SentrySplitter {

    public Sentry<RoutingContext> distribute(final Depot depot) {
        // Annotation to different verifier workflow
        // In current situation, there is only one implementation to build StandardVerifier
        // In future we could extend this implementation
        return Ut.singleton(StandardVerifier.class);
    }
}
