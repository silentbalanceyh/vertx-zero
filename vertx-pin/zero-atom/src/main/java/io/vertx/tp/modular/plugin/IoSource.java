package io.vertx.tp.modular.plugin;


import io.vertx.core.json.JsonArray;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface IoSource {

    default ConcurrentMap<String, JsonArray> source() {
        return new ConcurrentHashMap<>();
    }
}
