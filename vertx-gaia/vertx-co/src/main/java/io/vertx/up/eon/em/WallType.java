package io.vertx.up.eon.em;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Wall type for security
 */
public enum WallType {
    // Mongo Type
    MONGO("mongo"),
    JWT("jwt"),
    // Defined
    CUSTOM("custom");

    private static final ConcurrentMap<String, WallType> TYPE_MAP
            = new ConcurrentHashMap<String, WallType>() {
        {
            this.put("mongo", MONGO);
            this.put("jwt", JWT);
            this.put("custom", CUSTOM);
        }
    };

    private transient final String literal;

    WallType(final String literal) {
        this.literal = literal;
    }

    public String key() {
        return this.literal;
    }

    public boolean match(final String literal) {
        return null != literal && this.literal.equals(literal);
    }

    public static WallType from(final String literal) {
        return TYPE_MAP.get(literal);
    }
}
