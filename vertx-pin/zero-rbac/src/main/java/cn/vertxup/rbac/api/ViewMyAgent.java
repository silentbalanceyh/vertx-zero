package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import jakarta.ws.rs.*;

@Path("/api")
@EndPoint
public interface ViewMyAgent {

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

    @POST
    @Path("/view-p/existing")
    @Address(Addr.View.VIEW_P_EXISTING)
    Boolean pViewExisting(@BodyParam JsonObject params);

    @DELETE
    @Path("/view-p/:key")
    @Address(Addr.View.VIEW_P_DELETE)
    Boolean pViewDelete(@PathParam(KName.KEY) String key);

    @PUT
    @Path("/view-p/:key")
    @Address(Addr.View.VIEW_P_UPDATE)
    Boolean pViewById(@PathParam(KName.KEY) String key,
                      @BodyParam JsonObject params);

    @GET
    @Path("/view-p/:key")
    @Address(Addr.View.VIEW_P_BY_ID)
    Boolean pViewUpdate(@PathParam(KName.KEY) String key);


    @DELETE
    @Path("/batch/view-p/delete")
    @Address(Addr.View.VIEW_P_BATCH_DELETE)
    Boolean pViewsDelete(@BodyParam JsonArray keys);

    @POST
    @Path("/view-p")
    @Address(Addr.View.VIEW_P_ADD)
    Boolean pViewCreate(@BodyParam JsonObject params);
}
