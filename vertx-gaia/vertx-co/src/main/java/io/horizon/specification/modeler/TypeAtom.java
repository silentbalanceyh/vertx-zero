package io.horizon.specification.modeler;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Type for data structure
 */
public class TypeAtom implements Serializable {
    /*
     * When complex = true, mapping
     */
    private final ConcurrentMap<String, TypeField> shapeMap = new ConcurrentHashMap<>();
    private final List<Class<?>> typeList = new ArrayList<>();

    private boolean complex = Boolean.FALSE;

    private TypeAtom() {
    }

    public static TypeAtom create() {
        return new TypeAtom();
    }

    // JsonArray
    public TypeAtom add(final String name, final String alias, final List<TypeField> children) {
        return this.addInternal(name, alias, children);
    }

    // JsonObject
    public TypeAtom add(final String name, final String alias, final Set<TypeField> children) {
        return this.addInternal(name, alias, children);
    }

    // Elementary
    public TypeAtom add(final String name, final String alias, final Class<?> type) {
        if (Ut.notNil(name)) {
            /* JsonArray */
            final TypeField typeItem = TypeField.create(name, alias, type);
            this.shapeMap.put(name, typeItem);
        }
        return this;
    }

    public TypeAtom add(final TypeField field) {
        final String name = field.name();
        this.shapeMap.put(name, field);
        if (field.isComplex()) {
            this.complex = true;
        }
        return this;
    }

    private TypeAtom addInternal(final String name, final String alias, final Collection<TypeField> children) {
        if (Ut.notNil(name)) {
            this.complex = true;
            // Add field type first
            final Class<?> fieldType = children instanceof List ? JsonArray.class : JsonObject.class;
            this.add(name, alias, fieldType);
            // Here should contains subtypes
            final TypeField child = this.shapeMap.getOrDefault(name, null);
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
        final TypeField item = this.shapeMap.getOrDefault(name, null);
        if (Objects.isNull(item)) {
            return false;
        } else {
            return item.isComplex();
        }
    }

    public int size(final String name) {
        final TypeField typeItem = this.shapeMap.getOrDefault(name, null);
        if (Objects.nonNull(typeItem)) {
            return typeItem.children().size();
        } else {
            return Values.ZERO;
        }
    }

    public int size() {
        return this.shapeMap.size();
    }

    public TypeField item(final String name) {
        return this.shapeMap.getOrDefault(name, null);
    }

    public ConcurrentMap<String, String> alias() {
        final ConcurrentMap<String, String> alias = new ConcurrentHashMap<>();
        this.shapeMap.forEach((field, item) -> alias.put(field, item.alias()));
        return alias;
    }

    public Class<?> type(final String field) {
        final TypeField item = this.shapeMap.getOrDefault(field, null);
        return Objects.isNull(item) ? null : item.type();
    }

    public Class<?> type(final String field, final String childField) {
        final TypeField item = this.shapeMap.getOrDefault(field, null);
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
    public TypeAtom analyzed(final JsonArray data) {
        this.typeList.clear();
        if (this.complex) {
            // index = 2
            final JsonArray fields = data.getJsonArray(Values.TWO);
            // index = 3
            final JsonArray secondary = data.getJsonArray(Values.THREE);

            if (Ut.notNil(fields) && Ut.notNil(secondary)) {
                // parent processing
                final ConcurrentMap<Integer, String> parentMap = new ConcurrentHashMap<>();

                /*
                 * Iterator on first
                 * Calculate region for `null`
                 * Should not be `null` value to avoid: java.lang.NullPointerException
                 */
                String found = Strings.EMPTY;
                final int length = fields.size();
                for (int idx = 0; idx < length; idx++) {
                    final Object item = fields.getValue(idx);
                    final String field = this.typeField(item);
                    if (Ut.notNil(field)) {
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
                    if (Ut.notNil(field) && Ut.notNil(parent)) {
                        /*
                         * Find type based on parent/child both
                         */
                        this.typeList.add(this.type(parent, field));
                    }
                }
            }
        } else {
            // index = 2
            final JsonArray fields = data.getJsonArray(Values.ONE);
            if (Ut.notNil(fields)) {
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
