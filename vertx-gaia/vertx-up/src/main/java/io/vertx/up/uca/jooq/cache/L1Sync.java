package io.vertx.up.uca.jooq.cache;

import io.vertx.tp.plugin.cache.Harp;
import io.vertx.tp.plugin.cache.l1.L1Cache;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class L1Sync {

    private final transient L1Cache cacheL1;

    public L1Sync() {
        this.cacheL1 = Harp.cacheL1();
    }

    public <T> T findById(final Object id, final Supplier<T> executor) {
        if (Objects.nonNull(this.cacheL1)) {
            return null;
        } else return executor.get();
    }
}
