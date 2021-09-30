package io.vertx.up.eon.em;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Wall type for security
 */
public enum AuthWall {
    /*
     * Here are mode that defined by zero framework
     * Config key here and related to `rules`
     */
    MONGO("mongo"),
    SQL("sql"),
    JWT("jwt"),
    OAUTH2("oauth2"),
    BASIC("basic"),
    DIGEST("digest"),
    WEB_N("web"),
    /*
     * User-defined
     */
    CUSTOM("custom");

    private static final ConcurrentMap<String, AuthWall> TYPE_MAP = new ConcurrentHashMap<>();

    static {
        Arrays.stream(AuthWall.values()).forEach(wall -> TYPE_MAP.put(wall.key(), wall));
    }

    private transient final String configKey;

    AuthWall(final String configKey) {
        this.configKey = configKey;
    }

    public static AuthWall from(final String literal) {
        return TYPE_MAP.get(literal);
    }

    public static Set<String> keys() {
        return TYPE_MAP.keySet();
    }

    public String key() {
        return this.configKey;
    }

    public boolean match(final String literal) {
        return this.configKey.equals(literal);
    }
}
