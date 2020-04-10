package cn.vertxup.ambient.api;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface TodoAgent {
    /*
     * Get XTodo By Id
     */
    @Path("todo/:key")
    @GET
    @Address(Addr.Todo.BY_ID)
    JsonObject byId(@PathParam("key") String key);
}
