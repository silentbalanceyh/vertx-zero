package cn.vertxup.ambient.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface DatumStub {

    Future<JsonArray> tabulars(String appId, JsonArray types);

    Future<JsonArray> tabularsBySigma(String sigma, JsonArray types);

    Future<JsonArray> tabulars(String appId, String type);

    Future<JsonObject> tabular(String appId, String type, String code);

    Future<JsonArray> categories(String appId, JsonArray types);

    Future<JsonArray> categories(String appId, String type, Boolean includeLeaf);

    Future<JsonArray> categoriesBySigma(String sigma, String type);

    Future<JsonArray> categoriesBySigma(String sigma, JsonArray types);

    Future<JsonObject> category(String appId, String type, String code);

    Future<JsonArray> numbers(String appId, String code, Integer count);

    Future<JsonArray> numbersBySigma(String sigma, String code, Integer count);

    Future<JsonArray> codesBySigma(String sigma, String identifier, Integer count);
}
