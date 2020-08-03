package cn.vertxup.rbac.service.view;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public interface VisitStub {
    /*
     * Fetch single visitant object by two methods:
     * The request data is as following:
     * {
     *      "resourceId": "Sec Resource Id",
     *      "identifier": "Model identifier ( Static )",
     *      "configKey": "Dynamic Model, Control Id or ",
     *      "type": "FORM / RECORD / LIST / OP"
     * }
     * resourceId / ownerType / ownerId / name = DEFAULT
     * could calculate viewId
     * The unique condition for visitant:
     * 1) viewId + type + identifier
     * 2) viewId + type + configKey
     * One more:
     * `viewId + type` is not unique, here may contain more visitants
     */
    Future<JsonObject> fetchVisitant(String ownerType, String ownerId,
                                     JsonObject request);
}
