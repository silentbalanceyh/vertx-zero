package io.vertx.up.atom.record;

import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AttrSet implements Serializable {

    private final ConcurrentMap<String, Attr> attrMap = new ConcurrentHashMap<>();

    private AttrSet() {
    }

    public static AttrSet of(final ConcurrentMap<String, Attr> data) {
        final AttrSet set = new AttrSet();
        set.bind(data);
        return set;
    }

    public static AttrSet of() {
        return of(new ConcurrentHashMap<>());
    }

    public AttrSet bind(final ConcurrentMap<String, Attr> data) {
        if (Objects.nonNull(data)) {
            this.attrMap.putAll(data);
        }
        return this;
    }

    public AttrSet save(final String name, final String alias) {
        return this.saveWith(name, alias, String.class, null);
    }

    public AttrSet save(final String name, final String alias, final Class<?> type) {
        return this.saveWith(name, alias, type, null);
    }

    public AttrSet saveWith(final String name, final String alias,
                            final Object value) {
        return this.saveWith(name, alias, String.class, value);
    }

    public AttrSet saveWith(final String name, final String alias,
                            final Class<?> type, final Object value) {
        final Attr attr;
        if (this.attrMap.containsKey(name)) {
            attr = this.attrMap.get(name);
        } else {
            attr = new Attr(name);
        }
        attr.bind(Objects.isNull(type) ? String.class : type);
        if (Ut.notNil(alias)) {
            attr.bind(alias);
        }
        attr.value(value);
        this.attrMap.put(name, attr);
        return this;
    }

    public AttrSet remove(final String name) {
        this.attrMap.remove(name);
        return this;
    }

    public Attr attribute(final String name) {
        return this.attrMap.getOrDefault(name, null);
    }

    public Set<String> names() {
        return this.attrMap.keySet();
    }
}
