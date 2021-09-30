package io.vertx.up.eon.em;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum AuthType {
    MIXTURE,    // OAuth2 + JWT mode
    JWT,        // JWT ( Standard )
    OAUTH2,     // OAuth2 ( Standard )
}
