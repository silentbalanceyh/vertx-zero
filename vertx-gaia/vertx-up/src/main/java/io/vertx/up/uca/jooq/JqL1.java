package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.tp.plugin.cache.Harp;
import io.vertx.tp.plugin.cache.hit.HKId;
import io.vertx.tp.plugin.cache.hit.HKey;
import io.vertx.tp.plugin.cache.l1.L1Cache;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
class JqL1 {

    private final transient VertxDAO vertxDAO;
    private final transient JqAnalyzer analyzer;

    private final transient L1Cache cacheL1;

    private JqL1(final VertxDAO vertxDAO,
                 final JqAnalyzer analyzer) {
        /*
         * Dao / Analyzer
         */
        this.vertxDAO = vertxDAO;
        this.analyzer = analyzer;
        /*
         * Vertx Processing
         */
        this.cacheL1 = Harp.cacheL1();
    }

    public static JqL1 create(final VertxDAO vertxDAO, final JqAnalyzer analyzer) {
        return new JqL1(vertxDAO, analyzer);
    }

    /*
     * Insert data
     */
    public <T> void insertAsync(final T input) {
        final Class<T> entityCls = (Class<T>) this.analyzer.type();
        this.cacheL1.flushAsync(input, ChangeFlag.ADD, entityCls);
    }

    /*
     * Fetch data by id
     */
    public <T> T findById(final Object id, final Supplier<T> executor) {
        return cached(() -> {
            // HKey for id
            final HKey key = new HKId(id);
            final Class<T> entityCls = (Class<T>) this.analyzer.type();
            return this.cacheL1.hit(key, entityCls);
        }, executor);
    }

    private <T> T cached(final Supplier<T> internal, final Supplier<T> executor) {
        if (Objects.nonNull(this.cacheL1)) {
            return Fn.getJvm(executor.get(), () -> internal.get());
        } else return executor.get();
    }
}
