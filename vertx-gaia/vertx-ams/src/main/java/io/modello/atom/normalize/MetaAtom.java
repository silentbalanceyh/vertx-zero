package io.modello.atom.normalize;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Type for data structure
 */
public class MetaAtom implements Serializable {
    /*
     * When complex = true, mapping
     */
    private final ConcurrentMap<String, MetaField> shapeMap = new ConcurrentHashMap<>();
    private final List<Class<?>> typeList = new ArrayList<>();

    private boolean complex = Boolean.FALSE;

    private MetaAtom() {
    }

    public static MetaAtom create() {
        return new MetaAtom();
    }

    // JsonArray
    public MetaAtom add(final String name, final String alias, final List<MetaField> children) {
        return this.addInternal(name, alias, children);
    }

    // JsonObject
    public MetaAtom add(final String name, final String alias, final Set<MetaField> children) {
        return this.addInternal(name, alias, children);
    }

    // Elementary
    public MetaAtom add(final String name, final String alias, final Class<?> type) {
        if (HUt.isNotNil(name)) {
            /* JsonArray */
            final MetaField typeItem = MetaField.create(name, alias, type);
            this.shapeMap.put(name, typeItem);
        }
        return this;
    }

    public MetaAtom add(final MetaField field) {
        final String name = field.name();
        this.shapeMap.put(name, field);
        if (field.isComplex()) {
            this.complex = true;
        }
        return this;
    }

    private MetaAtom addInternal(final String name, final String alias, final Collection<MetaField> children) {
        if (HUt.isNotNil(name)) {
            this.complex = true;
            // Add field type first
            final Class<?> fieldType = children instanceof List ? JsonArray.class : JsonObject.class;
            this.add(name, alias, fieldType);
            // Here should contains subtypes
            final MetaField child = this.shapeMap.getOrDefault(name, null);
            child.add(children);
        }
        return this;
    }

    public void clear() {
        this.shapeMap.clear();
    }

    public boolean isComplex() {
        return this.complex;
    }

    public boolean isComplex(final String name) {
        final MetaField item = this.shapeMap.getOrDefault(name, null);
        if (Objects.isNull(item)) {
            return false;
        } else {
            return item.isComplex();
        }
    }

    public int size(final String name) {
        final MetaField typeItem = this.shapeMap.getOrDefault(name, null);
        if (Objects.nonNull(typeItem)) {
            return typeItem.children().size();
        } else {
            return VValue.ZERO;
        }
    }

    public int size() {
        return this.shapeMap.size();
    }

    public MetaField item(final String name) {
        return this.shapeMap.getOrDefault(name, null);
    }

    public ConcurrentMap<String, String> alias() {
        final ConcurrentMap<String, String> alias = new ConcurrentHashMap<>();
        this.shapeMap.forEach((field, item) -> alias.put(field, item.alias()));
        return alias;
    }

    public Class<?> type(final String field) {
        final MetaField item = this.shapeMap.getOrDefault(field, null);
        return Objects.isNull(item) ? null : item.type();
    }

    public Class<?> type(final String field, final String childField) {
        final MetaField item = this.shapeMap.getOrDefault(field, null);
        if (Objects.isNull(item)) {
            return null;
        } else {
            if (JsonArray.class == item.type()) {
                return item.type(childField);
            } else {
                return null;
            }
        }
    }

    public List<Class<?>> types() {
        return this.typeList;
    }

    public Class<?> type(final Integer index) {
        if (index < this.typeList.size()) {
            return this.typeList.get(index);
        } else {
            return null;
        }
    }

    // --------------------- Move Types internal and stored for future usage --------------------------
    /*
     * Build data type here
     * 1) Simple
     * (index = 2) = 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15...
     * 2) Complex
     * (index = 3) = 0,1,2,3,4,5 = JsonArray,n,n,8,9,10 = JsonArray,n,n,n,14,15,...
     * (index = 4) = n,n,n,n,n,5,6,7,n,n,10,11,12,13,14,15,...
     *
     * Flatted for complex type here
     */
    public MetaAtom analyzed(final JsonArray data) {
        this.typeList.clear();
        if (this.complex) {
            // index = 2
            final JsonArray fields = data.getJsonArray(VValue.TWO);
            // index = 3
            final JsonArray secondary = data.getJsonArray(VValue.THREE);

            if (HUt.isNotNil(fields) && HUt.isNotNil(secondary)) {
                // parent processing
                final ConcurrentMap<Integer, String> parentMap = new ConcurrentHashMap<>();

                /*
                 * Iterator on first
                 * Calculate region for `null`
                 * Should not be `null` value to avoid: java.lang.NullPointerException
                 */
                String found = VString.EMPTY;
                final int length = fields.size();
                for (int idx = 0; idx < length; idx++) {
                    final Object item = fields.getValue(idx);
                    final String field = this.typeField(item);
                    if (HUt.isNotNil(field)) {
                        final Class<?> type = this.type(field);
                        if (JsonArray.class != type) {
                            this.typeList.add(type);
                        } else {
                            // Start index
                            found = field;
                            parentMap.put(idx, found);
                        }
                    } else {
                        // End index
                        parentMap.put(idx, found);
                    }
                }
                /*
                 * Secondary must be children to extract
                 */
                final int lengthChild = secondary.size();
                for (int idx = 0; idx < lengthChild; idx++) {
                    final Object item = secondary.getValue(idx);
                    final String field = this.typeField(item);
                    final String parent = parentMap.getOrDefault(idx, null);
                    if (HUt.isNotNil(field) && HUt.isNotNil(parent)) {
                        /*
                         * Find type based on parent/child both
                         */
                        this.typeList.add(this.type(parent, field));
                    }
                }
            }
        } else {
            // index = 2
            final JsonArray fields = data.getJsonArray(VValue.ONE);
            if (HUt.isNotNil(fields)) {
                fields.forEach(item -> {
                    final String field = this.typeField(item);
                    this.typeList.add(this.type(field));
                });
            }
        }
        return this;
    }

    private String typeField(final Object input) {
        if (Objects.isNull(input)) {
            return null;
        } else {
            if (input instanceof JsonObject) {
                return ((JsonObject) input).getString("value");
            } else {
                return (String) input;
            }
        }
    }
}
