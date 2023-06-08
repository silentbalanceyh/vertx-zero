package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.business.GroupStub;
import cn.vertxup.rbac.service.business.RightsStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import jakarta.inject.Inject;

@Queue
public class RightsActor {

    @Inject
    private transient GroupStub groupStub;
    @Inject
    private transient RightsStub setStub;

    @Address(Addr.Group.GROUP_SIGMA)
    public Future<JsonArray> fetchGroups(final String sigma) {
        return this.groupStub.fetchGroups(sigma);
    }

    @Address(Addr.Role.ROLE_PERM_UPDATE)
    public Future<JsonArray> updateRolePerm(final String roleId, final JsonArray data) {
        return this.setStub.saveRoles(roleId, data);
    }
}
