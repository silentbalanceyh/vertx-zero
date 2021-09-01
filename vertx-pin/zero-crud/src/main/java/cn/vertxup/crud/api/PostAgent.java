package cn.vertxup.crud.api;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Orders;

import javax.ws.rs.*;

/*
 * HTTP Method: POST
 */
@EndPoint
@Path("/api")
public interface PostAgent {
    /*
     * Pure Creating for different entity
     */
    @POST
    @Path("/{actor}")
    @Address(Addr.Post.ADD)
    @Adjust(Orders.MODULE)
    JsonObject create(@PathParam("actor") String actor,
                      @BodyParam JsonObject data);

    /*
     * Search Interface for JqTool Engine
     */
    @POST
    @Path("/{actor}/search")
    @Address(Addr.Post.SEARCH)
    @Adjust(Orders.MODULE)
    JsonObject search(@PathParam("actor") String actor,
                      @BodyParam JsonObject data,
                      @PointParam(KName.VIEW) Vis view,
                      @QueryParam(KName.MODULE) String module);

    /*
     * Existing/Missing Interface for Async Validation
     */
    @POST
    @Path("/{actor}/existing")
    @Address(Addr.Post.EXISTING)
    @Adjust(Orders.MODULE)
    Boolean existing(@PathParam("actor") String actor,
                     @BodyParam JsonObject criteria,
                     @QueryParam(KName.MODULE) String module);

    @POST
    @Path("/{actor}/missing")
    @Address(Addr.Post.MISSING)
    @Adjust(Orders.MODULE)
    Boolean missing(@PathParam("actor") String actor,
                    @BodyParam JsonObject criteria,
                    @QueryParam(KName.MODULE) String module);
}
