package io.vertx.up.uca.jooq;

import io.horizon.eon.VValue;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.jooq.condition.JooqCond;
import io.vertx.up.util.Ut;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SelectJoinStep;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Abstract for aggr processing
 */
@SuppressWarnings("all")
abstract class AbstractAggregator extends AbstractAction {

    protected AbstractAggregator(final JqAnalyzer analyzer) {
        super(analyzer);
    }

    /*
     * Multi Group
     *
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
     *
     */
    protected JsonArray aggregateBy(final Field aggrField, final JsonObject criteria, final String... groupFields) {
        /*
         * Aggr by fields
         */
        final List<Map<String, Object>> groupResult = this.fetchAggregation(criteria, new Field[]{aggrField}, groupFields);
        final JsonArray result = new JsonArray();
        groupResult.forEach(record -> {
            final JsonObject json = new JsonObject();
            /*
             * Group data here
             */
            Arrays.stream(groupFields).forEach(field -> {
                final Field hitColumn = this.analyzer.column(field);
                final String columnName = hitColumn.getName();
                json.put(field, record.get(columnName));
            });
            /*
             * Default rule here
             * 1) COUNT -> count
             * 2) MAX -> max
             * 3) SUM -> sum
             * 4) MIN -> min
             * 5) AVERAGE -> average
             */
            final String tColumn = aggrField.getName();
            json.put(tColumn.toLowerCase(Locale.getDefault()), record.get(tColumn));
            result.add(json);
        });
        return result;
    }

    /*
     * Single Group
     *
     * SELECT <aggrField> FROM ... GROUP BY <groupField>
     *
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
    protected <T> ConcurrentMap<String, T> aggregateBy(final Field aggrField, final JsonObject criteria, final String groupField) {
        /*
         * Aggr by fields
         */
        final List<Map<String, Object>> groupResult = this.fetchAggregation(criteria, new Field[]{aggrField}, groupField);
        /*
         * Result calculation
         */
        final ConcurrentMap<String, T> result = new ConcurrentHashMap<>();
        final Field hitField = this.analyzer.column(groupField);
        final String metaKey = hitField.getName();
        groupResult.forEach(record -> {
            /*
             * Extract data from aggr result
             */
            final Object key = record.get(metaKey);
            final Object value = record.get(aggrField.getName());
            if (Objects.nonNull(key) && Objects.nonNull(value)) {
                /*
                 * Here are converting processing
                 * Object -> T
                 */
                result.put(key.toString(), (T) value);
            }
        });
        return result;
    }

    // ---------------- Aggregation Operation -----------
    /*
     * Aggregation to array, -> JsonArray
     * - More than 1 group
     */
    protected <T> JsonArray aggregateBy(
        final String fieldName, final JsonObject criteria,
        /* Critical Function Processing */
        final Function<Field, Field> aggrFun, final String... groupFields) {
        return this.aggregateBy(fieldName, new JsonArray(), field -> {
            /*
             * Field process
             */
            final Field aggrField = aggrFun.apply(field);
            /*
             * Process / Aggr
             */
            return this.aggregateBy(aggrField, criteria, groupFields);
        });
    }

    /*
     * Aggregation to map, -> ConcurrentMap<String, T>
     * - Only one group
     */
    protected <T> ConcurrentMap<String, T> aggregateBy(
        final String fieldName, final JsonObject criteria,
        /* Critical Function Processing */
        final Function<Field, Field> aggrFun, final String groupField) {
        return this.aggregateBy(fieldName, new ConcurrentHashMap<>(), field -> {
            /*
             * Field process
             */
            final Field aggrField = aggrFun.apply(field);
            /*
             * Process / Aggr
             */
            return this.aggregateBy(aggrField, criteria, groupField);
        });
    }

    /*
     * Aggregation to single result, -> T
     */
    protected <T> T aggregateBy(
        final String fieldName, final JsonObject criteria,
        /* Critical Function Processing */
        final Function<Field, Field> aggrFun, final T defaultValue) {
        return this.aggregateBy(fieldName, defaultValue, field -> {
            /*
             * Field process
             */
            final Field procField = aggrFun.apply(field);
            /*
             * Process / Aggr
             */
            return this.fetchAggregation(criteria, procField);
        });
    }

    // ---------------- Private Operation -----------
    /*
     * Uniform method to execute
     */
    private <T> T aggregateBy(final String fieldName, final T defaultValue, final Function<Field, T> function) {
        /*
         * Field of current model
         */
        final Field aggrField = this.analyzer.column(fieldName);
        if (Objects.isNull(aggrField)) {
            /*
             * Default value of current T
             */
            return defaultValue;
        } else {
            /*
             * Field -> T execution
             */
            return function.apply(aggrField);
        }
    }

    /*
     * The critical method for aggregation calculation
     * for complex SQL
     * 1) COUNT
     * 2) MAX
     * 3) MIN
     * 4) AVG
     * 5) SUM
     */
    private <T> List<Map<String, Object>> fetchAggregation(final JsonObject criteria, final Field[] selectedFields, final String... groupFields) {
        /*
         * Field[] by groupFields
         */
        final Field[] columns = this.analyzer.column(groupFields);
        /*
         * Result Part, Get `DSLContext` from environment
         */
        final DSLContext context = this.dsl.context();
        /*
         * Combine Field: Group + Aggr Fields
         */
        final List<Field> selectedList = new ArrayList<>();
        selectedList.addAll(Arrays.asList(columns));
        selectedList.addAll(Arrays.asList(selectedFields));
        /*
         * Result here, the type is:
         * List<Map<String, Object>>
         */
        final SelectJoinStep selected = context
            .select(selectedList.toArray(new Field[]{}))
            .from(this.analyzer.table());
        /*
         * Result calculation
         */
        final List<Map<String, Object>> groupResult;
        if (0 == columns.length) {
            /*
             * Directly to fetch ( Without Group )
             */
            if (Ut.isNil(criteria)) {
                /*
                 * No condition
                 */
                groupResult = selected.fetchMaps();
            } else {
                /*
                 * Condition of criteria only
                 */
                groupResult = selected
                    /*
                     * Where condition for result
                     */
                    .where(JooqCond.transform(criteria, this.analyzer::column))
                    .fetchMaps();
            }
        } else {
            if (Ut.isNil(criteria)) {
                /*
                 * Group field passed only
                 */
                groupResult = selected
                    .groupBy(columns).fetchMaps();
            } else {
                groupResult = selected
                    /*
                     * Where condition for result
                     */
                    .where(JooqCond.transform(criteria, this.analyzer::column))
                    .groupBy(columns).fetchMaps();
            }
        }
        return groupResult;
    }

    private <T> T fetchAggregation(final JsonObject criteria, final Field aggrField) {
        /*
         * Fetch aggregation field
         */
        final List<Map<String, Object>> fetched = this.fetchAggregation(criteria, new Field[]{aggrField});
        /*
         * Single aggregation here
         */
        if (1 == fetched.size()) {
            final Map<String, Object> result = fetched.get(VValue.IDX);
            /*
             * Get object result here
             */
            return (T) result.get(aggrField.getName());
        } else {
            /*
             * null reference here
             */
            return null;
        }
    }
}
