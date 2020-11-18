package io.vertx.up.commune.element;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Type for data structure
 */
public class Shape implements Serializable {
    /*
     * When complex = true, mapping
     */
    private final transient ConcurrentMap<String, ShapeItem> shapeMap = new ConcurrentHashMap<>();
    private final transient List<Class<?>> typeList = new ArrayList<>();

    private transient boolean complex = Boolean.FALSE;

    private Shape() {
    }

    public static Shape create() {
        return new Shape();
    }

    public Shape add(final String name, final String alias, final List<ShapeItem> children) {
        if (Objects.nonNull(name)) {
            this.complex = true;
            /*
             * Add `JsonArray.class` first
             */
            this.add(name, alias, JsonArray.class);
            /*
             * Here should contains type
             */
            final ShapeItem added = this.shapeMap.getOrDefault(name, null);
            if (Objects.nonNull(added)) {
                /*
                 * Add children
                 */
                added.add(children);
            }
        }
        return this;
    }

    public Shape add(final String name, final String alias, final Class<?> type) {
        if (Objects.nonNull(name)) {

            /* JsonArray */
            final ShapeItem shapeItem = ShapeItem.create(name, alias, type);
            this.shapeMap.put(name, shapeItem);
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
        final ShapeItem item = this.shapeMap.getOrDefault(name, null);
        if (Objects.isNull(item)) {
            return false;
        } else {
            return item.isComplex();
        }
    }

    public int size(final String name) {
        final ShapeItem typeItem = this.shapeMap.getOrDefault(name, null);
        if (Objects.nonNull(typeItem)) {
            return typeItem.children().size();
        } else return Values.ZERO;
    }

    public int size() {
        return this.shapeMap.size();
    }

    public ShapeItem item(final String name) {
        return this.shapeMap.getOrDefault(name, null);
    }

    public ConcurrentMap<String, String> alias() {
        final ConcurrentMap<String, String> alias = new ConcurrentHashMap<>();
        this.shapeMap.forEach((field, item) -> alias.put(field, item.getAlias()));
        return alias;
    }

    public Class<?> type(final String field) {
        final ShapeItem item = this.shapeMap.getOrDefault(field, null);
        return Objects.isNull(item) ? null : item.getType();
    }

    public Class<?> type(final String field, final String childField) {
        final ShapeItem item = this.shapeMap.getOrDefault(field, null);
        if (Objects.isNull(item)) {
            return null;
        } else {
            if (JsonArray.class == item.getType()) {
                return item.getType(childField);
            } else return null;
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
    public Shape analyzed(final JsonArray data) {
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
