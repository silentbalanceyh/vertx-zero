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
     * 1. status as condition for different
     * 2. Queue Search Results
     */
    @POST
    @Path("/up/flow-queue/:request")
    @Address(HighWay.Queue.TASK_QUEUE)
    JsonObject fetchApproval(
        @BodyParam JsonObject body,
        @PathParam("request") Boolean request);

    /*
     * SELECT * FROM X_FLOW by code/instanceKey
     */
    @GET
    @Path("/up/flow-definition/:code")
    @Address(HighWay.Flow.BY_CODE)
    JsonObject fetchFlow(@PathParam(KName.CODE) String code);

    /*
     * Here are two mode
     * 1. when isPre = true, the workflow is not started
     * 2. when isPre = false ( Default ), standard to pick up the task
     */
    @POST
    @Path("/up/flow-form/:pre")
    @Address(HighWay.Queue.TASK_FORM)
    JsonObject fetchForm(@BodyParam JsonObject body,
                         @PathParam("pre") Boolean isPre);
}
