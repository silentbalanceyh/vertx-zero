package cn.vertxup.psi.api;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.psi.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import jakarta.ws.rs.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface WhAgent {
    @POST
    @Path("/wh")
    @Address(Addr.WH_CREATE)
    JsonObject createAsync(@BodyParam JsonObject request);

    @GET
    @Path("/wh/:key")
    @Address(Addr.WH_READ)
    JsonObject readAsync(@PathParam(KName.KEY) String key);

    @DELETE
    @Path("/wh/:key")
    @Address(Addr.WH_DELETE)
    JsonObject removeAsync(@PathParam(KName.KEY) String key);

    @PUT
    @Path("/wh/:key")
    @Address(Addr.WH_UPDATE)
    JsonObject updateAsync(@PathParam(KName.KEY) String key,
                           @BodyParam JsonObject request);
}
