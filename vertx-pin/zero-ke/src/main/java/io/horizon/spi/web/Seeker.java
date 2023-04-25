package io.horizon.spi.web;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;

/*
 * View seeking interface
 * Fetch default view that will be impact and then the system will get
 * Resource Id here.
 */
public interface Seeker {

    String ARG0 = KName.URI;
    String ARG1 = KName.METHOD;
    String ARG2 = KName.SIGMA;

    Seeker on(UxJooq jooq);

    /*
     * Seeker resource by params and return to unique resourceId
     * The target resource should be impact by input params
     */
    Future<JsonObject> fetchImpact(JsonObject params);
}
