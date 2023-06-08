package io.modello.specification.uca;


import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 属性通道
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface IoSource {

    default ConcurrentMap<String, JsonArray> source(final JsonObject definition) {
        return new ConcurrentHashMap<>();
    }
}
