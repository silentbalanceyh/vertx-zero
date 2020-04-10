package io.vertx.up.atom.query;

import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.web._400QueryKeyTypeException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Advanced Criteria Interface for query
 */
public interface Inquiry {
    // Criteria Keys
    String KEY_PAGER = "pager";
    String KEY_SORTER = "sorter";
    String KEY_CRITERIA = "criteria";
    String KEY_PROJECTION = "projection";

    String[] KEY_QUERY = new String[]{KEY_CRITERIA, KEY_PAGER, KEY_PROJECTION, KEY_SORTER};

    /**
     * Create Inquiry
     *
     * @param data json literal
     * @return Inquiry reference. ( simple criteria or qtree automatically )
     */
    static Inquiry create(final JsonObject data) {
        return new IrInquiry(data);
    }

    /**
     * Query key checking for search operation
     *
     * @param checkJson input checked
     * @param key       key
     * @param type      expected type.
     * @param predicate function to check current json
     * @param target    class who call this method.
     */
    static void ensureType(final JsonObject checkJson,
                           final String key, final Class<?> type,
                           final Predicate<Object> predicate,
                           final Class<?> target) {
        Fn.safeNull(() -> Fn.safeNull(() -> Fn.safeSemi(checkJson.containsKey(key), Annal.get(target), () -> {
            // Throw type exception
            final Object check = checkJson.getValue(key);
            Fn.outWeb(!predicate.test(check), Annal.get(target),
                    _400QueryKeyTypeException.class, target,
                    key, type, check.getClass());
        }), checkJson), target);
    }

    /**
     * Add field=value (key/pair) in current context.
     *
     * @param field field that will be added.
     * @param value value that will be added.
     */
    void setInquiry(String field, Object value);

    /**
     * Get projection
     *
     * @return Projection to do filter
     */
    Set<String> getProjection();

    /**
     * Get pager
     *
     * @return Pager for pagination
     */
    Pager getPager();

    /**
     * Get Sorter
     *
     * @return Sorter for order by
     */
    Sorter getSorter();

    /**
     * Get criteria
     *
     * @return criteria with and/or
     */
    Criteria getCriteria();

    /**
     * To JsonObject
     *
     * @return the raw data that will be input into Jooq Condition
     */
    JsonObject toJson();

    enum Connector {
        AND, OR
    }

    enum Mode {
        LINEAR, // Conditions merged in linear mode.
        TREE    // Conditions with query tree mode.
    }

    interface Instant {
        String DAY = "day";
        String DATE = "date";
        String TIME = "time";
        String DATETIME = "datetime";
    }

    interface Op {
        String LT = "<";
        String LE = "<=";
        String GT = ">";
        String GE = ">=";
        String EQ = "=";
        String NEQ = "<>";
        String NOT_NULL = "!n";
        String NULL = "n";
        String TRUE = "t";
        String FALSE = "f";
        String IN = "i";
        String NOT_IN = "!i";
        String START = "s";
        String END = "e";
        String CONTAIN = "c";

        Set<String> VALUES = new HashSet<String>() {
            {
                add(LT);
                add(LE);
                add(GT);
                add(GE);
                add(EQ);
                add(NEQ);
                add(NOT_NULL);
                add(NULL);
                add(TRUE);
                add(FALSE);
                add(IN);
                add(NOT_IN);
                add(START);
                add(END);
                add(CONTAIN);
            }
        };
    }
}
