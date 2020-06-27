package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.BodyParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/api")
@EndPoint
public interface ViewAgent {

    @Path("/view/{ownerType}/{key}")
    @PUT
    @Address(Addr.View.VIEW_UPDATE_BY_TYPE)
    JsonObject updateViewByType(@PathParam("ownerType") String ownerType,
                                @PathParam("key") String key,
                                @BodyParam JsonObject data);
}
