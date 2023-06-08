package cn.vertxup.fm.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.fm.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import jakarta.ws.rs.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface SettleAgent {
    /*
     * Settlement
     {
        "relatedId": "85f2fe43-78bb-46ec-9273-50a5532d9d82",
        "book": [
            "73339856-1cd4-439c-a3c2-b790ab5d7f76"      // 只传入需关闭的账本
        ],
        "finished": true,
        "payed": true,
        "amount": 1128,
        "rounded": "HALF",
        "signName": "账本一",
        "signMobile": "15922611447",
        "items": [
            {
                "billType": "In",
                "billId": "b798c790-e790-493a-87ba-3c21b1314c7e",
                "bookId": "73339856-1cd4-439c-a3c2-b790ab5d7f76",
                "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                "active": true,
                "price": 127.88,
                "income": true,
                "number": "B21110114400004",
                "quantity": 1,
                "name": "场租",
                "amountTotal": 127.88,
                "code": "B21110114400004-01",
                "updatedAt": "2021-11-01T14:40:58",
                "payTermId": "902f6d2d-05bd-4716-8594-b8b6afb38246",
                "updatedBy": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
                "status": "Pending",
                "serial": "B21110114400004-01",
                "modelKey": "1105",
                "amount": 127.88,
                "language": "cn",
                "type": "In",
                "createdAt": "2021-11-01T14:40:58",
                "createdBy": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
                "key": "0c8c7563-f361-4582-8980-0375236c04d3",
                "billAmount": 127.88
            },
            {
                "billType": "GoodIn",
                "billId": "82970c6e-db65-4b68-a56a-fd9e2856121f",
                "bookId": "73339856-1cd4-439c-a3c2-b790ab5d7f76",
                "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                "active": true,
                "price": 100,
                "income": true,
                "number": "B21103121370003",
                "quantity": 10,
                "name": "米乐卡斯特",
                "amountTotal": 1000,
                "code": "B21103121370003-02",
                "updatedAt": "2021-10-31T21:37:50",
                "payTermId": "98b0e9c1-6c36-4d46-ac31-d9cb0d441202",
                "updatedBy": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
                "status": "Pending",
                "serial": "B21103121370003-02",
                "modelKey": "1105",
                "amount": 1000,
                "language": "cn",
                "type": "GoodIn",
                "createdAt": "2021-10-31T21:37:50",
                "createdBy": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
                "key": "044e420c-f903-48d3-8bfc-62ef444719bd",
                "billAmount": 2000
            }
        ],
        "payment": [
            {
                "amount": "528",
                "payMethod": "Cash",
                "payName": "账本一",
                "payMobile": "15922611447"
            },
            {
                "amount": "600",
                "payMethod": "AliPay",
                "payName": "账本一",
                "payMobile": "15922611447"
            }
        ]
    }
     */
    @Path("/settle/create/:runup")
    @POST
    @Address(Addr.Settle.UP_PAYMENT)
    JsonObject upPayment(@PathParam("runup") boolean isRunUp,
                         @BodyParam JsonObject body);

    /*
     * Unlock Authorize when settlement on `status`
     * From `Pending` to `Finished`
     */
    @PUT
    @Path("/settle/authorize/unlock")
    @Address(Addr.Settle.UNLOCK_AUTHORIZE)
    JsonObject unlockAuthorize(@BodyParam JsonArray authorize);

    /*
     * Saving the book information when settlement on book information
     * - checked
     * - checkedDesc
     * - exceed
     * - exceedDesc
     */
    @PUT
    @Path("/settle/book/finalize")
    @Address(Addr.Settle.UP_BOOK)
    JsonObject finalizeBook(@BodyParam JsonArray books);

    /*
     * settlementId for payment creation
     * {
     *      "cross":"true / false",
     *      "payment": []
     * }
     * payment + payment items
     * 1: 1 ( cross = true, size = 1)
     * 1: N ( cross = false, size > 1)
     */
    @Path("/payment/create")
    @POST
    @Address(Addr.Settle.PAY_CREATE)
    JsonObject paymentCreate(@BodyParam JsonObject payment);

    @Path("/payment/cascade/:key")
    @DELETE
    @Address(Addr.Settle.PAY_DELETE)
    JsonObject paymentDelete(@PathParam(KName.KEY) String key);
}
