package cn.vertxup.fm.api;

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
         "termId": "",
         "indent": "code definition for generation",
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

    /*
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
         "termId": "",
         "indent": "code definition for generation",
     */
    @POST
    @Path("/bill/single")
    @Address(Addr.Bill.IN_COMMON)
    JsonObject inCommon(@BodyParam JsonObject data);

    /*
    {
        "items": [
            {
                "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                "parentId": "b3016e38-d543-4699-bcde-8de470803dce",
                "active": true,
                "identifier": "hotel.commodity",
                "price": 100,
                "quantity": 2,
                "name": "云烟",
                "code": "01.0003",
                "helpCode": "01.0003",
                "payTermId": "766814e7-b5f1-46ea-b51f-f8c7efa160d1",
                "hotelId": "ea951ec-9e7f-403b-b437-243bfd29a4fb",
                "value": "210afc4e-f50d-4554-b8f6-26d1b29393d1",
                "label": "01.0003",
                "amount": 200,
                "language": "cn",
                "categoryId": "210afc4e-f50d-4554-b8f6-26d1b29393d1",
                "type": "hotel.commodity",
                "appId": "78fce5a2-17f3-4dac-a75c-7e751595015c",
                "sort": 4,
                "leaf": true,
                "createdBy": "zero-environment",
                "key": "210afc4e-f50d-4554-b8f6-26d1b29393d1"
            },
            {
                "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                "parentId": "b3016e38-d543-4699-bcde-8de470803dce",
                "active": true,
                "identifier": "hotel.commodity",
                "price": 100,
                "quantity": 1,
                "name": "软玉溪",
                "code": "01.0002",
                "helpCode": "01.0002",
                "payTermId": "ad73b0b8-56b7-425d-8b5d-4e59aa0acf8a",
                "hotelId": "ea951ec-9e7f-403b-b437-243bfd29a4fb",
                "value": "88e0d3cd-d9ca-4138-b30c-ab9e82e379a5",
                "label": "01.0002",
                "amount": 100,
                "language": "cn",
                "categoryId": "88e0d3cd-d9ca-4138-b30c-ab9e82e379a5",
                "type": "hotel.commodity",
                "appId": "78fce5a2-17f3-4dac-a75c-7e751595015c",
                "sort": 3,
                "leaf": true,
                "createdBy": "zero-environment",
                "key": "88e0d3cd-d9ca-4138-b30c-ab9e82e379a5"
            }
        ],
        "orderId": "869e0b0c-4747-4c1a-b316-6c7be0122fd3",
        "bookId": "8c21fb10-a9c9-430d-8f50-16b87711288d",
        "modelId": "datum.room",
        "modelKey": "1105",
        "status": "Valid",
        "category": "Pay",
        "income": true,
        "indent": "NUM.PAYBILL",
        "type": "GoodIn"
    }
     */
    @POST
    @Path("/bill/multi")
    @Address(Addr.Bill.IN_MULTI)
    JsonObject inMulti(@BodyParam JsonObject data);

    @PUT
    @Path("/bill-item/split/:key")
    @Address(Addr.BillItem.UP_SPLIT)
    JsonObject upSplit(@PathParam(KName.KEY) String key, @BodyParam JsonObject data);

    @PUT
    @Path("/bill-item/revert/:key")
    @Address(Addr.BillItem.UP_REVERT)
    JsonObject upRevert(@PathParam(KName.KEY) String key, @BodyParam JsonObject data);

    @PUT
    @Path("/bill-item/cancel/:type")
    @Address(Addr.BillItem.UP_CANCEL)
    JsonObject upCancel(@PathParam(KName.TYPE) String type, @BodyParam JsonObject data);

    @PUT
    @Path("/bill-item/transfer/:key")
    @Address(Addr.Bill.UP_TRANSFER)
    JsonObject upTransfer(@PathParam(KName.KEY) String bookId, @BodyParam JsonObject data);
}
