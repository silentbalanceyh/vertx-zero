package io.vertx.up.uca.jooq.aop;

import io.vertx.core.Future;
import io.vertx.tp.plugin.cache.hit.CacheId;
import io.vertx.tp.plugin.cache.hit.CacheKey;
import io.vertx.tp.plugin.cache.util.CacheFn;
import io.vertx.up.fn.RunSupplier;
import io.vertx.up.uca.jooq.JqAnalyzer;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class L1Fetch extends AbstractL1Aside {

    L1Fetch(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    /*
     * Fetch data by id
     * Here processed following:
     *
     * 1) Data Part: DATA:<MODEL>:<KEY>
     * 2）Key Part: KEY:<MODEL>:<KEY>,  Here the operation is append
     */
    public <T, K> T findById(final K id, final RunSupplier<T> executor) {
        final CacheKey key = new CacheId(id);
        return CacheFn.in(this.defend(() -> this.cacheL1.read(key, this.meta())), executor, this::writeCache);
    }

    public <T, K> Future<T> findByIdAsync(final K id, final RunSupplier<Future<T>> executor) {
        final CacheKey key = new CacheId(id);
        return CacheFn.in(this.defendAsync(() -> this.cacheL1.readAsync(key, this.meta())), executor, this::writeCache);
    }
}
