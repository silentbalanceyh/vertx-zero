package cn.vertxup.ambient.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface DatumStub {
    // Tabular Fetching
    /*
     * Three mode for tabular fetching
     * 1. AppId
     *    ( N ) APP_ID = ? AND TYPE IN (?,?)
     *    ( N ) APP_ID = ? AND TYPE = ?
     *    ( 1 ) APP_ID = ? AND TYPE = ? AND CODE = ?
     * 2. Sigma
     *    ( N ) SIGMA = ? AND TYPE IN (?,?)
     *    ( N ) SIGMA = ? AND TYPE = ?
     *    ( 1 ) SIGMA = ? AND TYPE = ? AND CODE = ?
     */
    // -- appId
    Future<JsonArray> dictApp(String appId, JsonArray types);

    Future<JsonArray> dictApp(String appId, String type);

    Future<JsonObject> dictApp(String appId, String type, String code);

    // -- sigma
    Future<JsonArray> dictSigma(String sigma, JsonArray types);

    Future<JsonArray> dictSigma(String sigma, String type);

    Future<JsonObject> dictSigma(String sigma, String type, String code);

    // Category Fetching
    /*
     * 1. AppId
     *    ( N ) APP_ID = ? AND TYPE IN (?,?)
     *    ( N ) APP_ID = ? AND TYPE = ? AND LEAF = ? ( Yes/No )
     *    ( 1 ) APP_ID = ? AND TYPE = ? AND CODE = ?
     * 2. Sigma
     *    ( N ) SIGMA = ? AND TYPE IN (?,?)
     *    ( N ) SIGMA = ? AND TYPE = ? AND LEAF = ? ( Yes/No )
     *    ( N ) SIGMA = ? AND TYPE = ?
     */
    // -- appId
    Future<JsonArray> treeApp(String appId, JsonArray types);

    default Future<JsonArray> treeApp(final String appId, final String type) {
        return this.treeApp(appId, type, Boolean.TRUE);
    }

    Future<JsonArray> treeApp(String appId, String type, Boolean leaf);

    Future<JsonObject> treeApp(String appId, String type, String code);

    // -- sigma

    Future<JsonArray> treeSigma(String sigma, JsonArray types);

    default Future<JsonArray> treeSigma(final String sigma, final String type) {
        return this.treeSigma(sigma, type, Boolean.TRUE);
    }

    Future<JsonArray> treeSigma(String sigma, String type, Boolean leaf);

    Future<JsonObject> treeSigma(String sigma, String type, String code);

    // Number Generation
    Future<JsonArray> numberApp(String appId, String code, Integer count);

    Future<JsonArray> numberAppI(String appId, String identifier, Integer count);

    Future<Boolean> numberAppR(String appId, String code, Long defaultValue);

    Future<JsonArray> numberSigma(String sigma, String code, Integer count);

    Future<JsonArray> numberSigmaI(String sigma, String identifier, Integer count);

    Future<Boolean> numberSigmaR(String sigma, String code, Long defaultValue);
}
