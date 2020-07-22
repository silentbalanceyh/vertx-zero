package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.*;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@EndPoint
@Path("/api")
public interface PermAgent {

    @Path("/permission/groups/by/sigma")
    @GET
    @Address(Addr.Authority.PERMISSION_GROUP)
    JsonArray calculate();

    @Path("/permission/definition/saving")
    @PUT
    @Address(Addr.Authority.PERMISSION_SAVING)
    JsonObject saveRelation(@BodyParam JsonObject body);

    @Path("/permission/by/role/:roleId")
    @GET
    @Address(Addr.Authority.PERMISSION_BY_ROLE)
    JsonArray fetchAsync(@PathParam("roleId") String roleId);
}
