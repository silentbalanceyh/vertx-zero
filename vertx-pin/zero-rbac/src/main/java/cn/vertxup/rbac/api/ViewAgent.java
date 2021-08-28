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
    /*
     * All interface is for DEFAULT view
     * instead of personal views for future usage
     */
    @Path("/view/:ownerType/:ownerId")
    @PUT
    @Address(Addr.View.VIEW_UPDATE_BY_TYPE)
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

    /*
     * View Add, DELETE, FETCH
     * 1. Add ( Provide name, title, projection )
     * 2. Delete ( By key )
     * 3. Fetch ( method, uri, sigma, type )
     */
    @POST
    @Path("/view-p/fetch")
    @Address(Addr.View.VIEW_P_BY_USER)
    JsonArray pViewByUser(@BodyParam JsonObject params);

    @DELETE
    @Path("/view-p/:key")
    @Address(Addr.View.VIEW_P_DELETE)
    Boolean pViewDelete(@PathParam("key") String key);

    @PUT
    @Path("/view-p/:key")
    @Address(Addr.View.VIEW_P_UPDATE)
    Boolean pViewById(@PathParam("key") String key,
                      @BodyParam JsonObject params);

    @GET
    @Path("/view-p/:key")
    @Address(Addr.View.VIEW_P_BY_ID)
    Boolean pViewUpdate(@PathParam("key") String key);


    @DELETE
    @Path("/batch/view-p/delete")
    @Address(Addr.View.VIEW_P_BATCH_DELETE)
    Boolean pViewsDelete(@BodyParam JsonArray keys);

    @POST
    @Path("/view-p")
    @Address(Addr.View.VIEW_P_ADD)
    Boolean pViewCreate(@BodyParam JsonObject params);
}
