package io.vertx.mod.jet.uca.aim;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.jet.atom.JtUri;

/*
 * JtUri generate useful handler here
 */
public interface JtAim {
    /*
     * Main workflow here.
     */
    Handler<RoutingContext> attack(JtUri uri);
}
