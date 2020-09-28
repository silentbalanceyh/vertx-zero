package io.vertx.up.uca.jooq.aop;

import io.vertx.core.Future;
import io.vertx.tp.plugin.cache.Harp;
import io.vertx.tp.plugin.cache.hit.CacheKey;
import io.vertx.tp.plugin.cache.hit.CacheMeta;
import io.vertx.tp.plugin.cache.l1.L1Cache;
import io.vertx.tp.plugin.cache.util.CacheFn;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.fn.RunSupplier;
import io.vertx.up.uca.jooq.JqAnalyzer;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * L1 Cache abstract class
 */
public class L1Aside {
    private static final ConcurrentMap<Class<?>, CacheMeta> META_POOL = new ConcurrentHashMap<>();
    protected final transient JqAnalyzer analyzer;

    protected final transient L1Cache cacheL1;

    public L1Aside(final JqAnalyzer analyzer) {
        /*
         * Dao / Analyzer
         */
        this.analyzer = analyzer;
        /*
         * Vertx Processing
         */
        this.cacheL1 = Harp.cacheL1();
    }

    private CacheMeta meta() {
        final Class<?> clazz = this.analyzer.type();
        return Fn.pool(META_POOL, clazz, () -> new CacheMeta(clazz).primaryKey(this.analyzer.primarySet()));
    }


    /*
     * Private method for write
     */
    private <T> void writeCache(final T entity) {
        if (Objects.nonNull(this.cacheL1)) {
            this.cacheL1.write(entity, ChangeFlag.UPDATE, this.meta());
        }
    }

    private <T> void deleteCache(final T entity) {
        if (Objects.nonNull(this.cacheL1)) {
            this.cacheL1.write(entity, ChangeFlag.DELETE, this.meta());
        }
    }

    /*
     * Defined for null checking
     */
    private <T> Supplier<Future<T>> defendAsync(final Supplier<Future<T>> executor) {
        if (Objects.isNull(this.cacheL1)) {
            return Future::succeededFuture;
        } else return executor;
    }

    private <T> RunSupplier<T> defend(final RunSupplier<T> executor) {
        if (Objects.isNull(this.cacheL1)) {
            return null;
        } else return executor;
    }

    <T> T read(final CacheKey key, final RunSupplier<T> executor) {
        return CacheFn.in(this.defend(() -> this.cacheL1.read(key, this.meta())), executor, this::writeCache);
    }

    <T> Future<T> readAsync(final CacheKey key, final RunSupplier<Future<T>> executor) {
        return CacheFn.in(this.defendAsync(() -> this.cacheL1.readAsync(key, this.meta())), executor, this::writeCache);
    }
}
