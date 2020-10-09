package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.l1.AlgorithmCollection;
import io.vertx.tp.plugin.cache.l1.L1Algorithm;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CacheCond implements CacheKey {
    private final transient JsonObject condition = new JsonObject();

    public CacheCond(final JsonObject condition) {
        if (Objects.nonNull(condition)) {
            this.condition.mergeIn(condition, true);
        }
    }

    public CacheCond(final String field, final Object value) {
        this.condition.put(field, value);
    }

    @Override
    public String unique(final CacheMeta meta) {
        /*
         * Single Record
         */
        final L1Algorithm algorithm = Ut.singleton(AlgorithmCollection.class);
        return algorithm.dataUnique(meta.typeName(), this.condition);
    }

    @Override
    public boolean primary() {
        return false;
    }
}
