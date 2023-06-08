package io.vertx.up.extension;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.commune.Envelop;

/**
 * 「Extension」
 * Name: Data Region
 * Data Region when you want to do some modification on Envelop,
 * There are two position to process region modification:
 * 1) Before Envelop request building / sending.
 * 2) After Response replying from agent.
 * This plugin exist in agent only and could not be used in worker, standard
 */
public interface PlugRegion {
    /*
     * DataRegion bind for plug
     */
    PlugRegion bind(JsonObject config);

    /*
     * Request processing
     */
    Future<Envelop> before(RoutingContext context, Envelop request);

    /*
     * Response processing
     */
    Future<Envelop> after(RoutingContext context, Envelop response);
}
