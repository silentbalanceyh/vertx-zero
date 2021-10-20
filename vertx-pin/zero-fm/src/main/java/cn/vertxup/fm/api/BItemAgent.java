package cn.vertxup.fm.api;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface BItemAgent {
    @GET
    @Path("/bills/order/:orderId")
    @Address(Addr.BillItem.FETCH_AGGR)
    JsonObject fetchItem(@PathParam("orderId") String orderId,
                         @QueryParam("bookId") String bookId,
                         @QueryParam("type") String type);
}
