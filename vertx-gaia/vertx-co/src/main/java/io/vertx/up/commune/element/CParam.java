package io.vertx.up.commune.element;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Diff for two record comparing
 */
public class CParam implements Serializable {
    private transient final ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();
    private transient final ConcurrentMap<String, Set<String>> diffMap = new ConcurrentHashMap<>();
    private transient final Set<String> ignoreSet = new HashSet<>();
    private transient JsonObject oldRecord;
    private transient JsonObject newRecord;

    private CParam(final ConcurrentMap<String, Class<?>> typeMap) {
        if (Objects.nonNull(typeMap)) {
            this.typeMap.putAll(typeMap);
        }
    }

    public static CParam create(final ConcurrentMap<String, Class<?>> typeMap) {
        return new CParam(typeMap);
    }

    public static CParam create(final JsonObject oldRecord, final JsonObject newRecord) {
        return new CParam(new ConcurrentHashMap<>()).data(oldRecord, newRecord);
    }

    public static CParam create(final JsonObject oldRecord, final JsonObject newRecord,
                                final ConcurrentMap<String, Class<?>> typeMap) {
        return new CParam(typeMap).data(oldRecord, newRecord);
    }

    @Fluent
    public CParam data(final JsonObject oldRecord, final JsonObject newRecord) {
        this.oldRecord = oldRecord;
        this.newRecord = newRecord;
        return this;
    }

    @Fluent
    public CParam ignores(final Set<String> ignoreSet) {
        if (Objects.nonNull(ignoreSet)) {
            this.ignoreSet.addAll(ignoreSet);
        }
        return this;
    }

    @Fluent
    public CParam type(final ConcurrentMap<String, Class<?>> typeMap) {
        if (Objects.nonNull(typeMap)) {
            this.typeMap.putAll(typeMap);
        }
        return this;
    }

    @Fluent
    public CParam diff(final ConcurrentMap<String, Set<String>> diffMap) {
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
