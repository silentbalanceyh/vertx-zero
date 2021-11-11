package io.vertx.tp.workflow.uca.runner;

import cn.zeroup.macrocosm.cv.WfPool;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface StoreOn {
    static StoreOn get() {
        return Fn.poolThread(WfPool.POOL_STORE, StoreEngine::new);
    }

    /*
     * {
     *      "definitionId": "xxx",
     *      "code": "workflow code",
     *      "bpmn": "xml definition"
     * }
     */
    Future<JsonObject> processByKey(String code);

    Future<JsonObject> processById(String definitionId);

    /*
     * {
     *      "code": "the last one",
     *      "formKey": "the original form Key",
     *      "fields": {
     *      }
     * }
     */
    Future<JsonObject> fetchForm(String definitionId, boolean isTask);
}
