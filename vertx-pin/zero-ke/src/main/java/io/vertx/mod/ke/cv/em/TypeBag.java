package io.vertx.mod.ke.cv.em;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum TypeBag {
    KERNEL("Z-KERNEL"),             // Z-KERNEL
    COMMERCE("Z-COMMERCE"),         // Z-COMMERCE
    FOUNDATION("Z-FOUNDATION"),     // Z-FOUNDATION
    EXTENSION("EXTENSION");         // EXTENSION

    private static final ConcurrentMap<String, TypeBag> TYPE_MAP = new ConcurrentHashMap<>();

    static {
        Arrays.stream(TypeBag.values()).forEach(wall -> TYPE_MAP.put(wall.key(), wall));
    }

    private transient final String value;

    TypeBag(final String value) {
        this.value = value;
    }

    public static TypeBag from(final String configKey) {
        return TYPE_MAP.getOrDefault(configKey, null);
    }

    public static Set<String> keys() {
        return TYPE_MAP.keySet();
    }

    public String key() {
        return this.value;
    }
}
