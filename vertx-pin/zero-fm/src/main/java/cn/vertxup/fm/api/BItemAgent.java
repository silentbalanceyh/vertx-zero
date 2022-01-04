package cn.vertxup.fm.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface BItemAgent {
    @GET
    @Path("/bills/order/:orderId")
    @Address(Addr.BillItem.FETCH_AGGR)
    JsonObject fetchItem(@PathParam("orderId") String orderId);

    /*
     * Settlement to read book with authorize information
     */
    @GET
    @Path("/books/order/:orderId")
    @Address(Addr.BillItem.FETCH_BOOK)
    JsonArray fetchBooks(@PathParam("orderId") String orderId);

    /*
     * Overwrite the api
     * /api/fm-book/:key
     * instead of CRUD normalized api here
     */
    @GET
    @Path("/fm-book/:key")
    @Address(Addr.BillItem.FETCH_BOOK_BY_KEY)
    JsonObject fetchBook(@PathParam(KName.KEY) String key);
}
