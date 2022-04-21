package io.vertx.tp.workflow.atom;

import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WTabb implements Serializable {
    /*
     * Data Analyzing on
     * {
            "flowDefinitionKey": "process.asset.income",
            "flowDefinitionId": "process.asset.income:1:c175f471-c0fb-11ec-8c0e-161b1b9eb817",
            "flowInstanceId": "4fecbc57-c128-11ec-945b-161b1b9eb817",
            "flowName": "xxxx"
            "flowEnd": false,

            "taskId": "4ff39a2f-c128-11ec-945b-161b1b9eb817",
            "taskKey": "e.draft",
            "taskCode": "WAI220421100003-01",
            "taskSerial": "WAI220421100003-01",
            "taskName": "xxxx"
     * }
     */
    public WTabb(final JsonObject record) {

    }
}
