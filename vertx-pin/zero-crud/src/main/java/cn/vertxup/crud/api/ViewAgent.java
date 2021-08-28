package cn.vertxup.crud.api;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Orders;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/*
 * Http Method: Get
 */
@EndPoint
@Path("/api")
public interface ViewAgent {
    @GET
    @Path("/columns/{actor}/full")
    @Address(Addr.Get.COLUMN_FULL)
    @Adjust(Orders.MODULE)
    JsonArray getFull(@PathParam(KName.ACTOR) String actor,
                      @QueryParam(KName.VIEW) String view,
                      @QueryParam(KName.MODULE) String module);

    @GET
    @Path("/columns/{actor}/my")
    @Address(Addr.Get.COLUMN_MY)
    @Adjust(Orders.MODULE)
    JsonArray getMy(@PathParam(KName.ACTOR) String actor,
                    @QueryParam(KName.VIEW) String view,
                    @QueryParam(KName.MODULE) String module);
}
