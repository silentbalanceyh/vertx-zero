package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Read options and set default values
 *
 * @param <T>
 */
public interface Node<T> {

    ConcurrentMap<String, Node<JsonObject>> REFERENCES
            = new ConcurrentHashMap<>();

    /**
     * Infix usage for dynamic configuraiton laoding.
     *
     * @param key the up.god.file extension start with "vertx-xx"
     * @return Node reference that contains JsonObject data.
     */
    static Node<JsonObject> infix(final String key) {
        return Fn.pool(ZeroInfix.REFERENCES, key, () -> new ZeroInfix(key));
    }

    T read();
}
