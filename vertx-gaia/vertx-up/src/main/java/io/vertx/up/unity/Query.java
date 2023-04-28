package io.vertx.up.unity;

import io.horizon.atom.Kv;
import io.horizon.eon.VString;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KWeb;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * 查询条件专用算法，用来合并两个维度的条件
 * 1. irV: 合并 projection
 * 2. irH: 合并 criteria
 */
final class Query {
    private static final Annal LOGGER = Annal.get(Query.class);

    /*
     * Move `RibIr` code to current method, complex algorithm to combine qr parts directly
     * 1. irV / irH:  3 arguments -> Qr Full
     * 2. irV / irH:  2 arguments -> Qr Node ( criteria, projection )
     */
    // ------------------- H ----------------------

    static JsonObject irQH(final JsonObject query, final String field, final Object value) {
        Objects.requireNonNull(query);
        final JsonObject original = Ut.valueJObject(query, Qr.KEY_CRITERIA);
        query.put(Qr.KEY_CRITERIA, irH(original, field, value));
        return query;
    }

    static JsonObject irH(final JsonObject original, final String field, final Object value) {
        final JsonObject originalJ = Ut.valueJObject(original);
        // 如果 value 本身是 JsonObject
        if (value instanceof JsonObject) {
            // 左右合并
            final Kv<String, String> kv = Kv.create(KWeb.ARGS.TREE_L, field);
            return ir(originalJ, (JsonObject) value, Qr.Connector.AND, kv);
        } else {
            // 直接合并（加 And）
            if (!originalJ.containsKey(VString.EMPTY)) {
                originalJ.put(VString.EMPTY, Boolean.TRUE);
            }
            originalJ.put(field, value);
            return originalJ;
        }
    }

    static JsonObject irH(final JsonObject original, final JsonObject criteria) {
        // 此处必须使用副本
        final JsonObject originalJ = Ut.valueJObject(original, true);
        final JsonObject criteriaJ = Ut.valueJObject(criteria);
        // Combine Result
        final JsonObject result = new JsonObject();
        if (irNil(original) && irNil(criteria)) {
            // 左右都不包含条件
            return result;
        }
        if (irNil(originalJ)) {
            // 直接取新条件
            return result.mergeIn(criteriaJ, true);
        }
        if (irNil(criteriaJ)) {
            // 直接取旧条件
            return result.mergeIn(originalJ, true);
        }
        // 新旧都不为空
        final Kv<String, String> kv = Kv.create(KWeb.ARGS.TREE_L, KWeb.ARGS.TREE_R);
        return irAnd(originalJ, criteriaJ, kv);
    }

    static JsonObject irQH(final JsonObject query, final JsonObject criteria, final boolean clear) {
        Objects.requireNonNull(query);
        if (clear) {
            /* Overwrite Mode */
            query.put(Qr.KEY_CRITERIA, criteria);
        } else {
            /* Combine Mode */
            final JsonObject originalJ = Ut.valueJObject(query, Qr.KEY_CRITERIA);
            query.put(Qr.KEY_CRITERIA, irH(originalJ, criteria));
        }
        LOGGER.info("[Qr] Criteria: \n{0}", query.encodePrettily());
        return query;
    }

    private static JsonObject irAnd(final JsonObject originalJ, final JsonObject criteriaJ,
                                    final Kv<String, String> nodes) {
        return ir(originalJ, criteriaJ, Qr.Connector.AND, nodes);
    }

    private static JsonObject irOr(final JsonObject originalJ, final JsonObject criteriaJ,
                                   final Kv<String, String> nodes) {
        return ir(originalJ, criteriaJ, Qr.Connector.OR, nodes);
    }

    private static JsonObject ir(final JsonObject originalJ, final JsonObject criteriaJ,
                                 final Qr.Connector connectorL, final Kv<String, String> kv) {
        // 在 originalJ 中追加条件：AND
        originalJ.put(VString.EMPTY, Qr.Connector.AND == connectorL);
        if (irOne(criteriaJ)) {
            // 单条件，直接将条件追加（此时不论符号）
            criteriaJ.fieldNames()
                .forEach(field -> originalJ.put(field, criteriaJ.getValue(field)));
            return originalJ;
        } else {
            // 多条件，需检查对端符号
            final Boolean isAnd = criteriaJ.getBoolean(VString.EMPTY, Boolean.FALSE);
            final Qr.Connector connectorR = isAnd ? Qr.Connector.AND : Qr.Connector.OR;
            if (connectorL == connectorR) {
                // 两边符号相同，Linear合并
                // L AND R ( r1 = v1, r2 = v2 )
                criteriaJ.fieldNames()
                    .forEach(field -> originalJ.put(field, criteriaJ.getValue(field)));
                return originalJ;
            } else {
                // 符号不同，Tree合并
                // L AND R
                final JsonObject result = new JsonObject();
                result.put(VString.EMPTY, Boolean.TRUE);
                result.put(kv.key(), originalJ);
                result.put(kv.value(), criteriaJ);
                return result;
            }
        }
    }

    // ------------------- V ----------------------
    static JsonObject irQV(final JsonObject query, final JsonArray projection, final boolean clear) {
        Objects.requireNonNull(query);
        if (clear) {
            /* Overwrite Mode */
            query.put(Qr.KEY_PROJECTION, projection.copy());
        } else {
            /* Combine */
            final JsonArray original = Ut.valueJArray(query, Qr.KEY_PROJECTION);
            query.put(Qr.KEY_PROJECTION, irV(original, projection));
        }
        LOGGER.info("[Qr] Projection: \n{0}", query.encodePrettily());
        return query;
    }

    /*
     * Combine ( original, updated )
     */
    static JsonArray irV(final JsonArray original, final JsonArray projection) {
        final JsonArray originalA = Ut.valueJArray(original);
        final JsonArray projectionA = Ut.valueJArray(projection);

        // Original Set Conversation
        final Set<String> originalSet = originalA.stream()
            .filter(item -> item instanceof String)
            .map(item -> (String) item)
            .collect(Collectors.toSet());

        // Add New from
        projectionA.stream()
            .filter(item -> item instanceof String)
            .map(item -> (String) item)
            .forEach(originalSet::add);
        // Returned to Combined
        return Ut.toJArray(originalSet);
    }

    static boolean irOne(final JsonObject condition) {
        final JsonObject normalized = condition.copy();
        normalized.remove(VString.EMPTY);
        return 1 == normalized.fieldNames().size();
    }

    static boolean irNil(final JsonObject condition) {
        if (Ut.isNil(condition)) {
            return true;
        } else {
            final JsonObject normalized = condition.copy();
            normalized.remove(VString.EMPTY);
            return Ut.isNil(normalized);
        }
    }

    static boolean irAnd(final JsonObject condition) {
        if (!condition.containsKey(VString.EMPTY)) {
            // Default: OR
            return false;
        }
        // true for AND
        // false for OR
        return condition.getBoolean(VString.EMPTY, Boolean.FALSE);
    }
}
