package io.vertx.up.unity.jq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.uca.condition.JooqCond;
import org.jooq.DSLContext;
import org.jooq.Field;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
class JqAggregator {

    private static final String FIELD_COUNT = "COUNT";

    private transient final VertxDAO vertxDAO;

    private transient JqAnalyzer analyzer;

    private JqAggregator(final VertxDAO vertxDAO, final JqAnalyzer analyzer) {
        this.vertxDAO = vertxDAO;
        this.analyzer = analyzer;
    }

    static JqAggregator create(final VertxDAO vertxDAO, final JqAnalyzer analyzer) {
        return new JqAggregator(vertxDAO, analyzer);
    }

    <T> Integer count(final Inquiry inquiry) {
        return this.count(null == inquiry.getCriteria() ? new JsonObject() : inquiry.getCriteria().toJson());
    }

    <T> Integer count(final JsonObject filters) {
        final DSLContext context = JooqInfix.getDSL();
        return null == filters ? context.fetchCount(this.vertxDAO.getTable()) :
                context.fetchCount(this.vertxDAO.getTable(), JooqCond.transform(filters, this.analyzer::column));
    }

    /*
     * Basic Method for low tier search/count pair
     */
    <T> Future<Integer> countAsync(final Inquiry inquiry) {
        return this.countAsync(null == inquiry.getCriteria() ? new JsonObject() : inquiry.getCriteria().toJson());
    }

    <T> Future<Integer> countAsync(final JsonObject filters) {
        final Function<DSLContext, Integer> function
                = dslContext -> null == filters ? dslContext.fetchCount(this.vertxDAO.getTable()) :
                dslContext.fetchCount(this.vertxDAO.getTable(), JooqCond.transform(filters, this.analyzer::column));
        return JqTool.future(this.vertxDAO.executeAsync(function));
    }

    <T> Future<ConcurrentMap<String, Integer>> countByAsync(final JsonObject filters, final String field) {
        return aggregateAsync(filters, field, Field::count);
    }

    private <T, A> Future<ConcurrentMap<String, A>> aggregateAsync(final JsonObject filters, final String field,
                                                                   final Function<Field, Field<A>> supplier) {
        final ConcurrentMap<String, A> result = new ConcurrentHashMap<>();
        final DSLContext context = JooqInfix.getDSL();
        final List<Map<String, Object>> queried;
        final Field hitField = this.analyzer.column(field);
        if (null == filters) {
            queried = context.select(hitField, supplier.apply(hitField).as(FIELD_COUNT))
                    .from(this.vertxDAO.getTable())
                    .groupBy(hitField).fetchMaps();
        } else {
            queried = context.select(hitField, supplier.apply(hitField).as(FIELD_COUNT))
                    .from(this.vertxDAO.getTable())
                    .where(JooqCond.transform(filters, this.analyzer::column))
                    .groupBy(hitField).fetchMaps();
        }
        queried.forEach(record -> {
            /*
             * Field
             */
            final String metaKey = hitField.getName();
            final Object key = record.get(metaKey);
            final Object value = record.get(FIELD_COUNT);
            if (Objects.nonNull(key) && Objects.nonNull(value)) {
                result.put(key.toString(), (A) value);
            }
        });
        return Future.succeededFuture(result);
    }
}
