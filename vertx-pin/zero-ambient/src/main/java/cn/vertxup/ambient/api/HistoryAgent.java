package cn.vertxup.ambient.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface HistoryAgent {

    @Path("/history/:identifier/:key")
    @GET
    @Address(Addr.History.HISTORIES)
    Future<JsonArray> fetch(@PathParam("identifier") String identifier, @PathParam("key") String key);

    @Path("/history/:key")
    @GET
    @Address(Addr.History.HISTORY_ITEMS)
    Future<JsonArray> fetchItems(@PathParam("key") String key);
}
