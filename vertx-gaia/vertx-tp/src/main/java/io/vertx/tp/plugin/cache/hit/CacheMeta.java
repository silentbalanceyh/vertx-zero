package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.TreeSet;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CacheMeta implements Serializable {
    /*
     * UniqueKey for key calculation
     */
    private final TreeSet<String> primaryKey = new TreeSet<>();
    private final JsonObject condition = new JsonObject();

    private final transient Class<?> clazz;
    private transient boolean baseMeta;

    public CacheMeta(final Class<?> clazz) {
        this.clazz = clazz;
        this.baseMeta = true;
    }

    public CacheMeta primaryKey(final TreeSet<String> primaryKey) {
        this.condition.clear();
        this.primaryKey.clear();
        this.primaryKey.addAll(primaryKey);
        return this;
    }

    public CacheMeta conditionKey(final JsonObject condition) {
        if (Ut.notNil(condition)) {
            this.condition.clear();
            this.condition.mergeIn(condition, true);
        }
        return this;
    }

    /*
     * Copy of current base Meta here
     */
    public CacheMeta createCopy() {
        final CacheMeta nonBase = new CacheMeta(this.clazz);
        nonBase.baseMeta = false;
        return nonBase.primaryKey(this.primaryKey);
    }

    public String typeName() {
        return Objects.nonNull(this.clazz) ? this.clazz.getName() : null;
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> type() {
        return Objects.nonNull(this.clazz) ? (Class<T>) this.clazz : null;
    }

    public JsonObject metadata() {
        final JsonObject merged = new JsonObject();
        merged.put("type", this.typeName());
        if (this.primaryKey.isEmpty() && this.condition.isEmpty()) {
            return null;
        } else {
            merged.put("key", Ut.toJArray(this.primaryKey));
            if (this.primaryKey.isEmpty()) {
                merged.put("primary", Boolean.FALSE);
                merged.put("condition", this.condition);
            } else {
                merged.put("primary", Boolean.TRUE);
            }
        }
        return merged;
    }

    public String primaryString() {
        final StringBuilder key = new StringBuilder();
        this.primaryKey.forEach(key::append);
        return key.toString();
    }
}
