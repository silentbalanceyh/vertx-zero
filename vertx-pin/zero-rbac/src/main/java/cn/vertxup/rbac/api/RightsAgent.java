package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KWeb;
import jakarta.ws.rs.*;

@EndPoint
public interface RightsAgent {

    @Path("/api/groups")
    @GET
    @Address(Addr.Group.GROUP_SIGMA)
    JsonObject fetchGroups(@HeaderParam(KWeb.HEADER.X_SIGMA) String sigma);

    @Path("/role-perm/{roleId}")
    @PUT
    @Address(Addr.Role.ROLE_PERM_UPDATE)
    JsonArray updateRolePerm(@PathParam("roleId") String roleId,
                             @BodyParam JsonArray data);
}
