package cn.zeroup.macrocosm.api.report;

import cn.zeroup.macrocosm.cv.HighWay;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;

import javax.ws.rs.*;

/**
 * Refer actor part to get data format
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface ReportAgent {
    // Testing Passed ---------------------------
    @POST
    @Path("/up/report/list")
    @Address(HighWay.Report.TICKET_LIST)
    JsonObject list(@BodyParam JsonObject body);

    @GET
    @Path("/up/report/activity/:key")
    @Address(HighWay.Report.TICKET_ACTIVITY)
    JsonObject fetchActivity(@PathParam(KName.KEY) String key);
}
