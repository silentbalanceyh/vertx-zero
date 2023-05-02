package io.horizon.specification.typed;

import io.horizon.util.HUt;
import io.vertx.core.json.JsonObject;

/**
 * Json data extract from object, there are three method for using
 * 1) Json Data to T
 * 2) Json File to T
 * 3) T to Json
 */
public interface TJson {
    /**
     * T to Json
     *
     * @return JsonObject returned.
     */
    JsonObject toJson();

    /**
     * Json to T ( Pojo )
     *
     * @param json input JsonObject contains data here.
     */
    void fromJson(JsonObject json);

    /**
     * Default implementation here
     * File -> Json -> T
     *
     * @param jsonFile input file
     */
    default void fromFile(final String jsonFile) {
        final JsonObject data = HUt.ioJObject(jsonFile);
        this.fromJson(data);
    }
}
