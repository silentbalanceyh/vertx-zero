package cn.vertxup.ui.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * --> type = null
 * -----> control     = null,         WEB
 * -----> control     = value,        ATOM
 * --> type = ATOM
 * -----> control     = value
 * --> type = WEB
 * -----> identifier  = value
 * --> type = FLOW
 * -----> control     = value,
 * -----> event       = 任务节点
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface DoStub {
    /*
     * fetchAtom 和 fetchWeb 的切换（遗留系统专用）
     * type = null, 原始流程，根据 control 判断
     */
    Future<JsonArray> fetchSmart(JsonObject params);

    /*
     * 参数中必须包含:
     * {
     *     "control": "控件ID"
     * }
     **/
    Future<JsonArray> fetchAtom(JsonObject params);

    /*
     * 参数中必须包含:
     * {
     *     "identifier": "模型ID"
     * }
     */
    Future<JsonArray> fetchWeb(JsonObject params);

    /*
     * 参数中必须包含:
     * {
     *     "control": "工作流名称",
     *     "event":   "task任务节点名称"
     * }
     */
    Future<JsonArray> fetchFlow(JsonObject params);
}
