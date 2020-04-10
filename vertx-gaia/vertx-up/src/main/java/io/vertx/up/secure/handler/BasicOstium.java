package io.vertx.up.secure.handler;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.handler.AuthHandler;

/**
 * HTTP Basic Authentication for Zero System
 */
@VertxGen
public interface BasicOstium extends AuthHandler {
    /**
     * Default realm to use
     */
    String DEFAULT_REALM = "zero-micro";

    /**
     * Create a basic auth handler
     *
     * @param authProvider the auth provider to use
     * @return the auth handler
     */
    static AuthHandler create(final AuthProvider authProvider) {
        return new BasicPhylum(authProvider, DEFAULT_REALM);
    }

    /**
     * Create a basic auth handler, specifying realm
     *
     * @param authProvider the auth service to use
     * @param realm        the realm to use
     * @return the auth handler
     */
    static AuthHandler create(final AuthProvider authProvider, final String realm) {
        return new BasicPhylum(authProvider, realm);
    }
}
