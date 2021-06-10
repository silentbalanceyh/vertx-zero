package io.vertx.up.commune.compare;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Diff Container
 *
 * ### 1. Intro
 *
 * Store definition of each model for diff ( comparing workflow )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Deprecated
public class Vary implements Serializable {
    /**
     * Attribute Stored
     * name = type
     *
     * 1. name is the attribute name of model ( `M_ATTRIBUTE` ).
     * 2. type is the java type that could be reflect.
     */
    private transient final ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();

    /**
     * Subset Attribute Stored
     * name = Set
     *
     * 1. name is the attribute name of model ( `M_ATTRIBUTE` ).
     * 2. The data type is fixed: JsonObject / JsonArray.
     * 3. The `Set` means the attribute names of JsonObject or JsonArray here.
     */
    private transient final ConcurrentMap<String, Set<String>> diffMap = new ConcurrentHashMap<>();

    /**
     * Ignored field that could be set from object.
     *
     * 1. Default ignore set
     * 2. System ignore set that could not be compared.
     * 3. The ignore fields that could be set by developer.
     */
    private transient final Set<String> ignoreSet = new HashSet<>();
    private transient JsonObject oldRecord;
    private transient JsonObject newRecord;

    private Vary(final ConcurrentMap<String, Class<?>> typeMap) {
        if (Objects.nonNull(typeMap)) {
            this.typeMap.putAll(typeMap);
        }
    }

    public static Vary create(final ConcurrentMap<String, Class<?>> typeMap) {
        return new Vary(typeMap);
    }

    public static Vary create(final JsonObject oldRecord, final JsonObject newRecord) {
        return new Vary(new ConcurrentHashMap<>()).data(oldRecord, newRecord);
    }

    public static Vary create(final JsonObject oldRecord, final JsonObject newRecord,
                              final ConcurrentMap<String, Class<?>> typeMap) {
        return new Vary(typeMap).data(oldRecord, newRecord);
    }

    @Fluent
    public Vary data(final JsonObject oldRecord, final JsonObject newRecord) {
        this.oldRecord = oldRecord;
        this.newRecord = newRecord;
        return this;
    }

    @Fluent
    public Vary ignores(final Set<String> ignoreSet) {
        if (Objects.nonNull(ignoreSet)) {
            this.ignoreSet.addAll(ignoreSet);
        }
        return this;
    }

    @Fluent
    public Vary type(final ConcurrentMap<String, Class<?>> typeMap) {
        if (Objects.nonNull(typeMap)) {
            this.typeMap.putAll(typeMap);
        }
        return this;
    }

    @Fluent
    public Vary diff(final ConcurrentMap<String, Set<String>> diffMap) {
        if (Objects.nonNull(diffMap)) {
            this.diffMap.putAll(diffMap);
        }
        return this;
    }

    public Set<String> ignores() {
        return this.ignoreSet;
    }

    public JsonObject current() {
        return this.newRecord;
    }

    public JsonObject original() {
        return this.oldRecord;
    }

    public ConcurrentMap<String, Class<?>> type() {
        return this.typeMap;
    }

    public Class<?> type(final String field) {
        return this.typeMap.getOrDefault(field, null);
    }

    public ConcurrentMap<String, Set<String>> diff() {
        return this.diffMap;
    }

    public Set<String> diff(final String field) {
        return this.diffMap.getOrDefault(field, new HashSet<>());
    }
}
