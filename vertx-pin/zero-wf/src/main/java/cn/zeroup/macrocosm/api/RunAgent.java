package cn.zeroup.macrocosm.api;

import cn.zeroup.macrocosm.cv.HighWay;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface RunAgent {
    /*
    {
        "openBy": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
        "toUser": "a0b1c6bc-4162-47e2-8f16-c9f4dd162739",
        "record": {
            "size": 1114042,
            "name": "error.jpeg",
            "sizeUi": "1.06MB",
            "type": "image/jpeg",
            "file": [
                {
                    "uid": "rc-upload-1643355423248-2",
                    "name": "error.jpeg",
                    "key": "ceafc8ec-0137-46df-a60f-38ae475b0242",
                    "type": "image/jpeg",
                    "size": 1114042,
                    "sizeUi": "1.06MB",
                    "extension": "jpeg"
                }
            ],
            "category": "FILE.REQUEST",
            "extension": "jpeg",
            "key": "ceafc8ec-0137-46df-a60f-38ae475b0242"
        },
        "toUserName": "开发者",
        "status": "DRAFT",
        "owner": "f7fbfaf9-8319-4eb0-9ee7-1948b8b56a67",
        "title": "TEST",
        "catalog": "w.document.request",
        "type": "workflow.doc",
        "description": "<p>TEST</p>",
        "openAt": "2022-01-28T07:37:06.141Z",
        "ownerName": "虞浪",
        "language": "cn",
        "active": true,
        "sigma": "Qxw5HDkluJFnAPmcQCtu9uhGdXEiGNtP",
        "workflow": {
            "definitionKey": "process.file.management",
            "definitionId": "process.file.management:1:c80c1ad1-7fd9-11ec-b990-f60fb9ea15d8"
        },
        "draft": true
    }
     */
    @POST
    @Path("/up/flow/start")
    @Address(HighWay.Do.FLOW_START)
    JsonObject start(@BodyParam JsonObject body);

    @PUT
    @Path("/up/flow/complete")
    @Address(HighWay.Do.FLOW_COMPLETE)
    JsonObject complete(@BodyParam JsonObject body);

    @PUT
    @Path("/up/flow/saving")
    @Address(HighWay.Do.FLOW_DRAFT)
    JsonObject draft(@BodyParam JsonObject body);

    @PUT
    @Path("/up/flow/batch")
    @Address(HighWay.Do.FLOW_BATCH)
    JsonObject batch(@BodyParam JsonObject body);

    @PUT
    @Path("/up/flow/cancel")
    @Address(HighWay.Do.FLOW_CANCEL)
    JsonObject cancel(@BodyParam JsonObject body);
}
