package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.json.JsonArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class HMeta implements Serializable {
    private static final ConcurrentMap<Class<?>, HMeta> META_POOL = new ConcurrentHashMap<>();
    /*
     * UniqueKey for key calculation
     */
    private final List<TreeSet<String>> keys = new ArrayList<>();

    private final transient Class<?> clazz;

    public HMeta(final Class<?> clazz) {
        this.clazz = clazz;
    }

    public HMeta keys(final List<TreeSet<String>> keys) {
        this.keys.addAll(keys);
        return this;
    }

    public String typeName() {
        return Objects.nonNull(this.clazz) ? this.clazz.getName() : null;
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> type() {
        return Objects.nonNull(this.clazz) ? (Class<T>) this.clazz : null;
    }

    public JsonArray keys() {
        final JsonArray keys = new JsonArray();
        this.keys.forEach(set -> {
            final JsonArray each = new JsonArray();
            set.forEach(each::add);
            keys.add(each);
        });
        return keys;
    }
}
