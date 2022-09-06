package cn.vertxup.workflow.api.legacy;

import cn.vertxup.workflow.cv.HighWay;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface TodoAgent {
    /*
     * Get WTodo By Id
     */
    @Path("/todo/:key")
    @GET
    @Address(HighWay.Todo.BY_ID)
    JsonObject byId(@PathParam("key") String key);
}
