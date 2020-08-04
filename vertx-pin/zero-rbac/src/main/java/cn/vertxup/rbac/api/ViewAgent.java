package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.*;

@Path("/api")
@EndPoint
public interface ViewAgent {

    @Path("/view/:ownerType/:ownerId")
    @PUT
    @Address(Addr.Authority.VIEW_UPDATE_BY_TYPE)
    JsonObject saveViews(@PathParam("ownerType") String ownerType,
                         @PathParam("ownerId") String key,
                         @BodyParam JsonArray data);

    @Path("/view/:ownerType/:ownerId")
    @POST
    @Address(Addr.Rule.FETCH_VIEWS)
    JsonObject fetchByKeys(@PathParam("ownerType") String ownerType,
                           @PathParam("ownerId") String ownerId,
                           @BodyParam JsonArray ids);

    @POST
    @Path("/visitant/:ownerType/:ownerId")
    @Address(Addr.Rule.FETCH_VISITANT)
    JsonObject fetchVisitant(@PathParam("ownerType") String ownerType,
                             @PathParam("ownerId") String ownerId,
                             @BodyParam JsonObject params);
}
