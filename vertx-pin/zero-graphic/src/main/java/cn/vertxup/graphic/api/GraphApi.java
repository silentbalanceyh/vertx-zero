package cn.vertxup.graphic.api;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.graphic.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api")
public interface GraphApi {

    @Path("/graphic/analyze/:key")
    @GET
    @Address(Addr.GRAPH_ANALYZE)
    JsonObject searchGraph(@PathParam("key") String key,
                           @QueryParam("graph") String graph,
                           @QueryParam("level") Integer level);

}
