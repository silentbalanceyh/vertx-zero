package cn.vertxup.crud.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

/*
 * Http Method: Get
 */
@EndPoint
@Path("/api")
public interface GetAgent {

    @GET
    @Path("/{actor}/{key}")
    @Address(Addr.Get.BY_ID)
    @Adjust(KWeb.ORDER.MODULE)
    JsonObject getById(@PathParam("actor") String actor,
                       @PathParam(KName.KEY) String key);

    @GET
    @Path("/{actor}/by/sigma")
    @Address(Addr.Get.BY_SIGMA)
    @Adjust(KWeb.ORDER.MODULE)
    JsonArray getAll(@PathParam("actor") String actor,
                     @QueryParam(KName.MODULE) String module);
}
