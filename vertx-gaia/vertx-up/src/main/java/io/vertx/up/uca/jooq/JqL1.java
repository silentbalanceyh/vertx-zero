package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.up.uca.jooq.cache.L1Async;
import io.vertx.up.uca.jooq.cache.L1Sync;

import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
class JqL1 {

    private final transient VertxDAO vertxDAO;
    private final transient JqAnalyzer analyzer;

    private final transient L1Sync sync;
    private final transient L1Async async;

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
        this.sync = new L1Sync();
        this.async = new L1Async();
    }

    public static JqL1 create(final VertxDAO vertxDAO, final JqAnalyzer analyzer) {
        return new JqL1(vertxDAO, analyzer);
    }

    /*
     * Fetch data by id
     */
    public <T> T findById(final Object id, final Supplier<T> executor) {
        return sync.findById(id, executor);
    }
}
