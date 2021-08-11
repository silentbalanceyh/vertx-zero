package cn.vertxup.ambient.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.eon.KName;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.ID;

import javax.ws.rs.*;

@EndPoint
@Path("/api")
public interface ModelAgent {
    @Path("/module")
    @GET
    @Address(Addr.Module.BY_NAME)
    JsonObject moduleByName(@HeaderParam(ID.Header.X_APP_ID) String appId,
                            @QueryParam("entry") String entry);

    @Path("/model/fields/:identifier")
    @GET
    @Address(Addr.Module.MODEL_FIELDS)
    JsonArray modelAttributes(@PathParam(KName.IDENTIFIER) String identifier);

    @Path("/model")
    @GET
    @Address(Addr.Module.MODELS)
    JsonArray models(@HeaderParam(ID.Header.X_SIGMA) String appId);
}
