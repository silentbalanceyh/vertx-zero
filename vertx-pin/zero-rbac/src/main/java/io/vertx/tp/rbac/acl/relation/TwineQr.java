package io.vertx.tp.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.daos.RUserGroupDao;
import cn.vertxup.rbac.domain.tables.daos.RUserRoleDao;
import cn.vertxup.rbac.domain.tables.pojos.RUserGroup;
import cn.vertxup.rbac.domain.tables.pojos.RUserRole;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.specification.KQr;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class TwineQr {

    static Future<JsonObject> normalize(final KQr qr, final JsonObject query) {
        // The original `criteria` from query part, fix Null Pointer Issue
        final JsonObject queryJ = query.copy();
        final JsonObject criteria = Ut.valueJObject(queryJ, Qr.KEY_CRITERIA);
        return normalize(criteria).compose(normalizeJ -> {
            // Qr Json
            final JsonObject condition;
            condition = Ux.whereAnd();
            condition.mergeIn(qr.getCondition());
            if (Ut.notNil(normalizeJ)) {
                // java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
                // Sub Query Tree Must not be EMPTY
                condition.put("$KQR$", normalizeJ);
            }
            queryJ.put(Qr.KEY_CRITERIA, condition);
            return Ux.future(queryJ);
        });
    }

    private static Future<JsonObject> normalize(final JsonObject criteria) {
        final JsonObject criteriaJ = criteria.copy();
        final Set<String> combineSet = new HashSet<>();
        return Ux.future(combineSet)
                // Group
                .compose(keySet -> normalizeG(criteriaJ, keySet))
                // Role
                .compose(keySet -> normalizeR(criteriaJ, keySet))
                .compose(userKeys -> {
                    if (!userKeys.isEmpty()) {
                        criteriaJ.put("key,i", Ut.toJArray(userKeys));
                    }
                    return Ux.future(criteriaJ);
                });
    }

    /*
     * 兼容处理：groups, groupId / roles, roleId 两对
     */
    private static Future<Set<String>> normalizeG(final JsonObject criteriaJ, final Set<String> combineSet) {
        // groupId,i / groups,i
        final String fieldId = AuthKey.F_GROUP_ID + ",i";
        final String fieldV = KName.GROUPS + ",i";
        if (criteriaJ.containsKey(fieldId)
                || criteriaJ.containsKey(fieldV)) {
            final JsonArray value = valueBy(criteriaJ, fieldId, fieldV);
            return Ux.Jooq.on(RUserGroupDao.class).<RUserGroup>fetchInAsync(AuthKey.F_GROUP_ID, value)
                    .compose(relation -> {
                        combineSet.addAll(relation.stream().map(RUserGroup::getUserId).collect(Collectors.toSet()));
                        return Ux.future(combineSet);
                    });
        } else {
            return Ux.future(new HashSet<>());
        }
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

    private static Future<Set<String>> normalizeR(final JsonObject criteriaJ, final Set<String> combineSet) {
        // groupId,i / groups,i
        final String fieldId = AuthKey.F_ROLE_ID + ",i";
        final String fieldV = KName.ROLES + ",i";
        if (criteriaJ.containsKey(fieldId)
                || criteriaJ.containsKey(fieldV)) {
            final JsonArray value = valueBy(criteriaJ, fieldId, fieldV);
            return Ux.Jooq.on(RUserRoleDao.class).<RUserRole>fetchInAsync(AuthKey.F_ROLE_ID, value)
                    .compose(relation -> {
                        combineSet.addAll(relation.stream().map(RUserRole::getUserId).collect(Collectors.toSet()));
                        return Ux.future(combineSet);
                    });
        } else {
            return Ux.future(new HashSet<>());
        }
    }
}
