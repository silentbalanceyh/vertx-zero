package io.vertx.up.commune.element;

import io.vertx.core.json.JsonArray;
import io.vertx.up.eon.Values;

import java.io.Serializable;
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
}
