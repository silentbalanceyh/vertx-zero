package io.vertx.up.extension;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.up.extension.router.AresHub;

/**
 * This structure is new for `ZeroHttpAgent` extension, you can call this interface to create.
 * And it replace the previous binding such as
 *
 * 1) Dynamic Routing System ( I_API )
 * 2) Websocket Extension ( Stomp / SockJs )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Ares {

    Cc<String, Ares> CC_ARES = Cc.openThread();

    static Ares instance(final Vertx vertx) {
        return CC_ARES.pick(() -> new AresHub(vertx),
            // key = class name + hash code
            AresHub.class.getName() + vertx.hashCode());
    }

    /*
     * Optional for HttpServer reference
     */
    default Ares bind(final HttpServer server, final HttpServerOptions options) {
        return this;
    }

    default void configure(final HttpServerOptions options) {
    }

    /*
     * The phase for building should input following information:
     *
     * 1) The configuration of Kv<Integer,HttpServerOption>
     * 2) The reference of Router / HttpServer
     * 3) The constructor should bind to Vertx Reference instead of input parameters
     *
     * Method ( Router, JsonObject ) won't be used in `component` of websocket
     * because all the configuration of extension part will be calculated.
     */
    void mount(Router router, JsonObject config);

    default void mount(final Router router) {
        /*
         * Without Configuration and call internal major code logical
         */
        this.mount(router, new JsonObject());
    }
}
