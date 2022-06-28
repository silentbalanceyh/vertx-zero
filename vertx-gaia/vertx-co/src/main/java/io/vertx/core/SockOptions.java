package io.vertx.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SockOptions implements Serializable {

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject config = new JsonObject();
    private String publish;

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> component;

    @JsonIgnore
    private HttpServerOptions serverOptions;

    /*
     * The `input` parameter data structure should be:
     *
     * The yml format
     *
     * config:
     *    port:
     * websocket:
     *    publish:
     *    component:
     *    config:
     *       stomp:
     *       bridge:
     *       handler:
     *    server:
     *
     * 1) If the `config -> port` is the same as `HTTP` server, it will be mount to
     *    HTTP server here Or the Vert.x instance will ignore other kind of server.
     * 2) The configuration is as following:
     *    - publish:    Whether the framework enable the publish websocket
     *                  The publish websocket path is `/ws/`
     *    - component:  The component should be `Ares` component here, different kind of
     *                  implementation require different component, the default is `bridge sockJs`.
     *    - config:     The configuration for different implementation
     *        - stomp:  「mode = STOMP」stomp handler configuration
     *        - bridge: 「mode = SockJs Bridge」bridge sockJs handler configuration
     *        - handler:「mode = SockJs」direct configure the sockJs handler
     *    - server:     Here are two choice of server type:
     *        - 「StompServer」 Stomp Server Configure
     *        - 「SockServer」  WebSocket Server Configure
     *
     * The input parameter is `websocket` node data of JsonObject
     */
    public JsonObject getConfig() {
        return this.config;
    }

    public void setConfig(final JsonObject config) {
        this.config = config;
    }

    public String getPublish() {
        return this.publish;
    }

    public void setPublish(final String publish) {
        this.publish = publish;
    }

    public Class<?> getComponent() {
        return this.component;
    }

    public void setComponent(final Class<?> component) {
        this.component = component;
    }

    /*
     * Three configuration key for different usage
     * 1. configSockJs
     * 2. configBridge
     * 3. configStomp
     * But current method is only for `publish` websocket channel
     */
    public JsonObject configSockJs() {
        return Ut.valueJObject(this.config, KName.HANDLER);
    }

    public SockOptions options(final HttpServerOptions serverOptions) {
        this.serverOptions = serverOptions;
        return this;
    }

    public HttpServerOptions options() {
        return this.serverOptions;
    }
}
