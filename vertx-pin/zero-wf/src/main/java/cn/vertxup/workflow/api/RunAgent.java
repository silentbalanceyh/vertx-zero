package cn.vertxup.workflow.api;

import cn.vertxup.workflow.cv.HighWay;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import jakarta.ws.rs.BodyParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

/**
 * Refer actor part to get data format
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface RunAgent {
    // Testing Passed ---------------------------
    @POST
    @Path("/up/flow/start")
    @Address(HighWay.Do.FLOW_START)
    JsonObject start(@BodyParam JsonObject body);

    @PUT
    @Path("/up/flow/saving")
    @Address(HighWay.Do.FLOW_DRAFT)
    JsonObject draft(@BodyParam JsonObject body);

    // Todo for Testing -----------------------
    @PUT
    @Path("/up/flow/complete")
    @Address(HighWay.Do.FLOW_COMPLETE)
    JsonObject complete(@BodyParam JsonObject body);

    @PUT
    @Path("/up/flow/batch")
    @Address(HighWay.Do.FLOW_BATCH)
    JsonObject batch(@BodyParam JsonObject body);

    @PUT
    @Path("/up/flow/cancel")
    @Address(HighWay.Do.FLOW_CANCEL)
    JsonObject cancel(@BodyParam JsonObject body);

    @PUT
    @Path("/up/flow/close")
    @Address(HighWay.Do.FLOW_CLOSE)
    JsonObject close(@BodyParam JsonObject body);
}
