package io.vertx.up.commune.element;

import io.vertx.core.json.JsonArray;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JTypeItem implements Serializable {
    private final transient List<JTypeItem> children = new ArrayList<>();
    private final transient ConcurrentMap<String, JTypeItem> childMap = new ConcurrentHashMap<>();
    private final transient String name;
    private final transient String alias;
    private final transient Class<?> type;

    private JTypeItem(final String name, final String alias) {
        this(name, alias, String.class);
    }

    private JTypeItem(final String name, final String alias, final Class<?> type) {
        this.name = name;
        this.alias = alias;
        this.type = Objects.isNull(type) ? String.class : type;
    }

    public static JTypeItem create(final String name, final String alias, final Class<?> type) {
        return new JTypeItem(name, alias, type);
    }

    public static JTypeItem create(final String name, final String alias) {
        return new JTypeItem(name, alias);
    }

    void add(final List<JTypeItem> children) {
        if (Objects.nonNull(children) && JsonArray.class == this.type) {
            children.forEach(item -> {
                /*
                 * Array for children
                 * Map for children
                 */
                this.children.add(item);
                this.childMap.put(item.getName(), item);
            });
        }
    }

    public boolean isComplex() {
        return !this.children.isEmpty();
    }

    public String getName() {
        return this.name;
    }

    public String getName(final String field) {
        return this.children(field, JTypeItem::getName);
    }

    public String getAlias() {
        return this.alias;
    }

    public String getAlias(final String field) {
        return this.children(field, JTypeItem::getAlias);
    }

    public Class<?> getType() {
        return this.type;
    }

    public Class<?> getType(final String field) {
        return this.children(field, JTypeItem::getType);
    }

    public List<JTypeItem> children() {
        return this.children;
    }

    private <T> T children(final String field, final Function<JTypeItem, T> function) {
        if (Ut.isNil(field)) {
            return null;
        } else {
            final JTypeItem item = this.childMap.getOrDefault(field, null);
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
