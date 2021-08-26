package io.vertx.up.uca.rs.secure;

import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.up.atom.secure.Cliff;

/**
 * Security Module for dispatcher,
 * Build standard AuthHandler for different workflow.
 */
public interface Bolt {

    static Bolt get() {
        return ShuntBolt.create();
    }

    AuthHandler mount(final Vertx vertx,
                      final Cliff cliff);
}
