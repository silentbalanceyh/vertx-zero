package io.vertx.core;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.io.Serializable;

/**
 * # 「Co」Vert.x Extension
 *
 * This class is `Options` architecture in vert.x framework for cluster configuration in zero
 * framework. Here are some configuration parts:
 *
 * 1. Whether you have enabled Cluster in zero framework ( About vert.x cluster )
 * 2. After you have set cluster, here you should provide a default `io.vertx.core.spi.cluster.ClusterManager` for it,
 * 3. The default cluster manager is `HazelcastClusterManager` ( The same as vert.x ).
 * 4. Here provide `JsonObject` reference to store cluster options in the configured file.
 *
 * Here are yaml structure in `vertx.yml`:
 *
 * **vertx.yml**
 *
 * ```yaml
 * // <pre><code class="yaml">
 *  zero:
 *      vertx:
 *          clustered:
 *              enabled: true           # Enable Cluster
 *              manager: ""             # The default cluster manager implementation class name
 *              options:                # The JsonObject configuration for cluster
 * // </code></pre>
 * ```
 *
 * Please be careful about the configuration file, this configuration must be in `vertx.yml` file
 * instead of `lime` extension in zero framework, it is also no third-part configuration, the file
 * name must be fixed ( `vertx.yml` ).
 *
 * > NOTE: The generator will be ignored because of `ClusterManager` serialization with specific
 * code logical.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
// @DataObject(generateConverter = true, publicConverter = false)
public class ClusterOptions implements Serializable {

    /**
     * Default `enabled`, false
     * Whether enable the cluster mode in zero framework
     **/
    private static final boolean ENABLED = false;

    /**
     * Default `manager`, HazelcastClusterManager
     * This attribute is valid when `enabled = true`, you can provide your custom
     * defined ClusterManager to overwrite the default one.
     **/
    private static final ClusterManager MANAGER = new HazelcastClusterManager();

    /**
     * Default `options`, JsonObject without any attributes
     * When you provide custom ClusterManager, you may need some additional
     * configuration data here.
     **/
    private static final JsonObject OPTIONS = new JsonObject();

    private boolean enabled;
    private ClusterManager manager;
    private JsonObject options;

    /**
     * Default constructor
     */
    public ClusterOptions() {
        this.enabled = ENABLED;
        this.manager = MANAGER;
        this.options = OPTIONS;
    }

    /**
     * Copy constructor
     *
     * @param other The other {@code ClusterOptions} to copy when creating this
     */
    public ClusterOptions(final ClusterOptions other) {
        this.enabled = other.isEnabled();
        this.manager = other.getManager();
        this.options = other.getOptions();
    }

    /**
     * Create an instance from a {@link io.vertx.core.json.JsonObject}
     *
     * @param json the JsonObject to create it from
     */
    public ClusterOptions(final JsonObject json) {
        this();
        ClusterOptionsConverter.fromJson(json, this);
    }

    /**
     * Get whether cluster mode is enabled in zero framework.
     *
     * @return the result of cluster ( true / false )
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * 「Fluent」
     * When you want to modify the cluster mode, you can call this api.
     *
     * @param enabled the cluster mode switch based on your input.
     *
     * @return a reference to this.
     */
    @Fluent
    public ClusterOptions setEnabled(final boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * This attribute is different from other attributes, the literal of `manager` is
     * java {@link java.lang.String}, here the ClusterOptions stored `ClusterManager`
     * reference that has been initialized by converter. It's more smart for developers
     * to get ClusterManager directly and ignored the instance building code flow.
     *
     * @return ClusterManager
     */
    public ClusterManager getManager() {
        return this.manager;
    }

    /**
     * 「Fluent」
     * Set cluster manager in options to replace the default `ClusterManager`.
     *
     * @param manager another `ClusterManager` reference from out.
     *
     * @return a reference to this.
     */
    @Fluent
    public ClusterOptions setManager(final ClusterManager manager) {
        this.manager = manager;
        return this;
    }

    /**
     * @return the additional configuration in current options
     */
    public JsonObject getOptions() {
        return this.options;
    }

    /**
     * 「Fluent」
     * Set cluster manager additional configuration data here, if you provide your
     * custom defined ClusterManager, you can set this additional configuration to
     * configure ClusterManager to do adjustment of `options`.
     *
     * @param options the JsonObject that stored additional configuration.
     *
     * @return a reference to this.
     */
    @Fluent
    public ClusterOptions setOptions(final JsonObject options) {
        this.options = options;
        return this;
    }

    @Override
    public String toString() {
        return "ClusterOptions{enabled=" + this.enabled
            + ", manager=" +
            ((null == this.manager) ? "null" : this.manager.getClass().getName())
            + ", options="
            + this.options.encode() + '}';
    }
}
