package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.ID;

import javax.ws.rs.BodyParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@EndPoint
@Path("/api")
public interface ActionAgent {
    /*
     * RESTful Api stored in SEC_ACTION for all authorized, instead of
     * get api information from zero engine, here we provide another interface
     * to seek action for `/api/x-action/seek` here.
     *
     * Here are some spec situation for this interface
     */
    @POST
    @Path("action/seek")
    @Address(Addr.Authority.ACTION_SEEK)
    JsonArray searchApi(@HeaderParam(ID.Header.X_SIGMA) String sigma,
                        @BodyParam JsonObject params);
}
