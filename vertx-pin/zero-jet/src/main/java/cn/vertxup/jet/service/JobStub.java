package cn.vertxup.jet.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/*
 * Job Stub for job processing here
 * Job Management in front end
 */
public interface JobStub {

    Future<JsonObject> searchJobs(String sigma, JsonObject body, boolean grouped);

    Future<JsonObject> fetchByKey(String key);

    Future<JsonObject> update(String key, JsonObject data);
}
