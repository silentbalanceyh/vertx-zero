package cn.vertxup.jet.api;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.cv.JtAddr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Critical routing management
 * 1. Metadata Analyzing for zero-framework
 * 2. Call service interface of zero to get all routing information
 * 3. Update routing on `RoutingCore` in zero framework
 *
 * Here are futures of these uri
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@EndPoint
@Path("/api")
public interface UriApi {

    @Path("routing/create")
    @POST
    @Address(JtAddr.Aeon.NEW_ROUTE)
    Boolean createUri(@BodyParam JsonObject body);
}
