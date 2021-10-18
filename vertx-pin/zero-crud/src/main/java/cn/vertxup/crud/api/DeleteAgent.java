package cn.vertxup.crud.api;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Orders;

import javax.ws.rs.BodyParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/*
 * Http Method: DELETE
 */
@EndPoint
@Path("/api")
public interface DeleteAgent {
    @DELETE
    @Path("/{actor}/{key}")
    @Address(Addr.Delete.BY_ID)
    @Adjust(Orders.MODULE)
    Boolean delete(@PathParam("actor") String actor,
                   @PathParam(KName.KEY) String key);

    @DELETE
    @Path("/batch/{actor}/delete")
    @Address(Addr.Delete.BATCH)
    @Adjust(Orders.MODULE)
    Boolean deleteBatch(@PathParam("actor") String actor,
                        @BodyParam JsonArray data);
}
