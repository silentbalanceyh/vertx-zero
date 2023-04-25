package io.horizon.spi.feature;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * X_CATEGORY Tree Component
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Arbor {
    /*
     * Here categories is the base categories
     * -- 1. Include `treeComponent` and `treeConfig`
     * -- 2. Append the new data following original categories
     */
    Future<JsonArray> generate(JsonObject category, JsonObject configuration);
}
