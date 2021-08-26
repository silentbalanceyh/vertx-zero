package io.vertx.up.uca.rs.secure;

import io.vertx.core.Vertx;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.up.atom.secure.Cliff;

/**
 * Dispatch Bolt to real bolt instance.
 */
class ShuntBolt implements Bolt {

    private static Bolt INSTANCE;

    static Bolt create() {
        if (null == INSTANCE) {
            INSTANCE = new ShuntBolt();
        }
        return INSTANCE;
    }

    /**
     * @param vertx Vertx reference
     * @param cliff Cliff reference
     *
     * @return AuthHandler building
     */
    @Override
    public AuthHandler mount(final Vertx vertx,
                             final Cliff cliff) {
        if (cliff.isDefined()) {
            // User-defined
            // TODO: All should be defined by user.
            return null;
        } else {
            // Native
            final Bolt bolt = NativeBolt.create();
            return bolt.mount(vertx, cliff);
        }
    }
}
