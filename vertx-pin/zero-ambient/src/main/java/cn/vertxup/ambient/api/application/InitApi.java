package cn.vertxup.ambient.api.application;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.ID;

import javax.ws.rs.*;

@EndPoint
@Path("/api")
public interface InitApi {

    @Path("/app/init")
    @POST
    @Address(Addr.Init.INIT)
    JsonObject init(@HeaderParam(ID.Header.X_APP_ID) String appId,
                    @BodyParam JsonObject body);

    @Path("/app/prepare/{name}")
    @POST
    @Address(Addr.Init.PREPARE)
    JsonObject prepare(@PathParam("name") String name);

    @Path("/app/connect")
    @POST
    @Address(Addr.Init.CONNECT)
    String connect(@BodyParam JsonObject body);

    /*
     * Document Management Platform
     * 1. Fetch Category by `zero.directory`.
     * 2. Capture the component of these three and call `ExIo` interface ( Service Loader )
     * 3. Create all folders based on components defined.
     */
    @Path("/document/start")
    @POST
    @Address(Addr.Init.DOCUMENT)
    String document(@HeaderParam(ID.Header.X_APP_ID) String appId);
}
