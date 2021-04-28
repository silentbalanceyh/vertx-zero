package io.vertx.tp.optic.business;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class ExAttributeComponent {

    public ConcurrentMap<String, JsonArray> source(final JsonObject definition) {
        System.out.println(definition);
        return new ConcurrentHashMap<>();
    }
}
