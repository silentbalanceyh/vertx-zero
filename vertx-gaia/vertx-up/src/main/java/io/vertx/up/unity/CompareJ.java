package io.vertx.up.unity;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CompareJ {
    private static final Annal LOGGER = Annal.get(CompareJ.class);

    // ------------------------- Compare Json ------------------------
    static JsonArray ruleJReduce(final JsonArray records, final JsonArray matrix) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(records).filter(json -> ruleJOk(json, matrix)).forEach(normalized::add);
        return normalized;
    }

    static JsonArray ruleJReduce(final JsonArray records, final Set<String> fields) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(records).filter(json -> ruleJOk(json, fields)).forEach(normalized::add);
        return normalized;
    }

    static boolean ruleJOk(final JsonObject record, final Set<String> fields) {
        /*
         * All the uniqueFields must contain value
         */
        return fields.stream().allMatch(field ->
            Objects.nonNull(record.getValue(field)));
    }

    static boolean ruleJOk(final JsonObject record, final JsonArray matrix) {
        /*
         * Matrix may be multi groups
         */
        final int size = matrix.size();
        for (int idx = 0; idx < size; idx++) {
            final Object value = matrix.getValue(idx);
            final Set<String> fields = fieldSet(value);
            if (fields.isEmpty()) {
                /*
                 * Not unique defined, monitor the reduce processing:
                 * Fix: https://github.com/silentbalanceyh/hotel/issues/297
                 */
                LOGGER.warn("[ RD ] Reduce Process: Fields is empty !!! matrix = {0}",
                    matrix.encode());
                return false;
            }


            /*
             * Compare each group for matrix, monitor the reduce processing:
             * Fix: https://github.com/silentbalanceyh/hotel/issues/297
             */
            final boolean match = ruleJOk(record, fields);
            if (!match) {
                LOGGER.warn("[ RD ] Reduce Process: Fields is `{0}` matrix = {1}",
                    Ut.fromJoin(fields), matrix.encode());
                return false;
            }
        }
        return true;
    }

    /*
     * Here should be some comments for group unique rules
     * 1. When you check whether the data is OK, here should be:
     *    ----> Unique 1  --> Ok
     *    ----> Unique 2  --> Ok   ----- All the rule should be Ok
     *    ----> Unique 2  --> Ok
     * 2. When you want to match whether the two record are equal, here should be:
     *    ----> Unique 1  --> Match
     *    ----> Unique 2  --> Not    ----- Any rule should be matched
     *    ----> Unique 3  --> Match
     *    Here are the priority of each Unique Rule,
     *    The situation is often
     *    1)  Primary Key
     *    2)  Unique Key
     */
    static boolean ruleJEqual(final JsonObject record, final JsonObject latest,
                              final JsonArray matrix) {
        /*
         * Matrix may be multi groups
         */
        final int size = matrix.size();
        for (int idx = 0; idx < size; idx++) {
            final Object value = matrix.getValue(idx);
            final Set<String> fields = fieldSet(value);
            if (fields.isEmpty()) {
                /*
                 * Not unique defined, check the next
                 * rule here.
                 */
                continue;
            }
            /*
             * Compare each group for matrix
             * Find any one rule should be OK here for equal
             * 1) Primary Key    - 0
             * 2) Unique Key     - 1
             */
            final boolean equal = ruleJEqual(record, latest, fields);
            if (equal) {
                return true;
            }
        }
        return false;
    }

    static boolean ruleJEqual(final JsonObject record, final JsonObject latest,
                              final Set<String> fields) {
        final JsonObject subR = Ut.elementSubset(record, fields);
        final JsonObject subL = Ut.elementSubset(latest, fields);
        return subR.equals(subL);
    }

    static JsonObject ruleJFind(final JsonArray source, final JsonObject expected,
                                final Set<String> fields) {
        return Ut.itJArray(source).filter(json -> ruleJEqual(json, expected, fields))
            .findAny().orElse(new JsonObject());
    }

    static JsonObject ruleJFind(final JsonArray source, final JsonObject expected,
                                final JsonArray matrix) {
        return Ut.itJArray(source).filter(json -> ruleJEqual(json, expected, matrix))
            .findAny().orElse(new JsonObject());
    }

    private static Set<String> fieldSet(final Object value) {
        final Set<String> fields;
        if (value instanceof JsonArray) {
            fields = Ut.toSet((JsonArray) value);
        } else if (value instanceof String) {
            fields = new HashSet<>();
            fields.add((String) value);
        } else {
            fields = new HashSet<>();
        }
        return fields;
    }

    static ConcurrentMap<ChangeFlag, JsonArray> compareJ(
        final JsonArray original, final JsonArray current, final Set<String> fields) {
        return compareJ(original, current,
            (source, record) -> ruleJFind(source, record, fields));
    }

    static ConcurrentMap<ChangeFlag, JsonArray> compareJ(
        final JsonArray original, final JsonArray current, final JsonArray matrix) {
        return compareJ(original, current,
            (source, record) -> ruleJFind(source, record, matrix));
    }

    private static ConcurrentMap<ChangeFlag, JsonArray> compareJ(
        final JsonArray original, final JsonArray current,
        // 2 Function
        final BiFunction<JsonArray, JsonObject, JsonObject> findFn
    ) {
        final ConcurrentMap<ChangeFlag, JsonArray> result = new ConcurrentHashMap<>();
        result.put(ChangeFlag.UPDATE, new JsonArray());
        result.put(ChangeFlag.ADD, new JsonArray());
        result.put(ChangeFlag.DELETE, new JsonArray());
        Ut.itJArray(original).forEach(recordO -> {
            final JsonObject recordN = findFn.apply(current, recordO);
            if (Ut.isNil(recordN)) {
                // New: x, Old: o
                result.get(ChangeFlag.DELETE).add(recordO);
            } else {
                // New: o, Old: o
                // Do not overwrite `key` field because is primary key
                final JsonObject recordNC = recordN.copy();
                if (recordNC.containsKey(KName.KEY)) {
                    recordNC.remove(KName.KEY);
                }
                final JsonObject combine = recordO.copy().mergeIn(recordNC, true);
                result.get(ChangeFlag.UPDATE).add(combine);
            }
        });
        Ut.itJArray(current).forEach(recordN -> {
            final JsonObject recordO = findFn.apply(original, recordN);
            if (Ut.isNil(recordO)) {
                // New: o, Old: x
                result.get(ChangeFlag.ADD).add(recordN);
            }
        });
        return result;
    }
}
