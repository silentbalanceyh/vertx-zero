package cn.zeroup.macrocosm.api;

import cn.zeroup.macrocosm.cv.HighWay;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;

import javax.ws.rs.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface QueueAgent {
    /*
     * SELECT * FROM X_TODO WHERE CREATED_BY = ? ( user )
     */
    @POST
    @Path("/up/duty/created")
    @Address(HighWay.Queue.BY_CREATED)
    JsonObject fetchMyCreated(@BodyParam JsonObject body);

    /*
     * SELECT * FROM X_FLOW by code/instanceKey
     */
    @GET
    @Path("/up/workflow/:code")
    @Address(HighWay.Flow.BY_CODE)
    JsonObject fetchFlow(@PathParam(KName.CODE) String code);
}
