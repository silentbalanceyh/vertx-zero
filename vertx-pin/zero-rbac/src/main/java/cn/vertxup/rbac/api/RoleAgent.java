package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.BodyParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/api")
@EndPoint
public interface RoleAgent {

    @Path("/role-perm/{roleId}")
    @PUT
    @Address(Addr.Role.ROLE_PERM_UPDATE)
    JsonArray updateRolePerm(@PathParam("roleId") String roleId,
                              @BodyParam JsonArray data);
}
