package io.vertx.tp.modular.ray;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class RayRecord {
    private transient Object data;

    @SuppressWarnings("unchecked")
    private <T> T data() {
        return (T) this.data;
    }

    public void data(final JsonObject data) {
        this.data = data;
    }

    public void data(final JsonArray data) {
        this.data = data;
    }
}
