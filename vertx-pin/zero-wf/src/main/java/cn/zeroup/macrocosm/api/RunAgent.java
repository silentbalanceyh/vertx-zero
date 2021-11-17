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
                "roleName": "开发人员",
                "record": {
                    "sizeUi": "1.86MB",
                    "instance": {
                        "uid": "rc-upload-1636725967401-2",
                        "name": "4k0005.jpg",
                        "key": "91157696-661d-412b-8d83-e750f36acab3",
                        "type": "image/jpeg",
                        "size": 1948572,
                        "sizeUi": "1.86MB",
                        "extension": "jpg"
                    },
                    "key": "key",
                    "fileKey": "fileKey"
                    "type": "image/jpeg",
                    "category": "FILE.REQUEST",
                    "size": 1948572,
                    "extension": "jpg",
                    "name": "4k0005.jpg"
                },
                "toUser": "63b5383e-5a2e-44ec-87d4-add096aac548",
                "toGroupMode": "ROLE",
                "name": "Test",
                "toRole": "1f27530f-38db-4662-81d4-46ea15b04205",
                "status": "DRAFT",
                "userName": "开发者",
                "draft": true,
                "workflow":{
                    "code": "xxx, definitionKey",
                    "definitionId": "xxx"
                }
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

}
