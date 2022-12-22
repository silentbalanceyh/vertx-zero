package cn.vertxup.crud.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Orders;

import jakarta.ws.rs.*;

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
                      @PathParam(KName.KEY) String key,
                      @BodyParam JsonObject data);

    @PUT
    @Path("/batch/{actor}/update")
    @Address(Addr.Put.BATCH)
    @Adjust(Orders.MODULE)
    JsonArray updateBatch(@PathParam("actor") String actor,
                          @BodyParam JsonArray dataArray,
                          @QueryParam(KName.MODULE) String module);

    @PUT
    @Path("/columns/{actor}/my")
    @Address(Addr.Put.COLUMN_MY)
    @Adjust(Orders.MODULE)
    JsonArray getMy(@PathParam("actor") String actor,
                    @BodyParam JsonObject viewData,
                    @QueryParam(KName.MODULE) String module,
                    @PointParam(KName.VIEW) Vis view);
}
