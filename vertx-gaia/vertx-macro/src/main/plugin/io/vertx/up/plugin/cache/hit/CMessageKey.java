package io.vertx.up.plugin.cache.hit;

import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.TreeMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CMessageKey extends AbstractCMessage {
    private transient String id;

    /*
     * VertxDao processing, class mapped to L1Key
     */
    public CMessageKey(final Object id, final Class<?> type) {
        super(type);
        if (Objects.nonNull(id)) {
            this.id = id.toString();
        }
    }

    @Override
    public String dataKey() {
        final TreeMap<String, String> treeMap = this.dataMap(this.id);
        final L1Algorithm algorithm = Ut.singleton(AlgorithmRecord.class);
        return algorithm.dataKey(this.typeName(), treeMap);
    }

    @Override
    public boolean isList() {
        return Boolean.FALSE;
    }

    @Override
    public boolean isRef() {
        return Boolean.FALSE;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final CMessageKey that = (CMessageKey) o;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
