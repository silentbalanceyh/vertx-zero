package io.vertx.up.extension;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.eon.Orders;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

/**
 * 「Extension」
 * Name: Dynamic Router
 * Basic dynamic spec extension, every dynamic router must implements this interface for building dynamic routing system.
 * 1. The router system will mount to default Order: 6_000_000, it means that
 * dynamic router priority is lower than ZERO Standard: ( 5_000_000 ).
 * 2. There is a default method implementation and to nothing, one you have no implementation in
 * extension plugins, it's also available and do not impact Standard Part.
 */
public interface PlugRouter {

    String KEY_ROUTER = "router";

    /*
     * Get router configuration in zero config file `yml`
     */
    static JsonObject config() {
        final Node<JsonObject> uniform = Ut.singleton(ZeroUniform.class);
        final JsonObject config = uniform.read();
        return Fn.getNull(new JsonObject(), () -> config.getJsonObject(KEY_ROUTER), config);
    }

    void mount(Router router, JsonObject config);

    default void bind(final Vertx vertx) {
        // Empty Method for inject vertx instance
    }

    /**
     * We suggest do not overwrite this value once you haven't known the internal
     * architecture of Zero Framework.
     */
    default int getOrder() {
        return Orders.DYNAMIC;
    }
}
