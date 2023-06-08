package cn.vertxup.battery.api;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.battery.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import jakarta.ws.rs.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface BagArgAgent {

    // ------------------- Bag Configuration --------------------
    /*
     * Configuration of BLOCK, here will read config data of BAG
     * -- The bag is two level
     *    -- 1). When bag is root
     *    -- 2). When bag is secondary level
     * -- This method will read config data based on `bagId`
     */
    @GET
    @Path("/bag/config/:key")
    @Address(Addr.Argument.BAG_ARGUMENT)
    JsonObject fetchBag(@PathParam(KName.KEY) String bagId);

    @GET
    @Path("/bag/data/:key")
    @Address(Addr.Argument.BAG_ARGUMENT_VALUE)
    JsonObject fetchBagData(@PathParam(KName.KEY) String bagId);

    @PUT
    @Path("/bag/config/:key")
    @Address(Addr.Argument.BAG_CONFIGURE)
    JsonObject saveBag(@PathParam(KName.KEY) String bagId,
                       @BodyParam JsonObject data);
}
