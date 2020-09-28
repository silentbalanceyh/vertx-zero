package io.vertx.up.uca.jooq.aggr;

import io.github.jklingsporn.vertx.jooq.future.VertxDAO;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import io.vertx.up.uca.jooq.JqAnalyzer;
import io.vertx.up.util.Ut;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SelectJoinStep;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Abstract for aggr processing
 */
@SuppressWarnings("all")
public abstract class AbstractAggr {

    protected transient final VertxDAO vertxDAO;

    protected final transient JqAnalyzer analyzer;

    protected AbstractAggr(final JqAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.vertxDAO = analyzer.vertxDAO();
    }

    /*
     * This method is for grouped result processing, the grouped result by Jooq is as following:
     * The data structure is `List<Map<String,Object>>` groupResult
     * [
     *      {
     *            "field": "aggregation"
     *      }
     * ]
     * This methos should flat the result to
     * {
     *      "field1": "aggregation1",
     *      "field2": "aggregation2"
     * }
     * 「Single t Field」
     */
    protected <T> ConcurrentMap<String, T> toMap(final String tColumn,
                                                 final List<Map<String, Object>> groupResult, final String groupField) {
        final ConcurrentMap<String, T> result = new ConcurrentHashMap<>();
        final Field hitField = this.analyzer.column(groupField);
        final String metaKey = hitField.getName();
        groupResult.forEach(record -> {
            final Object key = record.get(metaKey);
            final Object value = record.get(tColumn);
            if (Objects.nonNull(key) && Objects.nonNull(value)) {
                result.put(key.toString(), (T) value);
            }
        });
        return result;
    }

    /*
     * This method is for grouped result processing, the grouped result by Jooq is as following:
     * The data structure is `List<Map<String,Object>>` groupResult
     * [
     *      {
     *            "field": "aggregation"
     *      }
     * ]
     * The result of this method is as following:
     * [
     *      {
     *            "field1": "value11",
     *            "field2": "value11",
     *            ......
     *            "fieldN": "valueN",
     *            "count":  "COUNT"
     *      },
     *      {
     *            "field1": "value11",
     *            "field2": "value12",
     *            ......
     *            "fieldN": "valueN",
     *            "count":  "COUNT"
     *      }
     * ]
     * Here are the rule,
     *
     * 1) The field of each group is COLUMN to lower, for example: COUNT -> count
     * 2) The result is for each complex group such as "field1 + field2 + ... + fieldN"
     */
    protected JsonArray toArray(final String tColumn, final List<Map<String, Object>> groupResult, final String... groupFields) {
        final JsonArray result = new JsonArray();
        groupResult.forEach(record -> {
            final JsonObject json = new JsonObject();
            Arrays.stream(groupFields).forEach(field -> {
                final Field hitColumn = this.analyzer.column(field);
                final String columnName = hitColumn.getName();
                json.put(field, record.get(columnName));
            });
            json.put(tColumn.toLowerCase(Locale.getDefault()), record.get(tColumn));
            result.add(json);
        });
        return result;
    }

    protected <T> List<Map<String, Object>> fetchAggregation(final JsonObject filters, final Field[] selectedFields, final String... groupFields) {
        /*
         * Field[] by groupFields
         */
        final Field[] columns = this.analyzer.column(groupFields);
        /*
         * Result Part, Get `DSLContext` from environment
         */
        final DSLContext context = JooqInfix.getDSL();
        /*
         * Result here, the type is:
         * List<Map<String, Object>>
         */
        final SelectJoinStep selected = context
                .select(selectedFields)
                .from(this.vertxDAO.getTable());
        /*
         * Result calculation
         */
        final List<Map<String, Object>> groupResult;
        if (Ut.isNil(filters)) {
            groupResult = selected
                    .groupBy(columns).fetchMaps();
        } else {
            groupResult = selected
                    /*
                     * Where condition for result
                     */
                    .where(JooqCond.transform(filters, this.analyzer::column))
                    .groupBy(columns).fetchMaps();
        }
        return groupResult;
    }
}
