package cn.vertxup.crud.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.commune.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
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
    @Adjust(KWeb.ORDER.MODULE)
    JsonObject update(@PathParam("actor") String actor,
                      @PathParam(KName.KEY) String key,
                      @BodyParam JsonObject data);

    @PUT
    @Path("/batch/{actor}/update")
    @Address(Addr.Put.BATCH)
    @Adjust(KWeb.ORDER.MODULE)
    JsonArray updateBatch(@PathParam("actor") String actor,
                          @BodyParam JsonArray dataArray,
                          @QueryParam(KName.MODULE) String module);

    @PUT
    @Path("/columns/{actor}/my")
    @Address(Addr.Put.COLUMN_MY)
    @Adjust(KWeb.ORDER.MODULE)
    JsonArray getMy(@PathParam("actor") String actor,
                    @BodyParam JsonObject viewData,
                    @QueryParam(KName.MODULE) String module,
                    @PointParam(KName.VIEW) Vis view);
}
