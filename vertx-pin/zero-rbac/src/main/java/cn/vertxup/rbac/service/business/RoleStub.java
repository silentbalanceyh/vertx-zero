package cn.vertxup.rbac.service.business;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

public interface RoleStub {
    /**
     * Update role perm relation information
     */
    Future<JsonArray> updateRolePerm(String roleId, JsonArray data);

    /*
     * delete by role id
     */
    Future<Boolean> deleteByRoleId(String roleId);
}
