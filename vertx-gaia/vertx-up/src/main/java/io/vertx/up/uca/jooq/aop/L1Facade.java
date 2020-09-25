package io.vertx.up.uca.jooq.aop;

import io.vertx.tp.plugin.cache.Harp;
import io.vertx.tp.plugin.cache.hit.HKId;
import io.vertx.tp.plugin.cache.hit.HKey;
import io.vertx.tp.plugin.cache.hit.HMeta;
import io.vertx.tp.plugin.cache.l1.L1Cache;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.JqAnalyzer;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class L1Facade {

    private static final ConcurrentMap<Class<?>, HMeta> META_POOL = new ConcurrentHashMap<>();
    private final transient JqAnalyzer analyzer;

    private final transient L1Cache cacheL1;

    private L1Facade(final JqAnalyzer analyzer) {
        /*
         * Dao / Analyzer
         */
        this.analyzer = analyzer;
        /*
         * Vertx Processing
         */
        this.cacheL1 = Harp.cacheL1();
    }

    public static L1Facade create(final JqAnalyzer analyzer) {
        return new L1Facade(analyzer);
    }

    /*
     * Insert data
     */
    public <T> void insertL1(final T input) {
        this.cacheL1.flushAsync(input, ChangeFlag.ADD, this.meta());
    }

    public <T> void updateL1(final T input) {
        this.cacheL1.flushAsync(input, ChangeFlag.UPDATE, this.meta());
    }

    public <T> void deleteL1(final T input) {
        this.cacheL1.flushAsync(input, ChangeFlag.DELETE, this.meta());
    }

    private HMeta meta() {
        final Class<?> clazz = this.analyzer.type();
        return Fn.pool(META_POOL, clazz, () -> new HMeta(clazz).keys(this.analyzer.keys()));
    }

    /*
     * Fetch data by id
     */
    public <T> T findById(final Object id, final Supplier<T> executor) {
        return this.cached(() -> {
            // HKey for id
            final HKey key = new HKId(id);
            return this.cacheL1.hit(key, this.meta());
        }, executor);
    }

    private <T> T cached(final Supplier<T> internal, final Supplier<T> executor) {
        if (Objects.nonNull(this.cacheL1)) {
            try {
                final T found = internal.get();
                if (Objects.isNull(found)) {
                    return executor.get();
                } else {
                    return found;
                }
            } catch (final Throwable ex) {
                ex.printStackTrace();
                return executor.get();
            }
        } else return executor.get();
    }
}
