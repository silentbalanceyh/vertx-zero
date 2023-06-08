package io.vertx.up.atom.typed;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * Spec data structure as following:
 * // <pre><code class="json">
 *     {
 *         "data": "{} | []",
 *         "config": {}
 *     }
 * // </code></pre>
 *
 * 1. `data` part contains different data of types here, the common situation is as following:
 * -- JsonObject: json object
 * -- JsonArray: json array
 * 2. `config` part of JsonObject.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("unchecked")
public class UData implements Serializable {
    private final JsonObject config = new JsonObject();
    private final Object data;

    private <T> UData(final T input, final JsonObject config) {
        final JsonObject configuration = Ut.valueJObject(config);
        this.config.mergeIn(configuration, true);
        this.data = input;
    }

    /*
     * {
     *     "data": {},
     *     "config": {}
     * }
     */
    public static UData createJ(final JsonObject input) {
        Objects.requireNonNull(input);
        final JsonObject data = input.getJsonObject(KName.DATA, new JsonObject());
        final JsonObject config = input.getJsonObject(KName.CONFIG, new JsonObject());
        return new UData(data, config);
    }

    /*
     * {
     *     "data": [],
     *     "config": {}
     * }
     */
    public static UData createA(final JsonObject input) {
        Objects.requireNonNull(input);
        final JsonArray data = input.getJsonArray(KName.DATA, new JsonArray());
        final JsonObject config = input.getJsonObject(KName.CONFIG, new JsonObject());
        return new UData(data, config);
    }

    public JsonObject config() {
        return this.config;
    }

    public <T> T config(final String key) {
        return (T) this.config.getValue(key);
    }

    public JsonObject dataJ() {
        if (this.data instanceof JsonObject) {
            return (JsonObject) this.data;
        } else {
            return new JsonObject();
        }
    }

    public JsonArray dataA() {
        if (this.data instanceof JsonArray) {
            return (JsonArray) this.data;
        } else {
            return new JsonArray();
        }
    }
}
