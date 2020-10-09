package io.vertx.tp.plugin.cache.hit;

import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.TreeMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
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
    public String dataUnique() {
        final TreeMap<String, String> treeMap = new TreeMap<>();
        /*
         * Primary Key
         */
        final StringBuilder key = new StringBuilder();
        this.primarySet.forEach(key::append);
        treeMap.put(key.toString(), this.id);
        /*
         * Single Record
         */
        final L1Algorithm algorithm = Ut.singleton(AlgorithmRecord.class);
        return algorithm.dataUnique(this.typeName(), treeMap);
    }

    @Override
    public boolean isList() {
        return Boolean.FALSE;
    }

    @Override
    public boolean isRef() {
        return Boolean.FALSE;
    }
}
