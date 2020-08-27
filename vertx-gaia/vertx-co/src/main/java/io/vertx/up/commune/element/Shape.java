package io.vertx.up.commune.element;

import io.vertx.up.atom.Kv;
import io.vertx.up.eon.Values;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * 带 Shape 数据类型的导入专用模型
 */
public class Shape implements Serializable {

    private final transient ConcurrentMap<String, ShapeItem> shapeMap =
            new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, String> aliasMap =
            new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, Class<?>> typeMap =
            new ConcurrentHashMap<>();
    private transient boolean complex = Boolean.FALSE;

    private Shape() {
    }

    private Shape(final ConcurrentMap<String, String> aliasMap,
                  final ConcurrentMap<String, Class<?>> typeMap) {
        if (Objects.nonNull(aliasMap)) {
            this.aliasMap.putAll(aliasMap);
        }
        if (Objects.nonNull(typeMap)) {
            this.typeMap.putAll(typeMap);
        }
        /*
         * For-each
         * 1) name
         * 2) alias
         * 3) type
         */
        aliasMap.forEach((field, alias) -> {
            final Class<?> type = typeMap.get(field);
            final ShapeItem item = ShapeItem.create(field, alias, type);
            this.shapeMap.put(field, item);
        });
    }

    public static Shape create() {
        return new Shape();
    }

    public static Shape create(final ConcurrentMap<String, String> aliasMap,
                               final ConcurrentMap<String, Class<?>> typeMap) {
        final ConcurrentMap<String, String> inputAlias = Objects.isNull(aliasMap)
                ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(aliasMap);
        final ConcurrentMap<String, Class<?>> inputType = Objects.isNull(typeMap)
                ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(typeMap);
        return new Shape(inputAlias, inputType);
    }

    public Shape add(final String name, final List<Kv<String, String>> children) {
        final ShapeItem typeItem = this.shapeMap.getOrDefault(name, null);
        if (Objects.nonNull(typeItem)) {
            typeItem.add(children);
            this.complex = true;
        }
        return this;
    }

    public Shape add(final String name, final String alias, final Class<?> type) {
        if (Objects.nonNull(name)) {
            this.aliasMap.put(name, alias);
            this.typeMap.put(name, type);
            this.shapeMap.put(name, ShapeItem.create(name, alias, type));
        }
        return this;
    }

    public void clear() {
        this.aliasMap.clear();
        this.typeMap.clear();
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

    public int sizeChildren(final String name) {
        final ShapeItem typeItem = this.shapeMap.getOrDefault(name, null);
        if (Objects.nonNull(typeItem)) {
            return typeItem.children().size();
        } else return Values.ZERO;
    }

    public ShapeItem child(final String name) {
        return this.shapeMap.getOrDefault(name, null);
    }

    public ConcurrentMap<String, String> alias() {
        return this.aliasMap;
    }

    public ConcurrentMap<String, Class<?>> type() {
        return this.typeMap;
    }
}
