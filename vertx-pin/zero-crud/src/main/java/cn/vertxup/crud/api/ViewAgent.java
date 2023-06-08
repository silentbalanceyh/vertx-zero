package cn.vertxup.crud.api;

import io.vertx.core.json.JsonArray;
import io.vertx.mod.crud.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.commune.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import jakarta.ws.rs.*;

/*
 * Http Method: Get
 */
@EndPoint
@Path("/api")
public interface ViewAgent {
    @GET
    @Path("/columns/{actor}/full")
    @Address(Addr.Get.COLUMN_FULL)
    @Adjust(KWeb.ORDER.MODULE)
    JsonArray getFull(@PathParam(KName.ACTOR) String actor,
                      @QueryParam(KName.MODULE) String module,
                      @PointParam(KName.VIEW) Vis view);

    @GET
    @Path("/columns/{actor}/my")
    @Address(Addr.Get.COLUMN_MY)
    @Adjust(KWeb.ORDER.MODULE)
    JsonArray getMy(@PathParam(KName.ACTOR) String actor,
                    @QueryParam(KName.MODULE) String module,
                    @PointParam(KName.VIEW) Vis view);
}
