package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import jakarta.ws.rs.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface ResourceAgent {

    @Path("/resource/search")
    @POST
    @Address(Addr.Authority.RESOURCE_SEARCH)
    JsonArray searchAsync(@BodyParam JsonObject query);

    @Path("/resource/{key}")
    @GET
    @Address(Addr.Authority.RESOURCE_GET_CASCADE)
    JsonObject getResourceById(@PathParam("key") String key);

    @Path("/resource")
    @POST
    @Address(Addr.Authority.RESOURCE_ADD_CASCADE)
    JsonObject addResource(@BodyParam JsonObject data);

    @Path("/resource/{key}")
    @PUT
    @Address(Addr.Authority.RESOURCE_UPDATE_CASCADE)
    JsonObject updateResourceById(@PathParam("key") String key,
                                  @BodyParam JsonObject data);

    @Path("resource/{key}")
    @DELETE
    @Address(Addr.Authority.RESOURCE_DELETE_CASCADE)
    Boolean deleteResourceById(@PathParam("key") String key);
}
