package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.ID;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;

@EndPoint
public interface GroupAgent {

    @Path("/api/groups")
    @GET
    @Address(Addr.Group.GROUP_SIGMA)
    JsonObject fetchGroups(@HeaderParam(ID.Header.X_SIGMA) String sigma);

}
