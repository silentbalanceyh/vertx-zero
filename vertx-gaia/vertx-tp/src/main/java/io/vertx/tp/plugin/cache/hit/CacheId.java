package io.vertx.tp.plugin.cache.hit;

import io.vertx.tp.plugin.cache.l1.AlgorithmRecord;
import io.vertx.tp.plugin.cache.l1.L1Algorithm;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.TreeMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CacheId implements CacheKey {
    private transient String id;

    /*
     * VertxDao processing, class mapped to L1Key
     */
    public CacheId(final Object id) {
        if (Objects.nonNull(id)) {
            this.id = id.toString();
        }
    }

    @Override
    public String unique(final CacheMeta meta) {
        final TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put(meta.primaryString(), this.id);
        /*
         * Single Record
         */
        final L1Algorithm algorithm = Ut.singleton(AlgorithmRecord.class);
        return algorithm.dataUnique(meta.typeName(), treeMap);
    }

    @Override
    public boolean primary() {
        return true;
    }
}
