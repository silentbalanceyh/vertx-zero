package cn.vertxup.rbac.service.view;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 *
 * The Gold Rule for visitant
 *
 * 1. viewId + type + identifier + ( configKey = DEFAULT ) must return
 * only one `visitant` ( Unique )
 * 2. viewId + type + configKey ( User Input and UUID format ) must return
 * only one `visitant` ( Unique )
 *
 * The situation is as following:
 *
 * 1. view is abstract ( Bind to abstract identifier ), and the instance visitant
 * could be divide into different by configKey here.
 * 2. view is instance ( Bind to fixed identifier ), and the instance visitant
 * could be divide into singleton one by configKey refer ( configKey = DEFAULT )
 * and identifier could distinguish between.
 *
 * The tree for visitant searching processing:
 *
 * Level1: type = XXX
 * Level2: viewId = XXX ( contains role/user, resource ( uri + method ) )
 * Level3:
 * 1. viewId = abstract, identifier = null
 * 2. viewId = instance, identifier = fixed, configKey = DEFAULT
 * Level4: configKey
 * 1. configKey = DEFAULT ( Master visitant for identifier )
 * 2. configKey = UUID ( Master visitant for different identifiers )
 * 3. configKey = CODE ( This situation must be used with identifier at the same time )
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
     * 1) viewId + type + identifier + ( configKey = DEFAULT )
     * 2) viewId + type + configKey ( UUID )
     * One more:
     * `viewId + type` is not unique, here may contain more visitants
     */
    Future<JsonObject> fetchVisitant(String ownerType, String ownerId,
                                     JsonObject request);

    /*
     * Save visitant into database
     * 1) Insert / Update both
     * 2) Based by view because of viewId
     *
     * Unique condition is as following:
     * 1) viewId + type + identifier
     * 2) viewId + type + configKey
     */
    Future<JsonObject> saveAsync(JsonObject request, JsonObject view);
}
