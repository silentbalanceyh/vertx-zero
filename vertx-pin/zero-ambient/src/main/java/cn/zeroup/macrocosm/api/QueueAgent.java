package cn.zeroup.macrocosm.api;

import cn.zeroup.macrocosm.cv.HighWay;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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
}
