package cn.vertxup.ambient.api.application;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KWeb;
import jakarta.ws.rs.*;

@EndPoint
@Path("/api")
public interface InitApi {

    @Path("/app/init")
    @POST
    @Address(Addr.Init.INIT)
    JsonObject init(@HeaderParam(KWeb.HEADER.X_APP_ID) String appId,
                    @BodyParam JsonObject body);

    @Path("/app/prepare/{name}")
    @POST
    @Address(Addr.Init.PREPARE)
    JsonObject prepare(@PathParam("name") String name);

    @Path("/app/connect")
    @POST
    @Address(Addr.Init.CONNECT)
    String connect(@BodyParam JsonObject body);
}
