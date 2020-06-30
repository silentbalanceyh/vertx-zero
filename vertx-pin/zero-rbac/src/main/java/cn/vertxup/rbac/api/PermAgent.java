package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.ID;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@EndPoint
@Path("/api")
public interface PermAgent {

    @Path("/permission/groups/by/sigma")
    @GET
    @Address(Addr.Authority.PERMISSION_GROUP)
    JsonArray calculate(@HeaderParam(ID.Header.X_SIGMA) String sigma);
}
