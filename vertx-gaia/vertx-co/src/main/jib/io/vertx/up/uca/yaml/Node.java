package io.vertx.up.uca.yaml;

import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonObject;

/**
 * Read options and set default values
 *
 * @param <T>
 */
public interface Node<T> {
    Cc<String, Node<JsonObject>> CC_REFERENCE = Cc.open();

    /**
     * Infusion usage for dynamic configuraiton laoding.
     *
     * @param key the up.god.file extension start with "vertx-xx"
     *
     * @return Node reference that contains JsonObject data.
     */
    //    static Node<JsonObject> infix(final String key) {
    //        return CC_REFERENCE.pick(() -> new ZeroInfix(key), key);
    //    }

    T read();
}
