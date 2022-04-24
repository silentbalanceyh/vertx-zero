package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.cache.CcOld;

/**
 * Read options and set default values
 *
 * @param <T>
 */
public interface Node<T> {
    CcOld<String, Node<JsonObject>> CC_REFERENCE = CcOld.open();

    /**
     * Infix usage for dynamic configuraiton laoding.
     *
     * @param key the up.god.file extension start with "vertx-xx"
     *
     * @return Node reference that contains JsonObject data.
     */
    static Node<JsonObject> infix(final String key) {
        return CC_REFERENCE.pick(key, () -> new ZeroInfix(key));
        // Fn.po?l(ZeroInfix.REFERENCES, key, () -> new ZeroInfix(key));
    }

    T read();
}
