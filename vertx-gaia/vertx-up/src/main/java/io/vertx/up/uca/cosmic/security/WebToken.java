package io.vertx.up.uca.cosmic.security;

/*
 * Token core interface
 */
public interface WebToken {
    /*
     * Read token string
     */
    String token();

    /*
     * Generate `Authorization` header value
     */
    String authorization();
}
