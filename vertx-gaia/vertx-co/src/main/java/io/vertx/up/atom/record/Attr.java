package io.vertx.up.atom.record;

import java.io.Serializable;

/**
 * 带类型的专用数据集
 * name = alias
 * name = Class
 * name = Object
 * 属性集
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Attr implements Serializable {
    private final String name;
    private String alias;
    private Class<?> type;
    private Object value;

    public Attr(final String name) {
        this.name = name;
        this.alias = name;
        this.type = String.class;
        this.value = null;
    }

    public Attr bind(final String alias) {
        this.alias = alias;
        return this;
    }

    public Attr bind(final Class<?> type) {
        this.type = type;
        return this;
    }

    public Attr value(final Object value) {
        this.value = value;
        return this;
    }

    public String name() {
        return this.name;
    }

    public Class<?> type() {
        return this.type;
    }

    public String alias() {
        return this.alias;
    }

    @SuppressWarnings("unchecked")
    public <T> T value() {
        return (T) this.value;
    }
}
