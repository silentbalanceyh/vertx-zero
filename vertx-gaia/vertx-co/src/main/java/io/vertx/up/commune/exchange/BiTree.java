package io.vertx.up.commune.exchange;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.MappingMode;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * [Data Structure]
 * 1) mapping stored `from -> to`
 * 2) revert stored `to -> from`
 * This data structure will store two mappings between configuration file here.
 * It could be used in some places to process ( ----> / <---- )
 *
 * Two mapping categories:
 * A) Single Mapping: String = String
 * B) Multi Mapping: String = JsonObject
 */
public class BiTree implements Serializable {

    /*
     * Root ( Single )
     * String = String
     * Map ( Multi )
     * String = JsonObject
     */
    private final transient BiMapping root = new BiMapping();
    private final transient ConcurrentMap<String, BiMapping> mapping =
        new ConcurrentHashMap<>();
    /*
     * Configured `MappingMode` and `Class<?>`
     */
    private transient MappingMode mode = MappingMode.NONE;
    private transient Class<?> component;

    public BiTree() {
    }

    public BiTree(final JsonObject input) {
        this.init(input);
    }

    public BiTree init(final JsonObject input) {
        if (Ut.notNil(input)) {
            /*
             * Mix data structure for
             * {
             *     "String": {},
             *     "String": "String",
             *     "String": {}
             * }
             */
            this.root.init(input);
            /*
             * Content mapping `Map` here
             */
            input.fieldNames().stream()
                /* Only stored JsonObject value here */
                .filter(field -> input.getValue(field) instanceof JsonObject)
                .forEach(field -> {
                    final JsonObject fieldValue = input.getJsonObject(field);
                    /* Init here */
                    if (Ut.notNil(fieldValue)) {
                        /* Json mapping here */
                        final BiMapping item = new BiMapping(fieldValue);
                        this.mapping.put(field, item);
                    }
                });
        }
        return this;
    }

    /*
     * 1) MappingMode
     * 2) Class<?>
     * 3) DualMapping ( Bind Life Cycle )
     * 4) valid() -> boolean Check whether the mapping is enabled.
     */
    public MappingMode getMode() {
        return this.mode;
    }

    public Class<?> getComponent() {
        return this.component;
    }

    public BiTree bind(final MappingMode mode) {
        this.mode = mode;
        return this;
    }

    public BiTree bind(final Class<?> component) {
        this.component = component;
        return this;
    }

    public boolean valid() {
        return MappingMode.NONE != this.mode;
    }

    // -------------  Get by identifier ----------------------------
    /*
     * Child get here
     */
    public BiMapping child(final String key) {
        final BiMapping selected = this.mapping.get(key);
        if (Objects.isNull(selected) || selected.isEmpty()) {
            return this.root;
        } else {
            return selected;
        }
    }

    public BiMapping child() {
        return this.root;
    }

    // -------------  Root Method here for split instead -----------
    /*
     * from -> to, to values to String[]
     */
    public String[] to() {
        return this.root.to();
    }

    public String[] from() {
        return this.root.from();
    }

    public Set<String> to(final JsonArray keys) {
        return this.root.to(keys);
    }

    public Set<String> from(final JsonArray keys) {
        return this.root.from(keys);
    }

    /*
     * Get value by from key, get to value
     */
    public String to(final String from) {
        return this.root.to(from);
    }

    public Class<?> toType(final String from) {
        return this.root.toType(from);
    }

    public boolean fromKey(final String key) {
        return this.root.fromKey(key);
    }

    public String from(final String to) {
        return this.root.from(to);
    }

    public Class<?> fromType(final String to) {
        return this.root.fromType(to);
    }

    public boolean toKey(final String key) {
        return this.root.toKey(key);
    }

    /*
     * from -> to, from keys
     */
    public Set<String> keys() {
        return this.root.keys();
    }

    public Set<String> values() {
        return this.root.values();
    }

    @Override
    public String toString() {
        return "DualMapping{" +
            "root=" + this.root +
            ", mapping=" + this.mapping +
            ", mode=" + this.mode +
            ", component=" + this.component +
            '}';
    }
}
