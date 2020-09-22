package io.vertx.up.uca.jooq;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.uca.condition.JooqCond;
import io.vertx.up.util.Ut;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.*;
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

    // -------------------- Count Operation ------------
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

    /*
     * Count Function by group field here
     * The aggregation result is List<Map<String,Object>> reference here,
     * Here the result should be:
     *
     * Map Data Structure:
     *  - Group1 = Group1's Counter
     *  - Group2 = Group2's Counter
     *  - ......
     *  - GroupN = GroupN's Counter
     *
     * The limitation is that the grouped field should be only one
     */
    <T> Future<ConcurrentMap<String, Integer>> countByAsync(final JsonObject filters, final String field) {
        final List<Map<String, Object>> queried = queryGrouped(filters, field);
        return Future.succeededFuture(this.toMap(queried, field));
    }

    // -------------------- Group Operation ------------
    /*
     * Group Fields here
     * The aggregation result is List<Map<String,Object>> reference here,
     * Here the result should be:
     *
     * List<JsonArray> Data Structure, here each element shouldd be:
     * {
     *     "field1": "value1",
     *     "field2": "value2",
     *     ......
     *     "fieldN": "valueN",
     *     "count": "COUNT"
     * }
     */
    Future<JsonArray> groupByAsync(final JsonObject filters, final String... fields) {
        final List<Map<String, Object>> queried = queryGrouped(filters, fields);
        return Future.succeededFuture(toArray(queried, fields));
    }

    // -------------------- Private Function for defined operation ------------
    private JsonArray toArray(final List<Map<String, Object>> queried, final String... fields) {
        final JsonArray result = new JsonArray();
        queried.forEach(record -> {
            final JsonObject json = new JsonObject();
            Arrays.stream(fields).forEach(field -> {
                final Field hitField = this.analyzer.column(field);
                final String fieldKey = hitField.getName();
                /*
                 * Json Object building
                 */
                json.put(field, record.get(fieldKey));
            });
            json.put("count", record.get(FIELD_COUNT));
            result.add(json);
        });
        return result;
    }

    private <A> ConcurrentMap<String, A> toMap(final List<Map<String, Object>> queried, final String keyField) {
        final ConcurrentMap<String, A> result = new ConcurrentHashMap<>();
        final Field hitField = this.analyzer.column(keyField);
        final String metaKey = hitField.getName();
        queried.forEach(record -> {
            final Object key = record.get(metaKey);
            final Object value = record.get(FIELD_COUNT);
            if (Objects.nonNull(key) && Objects.nonNull(value)) {
                result.put(key.toString(), (A) value);
            }
        });
        return result;
    }

    private <A> List<Map<String, Object>> queryGrouped(final JsonObject filters,
                                                       final String... fields) {
        /*
         * Field[] building
         */
        final List<Field> fieldList = new ArrayList<>();
        Arrays.asList(fields).forEach(field -> {
            /*
             * Column Field
             */
            final Field columnField = this.analyzer.column(field);
            if (Objects.nonNull(columnField)) {
                fieldList.add(columnField);
            }
        });
        /*
         * Count Here
         */
        final Field countField = DSL.field("*").count().as(FIELD_COUNT);
        final List<Field> selectedList = new ArrayList<>(fieldList);
        selectedList.add(countField);
        /*
         * Get `DSLContext` from environment `Jooq Infix` plugin here
         */
        final DSLContext context = JooqInfix.getDSL();

        /*
         * Select Part Processing here
         */
        final List<Map<String, Object>> queried;
        final Field[] groupedFields = fieldList.toArray(new Field[]{});
        final Field[] selectedFields = selectedList.toArray(new Field[]{});
        if (Ut.isNil(filters)) {
            queried = context.select(selectedFields)
                    .from(this.vertxDAO.getTable())
                    .where(JooqCond.transform(filters, this.analyzer::column))
                    .groupBy(groupedFields).fetchMaps();
        } else {
            queried = context.select(selectedFields)
                    .from(this.vertxDAO.getTable())
                    .groupBy(groupedFields).fetchMaps();
        }
        return queried;
    }
}
