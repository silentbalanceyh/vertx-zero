package io.vertx.core;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;

import java.io.Serializable;

/**
 * # 「Co」Vert.x Extension
 *
 * This class is `Options` architecture for RPC ( Default implementation is gRpc ), it provide the configuration for
 * micro service communication. This class should be used in Micro Service EmApp instead of standalone web
 * application here. In zero framework, the default service communication components is implemented with gRpc.
 *
 * Here are yaml structure in `vertx-server.yml`:
 *
 * **vertx-server.yml**
 *
 * ```yaml
 * // <pre><code class="yaml">
 *  server:
 *    - name: ipc-datum
 *      type: ipc
 *      config:
 *          jks: "ipc/ssl/rpc-server-keystore.jks"
 *          password: "wibble"
 *          port: 7104
 *          host: 0.0.0.0
 * // </code></pre>
 * ```
 *
 * This configuration is specific and you must define it in `vertx-server.yml` file instead of other files, the
 * server will be configured in `lime` extension internally. Here the name will be used out of current component,
 * I recommend to use `ipc` prefix in `name` attribute in zero framework, the name will be written to registry
 * configuration center ( In zero framework the default implementation is `etcd3` ).
 *
 * The `config` options is for RPC server:
 *
 * 1. RPC server host and port, the port must be different from http server port, the host `0.0.0.0` means wildly used.
 * 2. `jks` and `password` defined the communication mode of SSL here, when you defined ssl mode, each client must be over the same SSL cert.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RpcOptions implements Serializable {

    /**
     * Default `port`, 6084
     * It defined RPC server default port, this port is public because
     * RPC client will use this attribute as default port.
     */
    public static final Integer DEFAULT_PORT = 6084;

    /**
     * Default `name`, __RPC__
     * The service identifier in micro service architecture, you can provide
     * your own service name.
     */
    private static final String DEFAULT_NAME = "__RPC__";

    /**
     * Default `host`, 0.0.0.0
     * This attribute is for client usage, the default value means no limitation.
     */
    private static final String DEFAULT_HOST = "0.0.0.0";

    private transient String name = DEFAULT_NAME;
    private transient String host = DEFAULT_HOST;
    private transient Integer port = DEFAULT_PORT;
    private transient JsonObject options = new JsonObject();

    /**
     * Default constructor
     */
    public RpcOptions() {
    }

    /**
     * Copy constructor
     *
     * @param other The other {@code ServidorOptions} to copy when creating this
     */
    public RpcOptions(final RpcOptions other) {
        this.name = other.getName();
        this.host = other.getHost();
        this.port = other.getPort();
        this.options = other.getOptions();
    }

    /**
     * Create an instance from a {@link io.vertx.core.json.JsonObject}
     *
     * @param json the JsonObject to create it from
     */
    public RpcOptions(final JsonObject json) {
        RpcOptionsConverter.fromJson(json, this);
    }

    /**
     * Get the name of current RPC server, it could identify self in micro service framework
     *
     * @return the RPC server name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * 「Fluent」
     * You can set the name of RPC server here.
     *
     * @param name the name you want to modify
     *
     * @return a reference to this.
     */
    @Fluent
    public RpcOptions setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Here the format should be host name or ip address, when you set the host
     * to named format, the system will lookup the actual ip address based on DNS.
     *
     * @return host of RPC server
     */
    public String getHost() {
        return this.host;
    }

    /**
     * 「Fluent」
     * You can set the host and modified before RPC server started
     *
     * @param host the host that you wanted.
     *
     * @return a reference to this
     */
    @Fluent
    public RpcOptions setHost(final String host) {
        this.host = host;
        return this;
    }

    /**
     * RPC server port
     *
     * @return the port of your RPC server
     */
    public Integer getPort() {
        return this.port;
    }

    /**
     * 「Fluent」
     * When you wan to modify port or some ports conflicts, you can
     * modify the port of RPC server.
     *
     * @param port the RPC server port.
     *
     * @return a reference to this
     */
    @Fluent
    public RpcOptions setPort(final Integer port) {
        this.port = port;
        return this;
    }

    /**
     * Get the configuration of current RPC extension configuration
     *
     * @return the configuration information
     */
    public JsonObject getOptions() {
        return this.options;
    }

    /**
     * 「Fluent」
     *
     * You can provide another configuration options to overwrite default
     *
     * @param options You can provide RPC server options
     *
     * @return a reference to this
     */
    public RpcOptions setOptions(final JsonObject options) {
        this.options = options;
        return this;
    }

    /**
     * JsonObject serialization method `toJson`
     *
     * @return serialized JsonObject
     */
    public JsonObject toJson() {
        final JsonObject data = new JsonObject();
        data.put(KName.NAME, this.name);
        final JsonObject config = null == this.options ? new JsonObject() : this.options.copy();
        config.put(KName.HOST, this.host);
        config.put(KName.PORT, this.port);
        data.put(KName.CONFIG, config);
        return data;
    }

    @Override
    public String toString() {
        return "ServidorOptions{" +
            "name='" + this.name + '\'' +
            ", host='" + this.host + '\'' +
            ", port=" + this.port +
            ", options=" + this.options +
            '}';
    }
}
