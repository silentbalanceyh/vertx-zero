package io.vertx.mod.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.daos.RUserGroupDao;
import cn.vertxup.rbac.domain.tables.daos.RUserRoleDao;
import cn.vertxup.rbac.domain.tables.pojos.RUserGroup;
import cn.vertxup.rbac.domain.tables.pojos.RUserRole;
import io.aeon.experiment.specification.KQr;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

class TwineQr {

    static Future<JsonObject> normalize(final KQr qr, final JsonObject query) {
        // The original `criteria` from query part, fix Null Pointer Issue
        final JsonObject queryJ = query.copy();
        final JsonObject criteria = Ut.valueJObject(queryJ, Ir.KEY_CRITERIA);
        return normalize(criteria).compose(normalizeJ -> {
            // Qr Json
            final JsonObject condition;
            condition = Ux.whereAnd();
            condition.mergeIn(qr.getCondition());
            if (Ut.isNotNil(normalizeJ)) {
                // java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
                // Sub Query Tree Must not be EMPTY
                condition.put("$KQR$", normalizeJ);
            }
            queryJ.put(Ir.KEY_CRITERIA, condition);
            return Ux.future(queryJ);
        });
    }

    private static Future<JsonObject> normalize(final JsonObject criteria) {
        /*
         * 此处有两种格式
         * 1）criteriaJ 中直接包含了 roleId,i / roles,i / groupId,i / groups,i
         * 2）criteriaJ 中存在子条件，包含了 roleId,i / roles,i / groupId,i / groups.i
         * 深度计算
         */
        return normalizeJ(criteria).compose(normalized -> {
            final JsonObject resultJ = normalized.copy();
            final ConcurrentMap<String, Future<JsonObject>> loop = new ConcurrentHashMap<>();
            for (final String field : resultJ.fieldNames()) {
                final Object fieldV = resultJ.getValue(field);
                if (fieldV instanceof JsonObject) {
                    loop.put(field, normalize((JsonObject) fieldV));
                }
            }
            return Fn.combineM(loop).compose(mapped -> {
                mapped.forEach(resultJ::put);
                return Ux.future(resultJ);
            });
        });
    }

    private static Future<JsonObject> normalizeJ(final JsonObject criteriaJ) {
        return Ux.future(criteriaJ)
            .compose(TwineQr::normalizeG)
            .compose(TwineQr::normalizeR);
    }

    /*
     * 兼容处理：groups, groupId / roles, roleId 两对
     */
    private static Future<JsonObject> normalizeG(final JsonObject criteriaJ) {
        // groupId,i / groups,i
        final String fieldId = AuthKey.F_GROUP_ID + ",i";
        final String fieldV = KName.GROUPS + ",i";
        if (criteriaJ.containsKey(fieldId)
            || criteriaJ.containsKey(fieldV)) {
            final JsonArray value = valueBy(criteriaJ, fieldId, fieldV);
            return Ux.Jooq.on(RUserGroupDao.class).<RUserGroup>fetchInAsync(AuthKey.F_GROUP_ID, value)
                .compose(relation -> {
                    final Set<String> keySet = (relation.stream().map(RUserGroup::getUserId).collect(Collectors.toSet()));
                    // Replace field groupId,i / groups,i with key,i
                    return valueOut(criteriaJ, keySet, fieldId, fieldV);
                });
        } else {
            return Ux.future(criteriaJ);
        }
    }

    private static Future<JsonObject> normalizeR(final JsonObject criteriaJ) {
        // roleId,i / roles,i
        final String fieldId = AuthKey.F_ROLE_ID + ",i";
        final String fieldV = KName.ROLES + ",i";
        if (criteriaJ.containsKey(fieldId)
            || criteriaJ.containsKey(fieldV)) {
            final JsonArray value = valueBy(criteriaJ, fieldId, fieldV);
            return Ux.Jooq.on(RUserRoleDao.class).<RUserRole>fetchInAsync(AuthKey.F_ROLE_ID, value)
                .compose(relation -> {
                    final Set<String> keySet = (relation.stream().map(RUserRole::getUserId).collect(Collectors.toSet()));
                    // Replace field roleId,i / roles,i with key,i
                    return valueOut(criteriaJ, keySet, fieldId, fieldV);
                });
        } else {
            return Ux.future(criteriaJ);
        }
    }

    private static Future<JsonObject> valueOut(final JsonObject criteriaJ, final Set<String> keySet,
                                               final String... removeFields) {
        final String fieldRep = KName.KEY + ",i";
        // role / group
        final JsonArray keyArr = criteriaJ.getJsonArray(fieldRep, new JsonArray());
        if (keyArr.isEmpty()) {
            // keyArr is null, role / group first extracting
            if (!keySet.isEmpty()) {
                // add user key in our condition
                keySet.forEach(keyArr::add);
            }
            // forbidden condition
            // criteriaJ.put(fieldRep, keyArr);
        } else {
            // add user key in our condition
            keySet.forEach(keyArr::add);
        }
        criteriaJ.put(fieldRep, keyArr);
        Arrays.stream(removeFields).forEach(criteriaJ::remove);
        return Ux.future(criteriaJ);
    }

    private static JsonArray valueBy(final JsonObject criteriaJ, final String fieldId, final String fieldV) {
        final JsonArray value = new JsonArray();
        if (criteriaJ.containsKey(fieldId)) {
            value.addAll(criteriaJ.getJsonArray(fieldId));
            criteriaJ.remove(fieldId);
        }
        if (criteriaJ.containsKey(fieldV)) {
            value.addAll(criteriaJ.getJsonArray(fieldV));
            criteriaJ.remove(fieldV);
        }
        return value;
    }
}
