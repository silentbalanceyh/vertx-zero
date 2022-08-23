package io.vertx.tp.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.daos.RUserRoleDao;
import cn.vertxup.rbac.domain.tables.pojos.RUserRole;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.ke.secure.Tie;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TieRole implements Tie<String, JsonArray> {
    @Override
    public Future<JsonArray> identAsync(final JsonObject userJ) {
        final String userKey = Ut.valueString(userJ, KName.KEY);
        return Ke.umALink(AuthKey.F_USER_ID, userKey, RUserRoleDao.class, RUserRole::getPriority)
            .compose(result -> {
                final JsonArray roles = new JsonArray();
                result.stream().map(RUserRole::getRoleId).forEach(roles::add);
                return Ux.future(roles);
            });
    }

    @Override
    public Future<JsonArray> identAsync(final String userKey) {
        // Fetch related role
        Sc.infoAuth(this.getClass(), AuthMsg.RELATION_USER_ROLE, userKey);
        return Ke.umALink(AuthKey.F_USER_ID, userKey, RUserRoleDao.class);
    }

    /*
     * updatedJ
     * {
     *     "...",
     *     "roles": []
     * }
     */
    @Override
    @SuppressWarnings("all")
    public Future<JsonArray> identAsync(final String userKey, final JsonObject userJ) {
        // Update Related Roles
        final JsonArray roles = Ut.valueJArray(userJ, KName.ROLES);
        if (Ut.isNil(roles)) {
            // Execute this branch when only update user information
            return Ux.futureA();
        }
        final JsonObject conditionJ = new JsonObject()
            .put(AuthKey.F_USER_ID, userKey);
        // Remove & Insert
        final UxJooq jq = Ux.Jooq.on(RUserRoleDao.class);
        /* Delete Related Roles */
        return jq.deleteByAsync(conditionJ).compose(nil -> {
            /* Insert Related Roles */
            final List<String> roleIds = roles.getList();
            final List<RUserRole> inserted = roleIds.stream()
                .map(roleId -> new RUserRole()
                    .setUserId(userKey)
                    .setRoleId(roleId)
                    .setPriority(roleIds.indexOf(roleId)))
                .collect(Collectors.toList());
            return jq.insertAsync(inserted);
        }).compose(nil -> Ux.future(roles));
    }
}
