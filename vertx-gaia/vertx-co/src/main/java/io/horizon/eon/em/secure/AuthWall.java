package io.horizon.eon.em.secure;

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
     * Vert.x native standard, all below values came from vertx official guide
     *
     * Here are some tpl in `provider/handler` instead of nothing
     */
    BASIC("basic"),         // Any provider will be ok
    REDIRECT("redirect"),   // Any provider will be ok
    JWT("jwt"),
    OAUTH2("oauth2"),
    DIGEST("digest"),
    WEB("web"),
    /*
     * When you provide this value, it means that you will use Zero Extension kind
     * type of wall instead of Vert.x Native
     *
     * If the wall type value is not in these value collections, you must provide
     * defined key that could be read configuration data from `vertx-secure.yml` etc.
     */
    EXTENSION("extension");

    private static final ConcurrentMap<String, AuthWall> TYPE_MAP = new ConcurrentHashMap<>();

    static {
        Arrays.stream(AuthWall.values()).forEach(wall -> TYPE_MAP.put(wall.key(), wall));
    }

    private transient final String configKey;

    AuthWall(final String configKey) {
        this.configKey = configKey;
    }

    public static AuthWall from(final String configKey) {
        return TYPE_MAP.getOrDefault(configKey, null);
    }

    public static Set<String> keys() {
        return TYPE_MAP.keySet();
    }

    public String key() {
        return this.configKey;
    }
}
