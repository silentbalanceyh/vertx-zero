package io.vertx.up.extension;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.Envelop;

/**
 * 「Extension」:
 * Name: Auditor System
 * Extension for auditing system in zero system
 * This sub system often happened before each Worker component.
 * It's configured in vertx-tp.yml file and will be triggered before
 * any worker method invoking.
 */
public interface PlugAuditor {
    /*
     * DataRegion bind for plug
     */
    default PlugAuditor bind(final JsonObject config) {
        return this;
    }

    /*
     * The object envelop should be modified in current method,
     * There is no default implementation in zero system.
     */
    Future<Envelop> audit(RoutingContext context, Envelop envelop);
}
