package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.RRolePermDao;
import cn.vertxup.rbac.domain.tables.pojos.RRolePerm;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.stream.Collectors;

public class RoleService implements RoleStub {
    @Override
    public Future<JsonArray> updateRolePerm(final String roleId, final JsonArray data) {
        // 1. make up role-perm entity
        final List<RRolePerm> rolePerms = Ut.itJString(data)
                .filter(Ut::notNil)
                .map(perm -> new JsonObject().put(KName.Rbac.PERM_ID, perm).put(KName.Rbac.ROLE_ID, roleId))
                .map(rolePerm -> Ux.fromJson(rolePerm, RRolePerm.class))
                .collect(Collectors.toList());
        // 2. delete old ones and insert new ones
        return this.deleteByRoleId(roleId)
                .compose(result -> Ux.Jooq.on(RRolePermDao.class)
                        .insertAsync(rolePerms)
                        .compose(Ux::futureA)
                );
    }

    @Override
    public Future<Boolean> deleteByRoleId(final String roleId) {
        return Ux.Jooq.on(RRolePermDao.class)
                .deleteByAsync(new JsonObject().put(KName.Rbac.ROLE_ID, roleId));
    }
}
