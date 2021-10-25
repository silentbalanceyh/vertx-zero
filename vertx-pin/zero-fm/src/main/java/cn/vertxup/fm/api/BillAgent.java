package cn.vertxup.fm.api;

import com.google.gson.JsonObject;
import io.vertx.tp.fm.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface BillAgent {
    /*
     {
         "key": "primary key",
         "name": "name - The title",
         "code/serial": "The line number",
         "type": "bill type",
         "category": "bill category",
         "amount": "amount data",
         "income": "true for consume, false for pay",
         "comment": "",
         "orderId": "",
         "bookId": "",
         "manualNo": "",
         "payTermId": "",
         "preAuthorize": {
             "code/serial": "Calculated",
             "amount": "",
             "comment": "",
             "expiredAt": "",
             "bankName": "",
             "bankCard": "",
             "orderId": "",
             "billId": ""
         }
     }
     */
    @POST
    @Path("/bill/pre")
    @Address(Addr.Bill.IN_PRE)
    JsonObject inPre(@BodyParam JsonObject data);
}
