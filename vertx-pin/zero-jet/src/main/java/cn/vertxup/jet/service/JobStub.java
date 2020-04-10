package cn.vertxup.jet.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/*
 * Job Stub for job processing here
 * Job Management in front end
 */
public interface JobStub {

    Future<JsonArray> fetchAll(String sigma);

    Future<JsonObject> fetchByKey(String key);

    Future<JsonObject> update(String key, JsonObject data);
}
