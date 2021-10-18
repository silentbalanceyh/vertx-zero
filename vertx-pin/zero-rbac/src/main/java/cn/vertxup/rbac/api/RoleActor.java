package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.business.RoleStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;

@Queue
public class RoleActor {

    @Inject
    private transient RoleStub roleStub;

    @Address(Addr.Role.ROLE_PERM_UPDATE)
    public Future<JsonArray> updateRolePerm(final String roleId, final JsonArray data) {
        return roleStub.updateRolePerm(roleId, data);
    }
}
