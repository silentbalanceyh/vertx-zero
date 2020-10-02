package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * SQL Statement:
 * -- SELECT COUNT(*) FROM <TABLE_NAME>
 */
@SuppressWarnings("all")
class ActionCount extends AbstractAggregator {

    private static final String FIELD_COUNT = "COUNT";

    ActionCount(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    // ------------- Count Operation --------------

    Long count() {
        return this.vertxDAO.count();
    }

    Future<Long> countAsync() {
        return this.successed(this.vertxDAO.countAsync());
    }

    Long count(final JsonObject criteria) {
        return this.countInternal(this.context(), criteria);
    }

    <T> Future<Long> countAsync(final JsonObject criteria) {
        final Function<DSLContext, Long> executor = context -> countInternal(context, criteria);
        return this.successed(this.vertxDAO.executeAsync(executor));
    }

    ConcurrentMap<String, Integer> countBy(final JsonObject filters, final String field) {
        return null;
    }

    /*
     * Count Grouped
     */

    // ---------------- Private Operation -----------
    /*
     * 「Sync method」
     * Simple count
     */
    private long countInternal(final DSLContext context, final JsonObject criteria) {
        final Condition condition = this.condition(criteria);
        if (Objects.isNull(condition)) {
            return context.fetchCount(this.vertxDAO.getTable());
        } else {
            return context.fetchCount(this.vertxDAO.getTable(), condition);
        }
    }

}
