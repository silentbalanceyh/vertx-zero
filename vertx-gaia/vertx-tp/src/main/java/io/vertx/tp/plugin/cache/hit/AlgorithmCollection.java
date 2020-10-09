package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class AlgorithmCollection extends AbstractL1Algorithm {
    @Override
    public <T> void dataDelivery(final JsonObject message, final T entity) {
        if (Objects.nonNull(message)) {
            message.put("data", (JsonArray) Ut.serializeJson(entity));
            message.put("collection", Boolean.TRUE);
        }
    }

    @Override
    public String dataType() {
        return "LIST";
    }
}
