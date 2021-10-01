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
     * Vert.x native standard
     */
    MONGO("mongo"),
    SQL("sql"),
    JWT("jwt"),
    OAUTH2("oauth2"),
    BASIC("basic"),
    DIGEST("digest"),
    WEB_N("web");

    private static final ConcurrentMap<String, AuthWall> TYPE_MAP = new ConcurrentHashMap<>();

    static {
        Arrays.stream(AuthWall.values()).forEach(wall -> TYPE_MAP.put(wall.key(), wall));
    }

    private transient final String configKey;

    AuthWall(final String configKey) {
        this.configKey = configKey;
    }

    public static AuthWall from(final String configKey) {
        return TYPE_MAP.get(configKey);
    }

    public static Set<String> keys() {
        return TYPE_MAP.keySet();
    }

    public String key() {
        return this.configKey;
    }
}
