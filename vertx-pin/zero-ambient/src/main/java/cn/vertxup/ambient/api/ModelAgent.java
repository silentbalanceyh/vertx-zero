package cn.vertxup.ambient.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.ID;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api")
public interface ModelAgent {
    @Path("/module")
    @GET
    @Address(Addr.Module.BY_NAME)
    JsonObject moduleByName(@HeaderParam(ID.Header.X_APP_ID) String appId,
                            @QueryParam("entry") String entry);

    @Path("/model")
    @GET
    @Address(Addr.Module.IDENTIFIERS)
    JsonArray models(@HeaderParam(ID.Header.X_SIGMA) String appId);
}
