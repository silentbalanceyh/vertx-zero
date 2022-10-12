package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.pojos.SPath;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScOwner;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface RuleStub {
    /*
     * Process SPath
     *
     * 1) Read `runComponent` to build `HValve`
     * 2) Based on `HValue` to extract `HPermit` object
     * 3) Configure based on `HPermit`
     */
    Future<JsonArray> regionAsync(List<SPath> paths);

    /*
     * Process SPath
     *
     * 1) Read all the related `SPocket` based on path
     * 2) The returned data structure
     * {
     *     "resource": {
     *         "v": {
     *             "mapping": {},
     *             "config": {},
     *             "value": ...
     *         },
     *         "h": {
     *             "mapping": {},
     *             "config": {},
     *             "value": ...
     *         },
     *         "q": {
     *             "mapping": {},
     *             "config": {},
     *             "value": ...
     *         }
     *     }
     * }
     * 3) The value of each node must be calculated based on `owner` and stored `resource`
     */
    Future<JsonObject> regionAsync(JsonObject pathData, ScOwner owner);

    /*
     * 内置处理 path -> code 集合，直接传入的 pathCodes 包含了当前 path 之下的子集合
     * viewData 则维持原始的 Body 经过处理之后的集合
     * {
     *     "resource1": {
     *         "xxx"
     *     }
     * }
     */
    Future<JsonObject> regionAsync(JsonObject condition, JsonObject viewData);

    /*
     * Fetch all views that belong to
     *
     * 1) ownerType: USER | ROLE
     * 2) ownerId: user id | role id
     * 3) keys: resource ids that act as condition
     * 4) view: default view name: DEFAULT
     */
    Future<JsonArray> fetchViews(String ownerType, String ownerId,
                                 JsonArray keys, String view);

    Future<JsonArray> saveViews(String ownerType, String ownerId,
                                JsonArray keys, String view);
}
