package io.horizon.specification.meta.typed;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

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
        final JsonObject data = Ut.ioJObject(jsonFile);
        this.fromJson(data);
    }
}
