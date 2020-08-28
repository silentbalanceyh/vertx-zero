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
    /*
     * Index storage for index mapping
     * 1) Flat index store
     * 2) Complex index store
     * 0,1,2,3,4,5,6 = JsonArray,11,12,13 = JsonArray
     *             6,7,8,9,10          13,14,15,16
     *
     */
    private final ConcurrentMap<Integer, ShapeItem> indexMap = new ConcurrentHashMap<>();

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
                /*
                 * index = Class<?>
                 */
                children.forEach(this::indexPos);
            }
        }
        return this;
    }

    public Shape add(final String name, final String alias, final Class<?> type) {
        if (Objects.nonNull(name) && JsonArray.class != type) {

            /* JsonArray */
            final ShapeItem shapeItem = ShapeItem.create(name, alias, type);
            this.shapeMap.put(name, shapeItem);

            /* Index for simple */
            this.indexPos(shapeItem);
        }
        return this;
    }

    /*
     * Re calculate index map to correct structure
     */
    private void indexPos(final ShapeItem item) {
        if (JsonArray.class != item.getType()) {
            /*
             * index = Class<?>
             */
            final int key = this.indexMap.size();
            this.indexMap.put(key, item);
        }
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

    public ShapeItem item(final int index) {
        return this.indexMap.getOrDefault(index, null);
    }

    public ConcurrentMap<String, String> alias() {
        final ConcurrentMap<String, String> alias = new ConcurrentHashMap<>();
        this.shapeMap.forEach((field, item) -> alias.put(field, item.getAlias()));
        return alias;
    }
}
