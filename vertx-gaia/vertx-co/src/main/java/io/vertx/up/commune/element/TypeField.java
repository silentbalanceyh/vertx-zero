package io.vertx.up.commune.element;

import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * ## Type Info
 *
 * ### 1. Intro
 *
 * 1. Rule: rule -> diff to get unique condition ( This is only for JsonArray )
 * 2. Fields: fields -> Field information of JsonArray, JsonObject
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TypeField implements Serializable {
    /**
     * field = JTypeItem
     *
     * JsonObject Support Only
     */
    private final transient ConcurrentMap<String, TypeField> childMap = new ConcurrentHashMap<>();
    /**
     * JTypeItem, JTypeItem
     *
     * JsonArray Support only
     */
    private final transient List<TypeField> children = new ArrayList<>();
    /** Current field name */
    private final transient String name;
    /** Current field alias */
    private final transient String alias;
    /** Unique field **/
    private final transient Set<String> unique = new HashSet<>();
    /**
     * Current field type.
     *
     * 1. Elementary
     * 2. JsonArray
     * 3. JsonObject
     */
    private final transient Class<?> type;

    // -------------------- Constructor -----------------------

    private TypeField(final String name, final String alias) {
        this(name, alias, String.class);
    }

    private TypeField(final String name, final String alias, final Class<?> type) {
        this.name = name;
        this.alias = alias;
        this.type = Objects.isNull(type) ? String.class : type;
    }

    public static TypeField create(final String name, final String alias, final Class<?> type) {
        return new TypeField(name, alias, type);
    }

    public static TypeField create(final String name, final String alias) {
        return new TypeField(name, alias);
    }

    public TypeField ruleUnique(final Set<String> diffSet) {
        if (Objects.nonNull(diffSet)) {
            this.unique.clear();
            this.unique.addAll(diffSet);
        }
        return this;
    }

    public Set<String> ruleUnique() {
        return this.unique;
    }

    public void add(final Collection<TypeField> children) {
        if (Objects.nonNull(children)) {
            children.forEach(item -> {
                /*
                 * Array for children
                 * Map for children
                 */
                this.children.add(item);
                this.childMap.put(item.name(), item);
            });
        }
    }

    public boolean isComplex() {
        return !this.children.isEmpty();
    }

    public String name() {
        return this.name;
    }

    public String name(final String field) {
        return this.children(field, TypeField::name);
    }

    public String alias() {
        return this.alias;
    }

    public String alias(final String field) {
        return this.children(field, TypeField::alias);
    }

    public Class<?> type() {
        return this.type;
    }

    public Class<?> type(final String field) {
        return this.children(field, TypeField::type);
    }

    public List<TypeField> children() {
        return this.children;
    }

    private <T> T children(final String field, final Function<TypeField, T> function) {
        if (Ut.isNil(field)) {
            return null;
        } else {
            final TypeField item = this.childMap.getOrDefault(field, null);
            if (Objects.isNull(item)) {
                return null;
            } else {
                return function.apply(item);
            }
        }
    }

    @Override
    public String toString() {
        return "ShapeItem{" +
                "name='" + this.name + '\'' +
                ", alias='" + this.alias + '\'' +
                ", type=" + this.type +
                '}';
    }
}
