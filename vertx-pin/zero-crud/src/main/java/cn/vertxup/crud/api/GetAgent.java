package cn.vertxup.crud.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.EndPoint;
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
public interface GetAgent {

    @GET
    @Path("/{actor}/{key}")
    @Address(Addr.Get.BY_ID)
    @Adjust(Orders.MODULE)
    JsonObject getById(@PathParam("actor") String actor,
                       @PathParam("key") String key);

    @GET
    @Path("/columns/{actor}/full")
    @Address(Addr.Get.COLUMN_FULL)
    @Adjust(Orders.MODULE)
    JsonArray getFull(@PathParam("actor") String actor,
                      @QueryParam("module") String module);

    @GET
    @Path("/columns/{actor}/my")
    @Address(Addr.Get.COLUMN_MY)
    @Adjust(Orders.MODULE)
    JsonArray getMy(@PathParam("actor") String actor);

    @GET
    @Path("/{actor}/by/sigma")
    @Address(Addr.Get.BY_SIGMA)
    @Adjust(Orders.MODULE)
    JsonArray getAll(@PathParam("actor") String actor);
}
