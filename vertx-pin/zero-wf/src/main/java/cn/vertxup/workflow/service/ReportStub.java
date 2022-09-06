package cn.vertxup.workflow.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ReportStub {
    /*
     * Fetch Activity
     * */
    Future<JsonArray> fetchActivity(String modelKey);

    Future<JsonArray> fetchActivityByUser(String modelKey, String userId);
}
