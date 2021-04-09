package io.vertx.tp.modular.ray;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.reference.DataQRule;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Record;
import io.vertx.up.commune.element.JAmb;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Reference Result Processor
 *
 * ### 1. Intro
 *
 * This class could combine the results based on the `rule` that defined.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RayResult {
    /**
     * Combine single record based on defined code logical
     *
     * @param record  {@link io.vertx.up.commune.Record} Result records
     * @param ambMap  {@link java.util.concurrent.ConcurrentMap} Reference data map
     * @param ruleMap {@link java.util.concurrent.ConcurrentMap} Reference rule map
     *
     * @return {@link io.vertx.up.commune.Record}
     */
    static Record combine(final Record record, final ConcurrentMap<String, JAmb> ambMap,
                          final ConcurrentMap<String, DataQRule> ruleMap) {
        ambMap.forEach((field, each) -> {
            final DataQRule rule = ruleMap.get(field);
            combine(record, field, each, rule);
        });
        return record;
    }

    /**
     * Combine multi record baseed on defined code logical
     *
     * @param records {@link io.vertx.up.commune.Record}[] Result records
     * @param ambMap  {@link java.util.concurrent.ConcurrentMap} Reference data map
     * @param ruleMap {@link java.util.concurrent.ConcurrentMap} Reference rule map
     *
     * @return {@link io.vertx.up.commune.Record}[]
     */
    static Record[] combine(final Record[] records,
                            final ConcurrentMap<String, JAmb> ambMap,
                            final ConcurrentMap<String, DataQRule> ruleMap) {
        ambMap.forEach((field, each) -> {
            final DataQRule rule = ruleMap.get(field);
            if (Objects.nonNull(rule)) {
                combine(records, field, each, rule);
            }
        });
        return records;
    }

    /**
     * Element based on single attribute calculation.
     *
     * @param records {@link io.vertx.up.commune.Record}[] Result records
     * @param field   {@link java.lang.String} Input attribute name
     * @param amb     {@link io.vertx.up.commune.element.JAmb} The data map stored reference data
     * @param rule    {@link io.vertx.tp.atom.modeling.reference.DataQRule} The rule applied to current attribute
     */
    private static void combine(final Record[] records, final String field, final JAmb amb, final DataQRule rule) {
        final List<Kv<String, String>> ruleJoined = rule.joined();
        final Boolean single = amb.isSingle();
        if (Objects.nonNull(single) && !single) {
            /*
             * Only valid for batch operations
             * also include empty json array
             */
            final JAmb normalized = RayRuler.required(amb, rule);
            if (Objects.nonNull(normalized)) {
                final JsonArray source = normalized.dataT();
                /*
                 * Element find by joined
                 */
                final Class<?> returnType = rule.type();
                /*
                 * Grouped by returnType
                 */
                final ConcurrentMap<String, JAmb> grouped = RayRuler.group(source, ruleJoined, returnType);
                /*
                 * Process records
                 */
                Arrays.stream(records).forEach(record -> {
                    /*
                     * Process joined data
                     */
                    final String joinedKey = RayRuler.joinedKey(record, ruleJoined);
                    if (grouped.containsKey(joinedKey)) {
                        /*
                         * Extract data
                         */
                        final JAmb item = grouped.get(joinedKey);
                        combine(record, field, item, rule);
                    }
                });
            }
        }
    }

    /**
     * Single record checking for data processing.
     *
     * @param record {@link io.vertx.up.commune.Record} Result records
     * @param field  {@link java.lang.String} Input attribute name
     * @param amb    {@link io.vertx.up.commune.element.JAmb} The data map stored reference data
     * @param rule   {@link io.vertx.tp.atom.modeling.reference.DataQRule} The rule applied to current attribute
     */
    private static void combine(final Record record, final String field, final JAmb amb, final DataQRule rule) {
        final Boolean single = amb.isSingle();
        if (Objects.nonNull(single) && Objects.nonNull(record)) {
            if (single) {
                // Required
                final JAmb normalized = RayRuler.required(amb, rule);
                if (Objects.nonNull(normalized)) {
                    final JsonObject data = amb.dataT();
                    record.add(field, data);
                }
            } else {
                // Required
                final JAmb normalized = RayRuler.required(amb, rule);
                if (Objects.nonNull(normalized)) {
                    final JsonArray data = amb.dataT();
                    record.add(field, data);
                }
            }
        }
    }
}
