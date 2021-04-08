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
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RayResult {

    static Record combine(final Record record, final ConcurrentMap<String, JAmb> ambMap,
                          final ConcurrentMap<String, DataQRule> ruleMap) {
        ambMap.forEach((field, each) -> {
            final DataQRule rule = ruleMap.get(field);
            combine(record, field, each, rule);
        });
        return record;
    }

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
                 * 记录处理
                 */
                Arrays.stream(records).forEach(record -> {
                    /*
                     * 记录 key
                     */
                    final String joinedKey = RayRuler.joinedKey(record, ruleJoined);
                    if (grouped.containsKey(joinedKey)) {
                        /*
                         * 合并记录属性集
                         */
                        final JAmb item = grouped.get(joinedKey);
                        combine(record, field, item, rule);
                    }
                });
            }
        }
    }


    static void combine(final Record record, final String field, final JAmb amb, final DataQRule rule) {
        // 读取 single
        final Boolean single = amb.isSingle();
        if (Objects.nonNull(single) && Objects.nonNull(record)) {
            if (single) {
                final JAmb normalized = RayRuler.required(amb, rule);
                if (Objects.nonNull(normalized)) {
                    // 必须该行，类型设定
                    final JsonObject data = amb.dataT();
                    record.add(field, data);
                }
            } else {
                final JAmb normalized = RayRuler.required(amb, rule);
                if (Objects.nonNull(normalized)) {
                    // 必须该行，类型设定
                    final JsonArray data = amb.dataT();
                    record.add(field, data);
                }
            }
        }
    }
}
