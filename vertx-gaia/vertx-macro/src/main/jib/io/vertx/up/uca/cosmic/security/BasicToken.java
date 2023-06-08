package io.vertx.up.uca.cosmic.security;

import io.vertx.up.commune.config.Integration;
import io.vertx.up.util.Ut;

/*
 * This token if for Basic authorization in Http client here
 * It could provide:
 * 1) token value
 * 2) authorization http header value based on Basic
 */
public class BasicToken implements WebToken {
    private final transient String token;

    public BasicToken(final Integration integration) {
        this.token = Ut.encryptBase64(integration.getUsername(), integration.getPassword());
    }

    @Override
    public String token() {
        return this.token;
    }

    @Override
    public String authorization() {
        return "Basic " + this.token;
    }
}
