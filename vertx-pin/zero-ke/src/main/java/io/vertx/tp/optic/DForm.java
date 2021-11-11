package io.vertx.tp.optic;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface DForm {
    /*
     * {
     *     "dynamic": false,
     *     "code": form code / form path,
     *     "sigma": ""
     * }
     */
    Future<JsonObject> fetchUi(JsonObject params);
}
