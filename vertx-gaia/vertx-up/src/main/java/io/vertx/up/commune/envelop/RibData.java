package io.vertx.up.commune.envelop;


import io.horizon.eon.VName;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KWeb;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * Data Part Only
 */
class RibData {
    /*
     * Extract Data directly
     */
    @SuppressWarnings("all")
    static <T> T get(final JsonObject data) {
        if (Objects.isNull(data) || !data.containsKey(VName.DATA)) {
            /*
             * 2 situations:
             * 1) data = null
             * 2) data without `data` key ( Zero Specification defined )
             */
            return null;
        } else {
            final Object value = data.getValue(VName.DATA);
            if (Objects.isNull(value)) {
                /*
                 * `data` key value is null.
                 */
                return null;
            } else {
                return (T) value;
            }
        }
    }

    /*
     * Extract Data by Type
     */
    static <T> T get(final JsonObject data, final Class<?> clazz) {
        T reference = null;
        if (data.containsKey(VName.DATA)) {
            final Object value = data.getValue(VName.DATA);
            reference = Rib.deserialize(value, clazz);
        }
        return reference;
    }

    static JsonObject getBody(final JsonObject data) {
        final Object reference = getData(data, null);
        // Fix Bug: Caused by: java.lang.ClassCastException:
        if (reference instanceof JsonObject) {
            return (JsonObject) reference;
        } else {
            return get(data, JsonObject.class);
        }
    }

    /*
     * Extract Data by Index
     * Here are complex situation in new version here
     */
    @SuppressWarnings("unchecked")
    static <T> T get(final JsonObject data, final Class<?> clazz, final Integer index) {
        T reference = null;
        if (data.containsKey(VName.DATA)) {
            final Object rawData = data.getValue(VName.DATA);
            /* Check whether data is complex object */
            if (rawData instanceof JsonObject) {
                final JsonObject raw = (JsonObject) rawData;
                if (Ut.isNil(raw)) {
                    /* Shorten */
                    return null;
                }
                /* Key */
                final String key = KWeb.MULTI.INDEXES.get(index);
                /* Index Checking */
                if (raw.containsKey(key)) {
                    /* Interface Style */
                    reference = Rib.deserialize(raw.getValue(key), clazz);
                } else {
                    /*
                     * If JsonObject.class
                     * Spec Situation
                     * {
                     *      "data": {
                     *      }
                     * }
                     *  */
                    if (JsonObject.class == clazz) {
                        reference = (T) raw;
                    }
                }
            } else {
                /* */
                reference = (T) rawData;
            }
        }
        return reference;
    }

    static <T> void set(final JsonObject data, final String field, final T value, final Integer argIndex) {
        final Object reference = getData(data, argIndex);
        if (reference instanceof JsonObject) {
            final JsonObject ref = (JsonObject) reference;
            ref.put(field, value);
        } else if (reference instanceof JsonArray) {
            final JsonArray ref = (JsonArray) reference;
            Ut.itJArray(ref).forEach(refEach -> refEach.put(field, value));
        }
    }

    /*
     * Find json object ( Body Part )
     */
    private static Object getData(final JsonObject data, final Integer argIndex) {
        Object found;

        /*
         * If the data ( JsonObject this.data ) does not contains "data",
         * Extract `data` part directly here.
         *
         * --------------- Data Part
         * 1.1. Format 1-1:
         * {
         *      "data": {
         *          "0": ...,
         *          "1": ...
         *      }
         * }
         * 1.2. Format 1-2:
         * {
         *      "data": {
         *          "f1": ...,
         *          "f2": ...
         *      }
         * }
         *
         * --------------- Non-Data Part
         * 2.1. Format 2-1:
         * {
         *      "0": ...,
         *      "1": ...
         * }
         * 2.2. Format 2-2:
         * {
         *      "f1": ...,
         *      "f2": ...,
         * }
         */
        JsonObject itRef = data;
        if (data.containsKey(VName.DATA)) {
            itRef = data.getJsonObject(VName.DATA);
        }

        /*
         * Setted data by index, it means we should capture following part in interface style
         *
         * public String method(@PathParam("name") String name,
         *                      @RequestParam("user") String user);
         * Here:
         * name argIndex = 0;
         * user argIndex = 1;
         *
         * We do not support more than 8 parameters in one method to have a good habit
         * to write readile code.
         */
        if (null == argIndex) {
            /*
             * Find the first value of type JsonObject / JsonArray ( Complex Smart workflow )
             */
            found = findSmart(itRef);
            if (Objects.isNull(found)) {
                /*
                 * Move reference and let found reference set
                 */
                found = itRef;
            }
        } else {
            return findByIndex(itRef, argIndex);
        }
        return found;
    }

    private static Object findByIndex(final JsonObject itPart, final Integer argIndex) {
        /*
         * Extract data by "argIndex", here
         * argIndex could be passed by developers or outer environment
         *
         * Here argIndex should not be null
         */
        return itPart.getValue(KWeb.MULTI.INDEXES.get(argIndex));
    }

    private static Object findSmart(final JsonObject itPart) {
        /*
         * Find first complex object reference
         * 1. The first value of JsonObject
         * 2. The first value of JsonArray
         */
        final Object body = itPart.fieldNames().stream()
            .filter(Objects::nonNull)
            .map(itPart::getValue)
            /*
             * Predicate to test whether value is JsonObject
             * If JsonObject, then find the first JsonObject as body
             */
            .filter(value -> value instanceof JsonObject || value instanceof JsonArray)
            .findFirst().orElse(null);
        if (Objects.nonNull(body)) {
            if (body instanceof JsonObject && Ut.isNil((JsonObject) body)) {
                return null;
            }
            if (body instanceof JsonArray && Ut.isNil((JsonArray) body)) {
                return null;
            }
        } else {
            return null;
        }
        return body;
    }
}
