package cn.vertxup.crud.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.Orders;

import javax.ws.rs.*;

/*
 * HTTP Method: Put
 */
@EndPoint
@Path("/api")
public interface PutAgent {
    @PUT
    @Path("/{actor}/{key}")
    @Address(Addr.Put.BY_ID)
    @Adjust(Orders.MODULE)
    JsonObject update(@PathParam("actor") String actor,
                      @PathParam("key") String key,
                      @BodyParam JsonObject data);

    @PUT
    @Path("/batch/{actor}/update")
    @Address(Addr.Put.BATCH)
    @Adjust(Orders.MODULE)
    JsonArray updateBatch(@PathParam("actor") String actor,
                          @QueryParam("module") String module,
                          @BodyParam JsonArray dataArray);

    @PUT
    @Path("/columns/{actor}/my")
    @Address(Addr.Put.COLUMN_MY)
    @Adjust(Orders.MODULE)
    JsonArray getMy(@PathParam("actor") String actor,
                    @QueryParam("view") String view,
                    @QueryParam("module") String module,
                    @BodyParam JsonObject viewData);
}
