package io.vertx.up.uca.jooq.aggr;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import io.vertx.up.uca.jooq.JqAnalyzer;
import io.vertx.up.uca.jooq.util.JqTool;
import io.vertx.up.util.Ut;
import org.jooq.DSLContext;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * SQL Statement:
 * -- SELECT COUNT(*) FROM <TABLE_NAME>
 */
@SuppressWarnings("all")
class AgCount extends AbstractAggr {

    private static final String FIELD_COUNT = "COUNT";

    private AgCount(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    static AgCount create(final JqAnalyzer analyzer) {
        return new AgCount(analyzer);
    }

    // ------------- Count Operation --------------
    /*
     * Count All
     */
    Long count() {
        return this.vertxDAO.count();
    }

    Future<Long> countAsync() {
        return JqTool.future(this.vertxDAO.countAsync());
    }

    Long count(final JsonObject filters) {
        final DSLContext context = JooqInfix.getDSL();
        return this.countInternal(context, filters);
    }

    <T> Future<Long> countAsync(final JsonObject filters) {
        final Function<DSLContext, Long> executor = context -> countInternal(context, filters);
        return JqTool.future(this.vertxDAO.executeAsync(executor));
    }

    ConcurrentMap<String, Integer> countBy(final JsonObject filters, final String field) {
        return null;
    }

    /*
     * Count Grouped
     */


    // ---------------- Private Operation -----------
    private long countInternal(final DSLContext context, final JsonObject filters) {
        return Ut.isNil(filters) ? context.fetchCount(this.vertxDAO.getTable()) :
                context.fetchCount(this.vertxDAO.getTable(), JooqCond.transform(filters, this.analyzer::column));
    }

}
