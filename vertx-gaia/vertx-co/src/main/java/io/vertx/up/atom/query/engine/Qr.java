package io.vertx.up.atom.query.engine;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.atom.query.Pager;
import io.vertx.up.atom.query.Sorter;
import io.vertx.up.exception.web._400QueryKeyTypeException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * ## Query Engine Interface
 * ### 1. Intro
 * Advanced Criteria Interface for query engine, it provide critical api interfaces.
 * ### 2. Data Structure
 * The full query criteria data structure is as following:
 * ```json
 * // <pre><code class="json">
 *     {
 *         "pager": {
 *              "page":"",
 *              "size":""
 *         },
 *         "sorter": [
 *              "field1,ASC",
 *              "field2,DESC"
 *         ],
 *         "projection": [
 *              "field1",
 *              "field2"
 *         ],
 *         "criteria":{
 *         }
 *     }
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Qr {
    /**
     * <value>pager</value>, It's mapped to `pager` field in json configuration.
     * 「Pagination」This json contains following two field:
     * 1. page: The page index ( started from 1 ).
     * 2. size: The page size.
     */
    String KEY_PAGER = "pager";
    /**
     * <value>sorter</value>, It's mapped to `sorter` field in json configuration.
     * 「Sorting」It's json array to contains different field with sorting mode.
     * 1. ASC: ORDER BY ?? ASC.
     * 2. DESC: ORDER BY ?? DESC.
     */
    String KEY_SORTER = "sorter";

    /**
     * <value>criteria</value>, It's mapped to `criteria` field in json configuration.
     * 「Criteria」
     */
    String KEY_CRITERIA = "criteria";

    /**
     * <value>projection</value>, It's mapped to `projection` field in json configuration.
     * 「Fields」It's feature for column picking up.
     */
    String KEY_PROJECTION = "projection";

    /**
     * It's specification for fields to be sure the request data.
     * The system should detect the request whether it's Qr parameters ( request ).
     */
    String[] KEY_QUERY = new String[]{KEY_CRITERIA, KEY_PAGER, KEY_PROJECTION, KEY_SORTER};

    /**
     * Create Qr instance ( The default implementation class is {@link IrQr} )
     * The implementation class name meaning is `IrQr` - Internal Reactive Query Engine
     *
     * @param data {@link io.vertx.core.json.JsonObject} json literal
     *
     * @return {@link Qr} reference. ( simple criteria or qtree automatically )
     */
    static Qr create(final JsonObject data) {
        return new IrQr(data);
    }

    /**
     * Query key checking for search operation, this method could be sure the query searching be executed safely.
     *
     * @param checkJson input checked json object.
     * @param key       key field that will be checked.
     * @param type      expected type of java class.
     * @param predicate function to check current json.
     * @param target    class who call this method.
     */
    static void ensureType(final JsonObject checkJson,
                           final String key, final Class<?> type,
                           final Predicate<Object> predicate,
                           final Class<?> target) {
        Fn.safeNull(() -> Fn.safeNull(() -> Fn.safeSemi(checkJson.containsKey(key), Annal.get(target), () -> {
            // Throw type exception
            final Object check = checkJson.getValue(key);
            Fn.outWeb(!predicate.test(check), Annal.get(target), _400QueryKeyTypeException.class, target, key, type, check.getClass());
        }), checkJson), target);
    }

    /**
     * Add `field = value` (key/pair) in current context.
     *
     * @param field {@link java.lang.String} field that will be added.
     * @param value {@link java.lang.Object} value that will be added.
     */
    void setQr(String field, Object value);

    /**
     * Get projection
     *
     * @return {@link java.util.Set} Projection to do filter
     */
    Set<String> getProjection();

    /**
     * Get pager
     *
     * @return {@link Pager} Pager for pagination
     */
    Pager getPager();

    /**
     * Get Sorter
     *
     * @return {@link Sorter} Sorter for order by
     */
    Sorter getSorter();

    /**
     * Get criteria
     *
     * @return {@link Criteria} criteria with and/or
     */
    Criteria getCriteria();

    /**
     * To JsonObject
     *
     * @return {@link io.vertx.core.json.JsonObject} the raw data that will be input into Jooq Condition
     */
    JsonObject toJson();

    /**
     * The where condition connector of two: AND / OR.
     * - AND: `cond1 AND cond2`.
     * - OR: `cond1 OR cond2`.
     */
    enum Connector {
        /**
         * Connector AND
         */
        AND,
        /**
         * Connector OR
         */
        OR
    }

    /**
     * The query condition mode of two: LINEAR / TREE.
     * - LINEAR: The json query condition is 1 level.
     * - TREE: The complex query condition
     */
    enum Mode {
        /**
         * LINEAR: Conditions merged in linear mode.
         */
        LINEAR,
        /**
         * TREE:  Conditions with query tree mode.
         */
        TREE
    }

    /**
     * Critical condition field `flag` for date field
     * 1. DAY: Date only.
     * 2. DATE: Date + Time.
     * 3. TIME: Time only.
     * 4. DATETIME: Timestamp.
     */
    interface Instant {
        /**
         * {@link java.time.LocalDate} Date format only here.
         */
        String DAY = "day";
        /**
         * {@link java.time.LocalDate} Date + Time ( `yyyy-MM-dd` )
         */
        String DATE = "date";
        /**
         * {@link java.time.LocalTime} Time format only.
         */
        String TIME = "time";
        /**
         * {@link java.time.LocalDateTime} Full format and timestamp.
         */
        String DATETIME = "datetime";
    }

    /**
     * The operator in where clause.
     */
    interface Op {
        /**
         * less than
         */
        String LT = "<";
        /**
         * less than or equal
         */
        String LE = "<=";
        /**
         * greater than
         */
        String GT = ">";
        /**
         * greater than or equal
         */
        String GE = ">=";
        /**
         * equal
         */
        String EQ = "=";
        /**
         * not equal
         */
        String NEQ = "<>";
        /**
         * not null
         */
        String NOT_NULL = "!n";
        /**
         * is null
         */
        String NULL = "n";
        /**
         * equal `TRUE` ( Boolean )
         */
        String TRUE = "t";
        /**
         * equal `FALSE` ( Boolean )
         */
        String FALSE = "f";
        /**
         * in (value1, value2), processed in array.
         */
        String IN = "i";
        /**
         * not in (value1, value2), calculated in array.
         */
        String NOT_IN = "!i";
        /**
         * start with ( String )
         */
        String START = "s";
        /**
         * end with ( String )
         */
        String END = "e";
        /**
         * contains ( String )
         */
        String CONTAIN = "c";

        /**
         * The constant collection of all {@link Op} values.
         */
        Set<String> VALUES = new HashSet<String>() {
            {
                this.add(LT);
                this.add(LE);
                this.add(GT);
                this.add(GE);
                this.add(EQ);
                this.add(NEQ);
                this.add(NOT_NULL);
                this.add(NULL);
                this.add(TRUE);
                this.add(FALSE);
                this.add(IN);
                this.add(NOT_IN);
                this.add(START);
                this.add(END);
                this.add(CONTAIN);
            }
        };
    }
}
