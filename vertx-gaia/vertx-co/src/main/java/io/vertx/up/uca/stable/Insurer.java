package io.vertx.up.uca.stable;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.ZeroException;

/**
 * Insure to be configuration correct.
 */
public interface Insurer {
    /**
     * @param data        input data
     * @param elementRule rule element
     *
     * @throws ZeroException returned checked error
     */
    void flumen(JsonObject data, JsonObject elementRule) throws ZeroException;

    /**
     * Verify json array for each element.
     *
     * @param array       input data array
     * @param elementRule rule element
     *
     * @throws ZeroException returned checked error
     */
    void flumen(JsonArray array, JsonObject elementRule) throws ZeroException;
}
