package io.vertx.up.unity;

import io.vertx.up.commune.config.Integration;

/*
 * LDAP专用，由于LDAP不会使用第三方库，而是直接使用 javax.naming执行处理，所以不放在 Infusion 架构中，直接提供即可API
 */
public class UxLdap {
    private final Integration integration;

    UxLdap(final Integration integration) {
        this.integration = integration;
    }
}
