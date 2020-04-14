package io.vertx.core;

import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.io.Serializable;

/**
 * 「Tp」Vert.x Extension
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
 * // <pre><code class="yaml">
 *      zero:
 *          vertx:
 *              clustered:
 *                  enabled: true  # Enable Cluster
 *                  manager: ""    # The default cluster manager implementation class name
 *                  options:       # The JsonObject configuration for cluster
 * // </code></pre>
 *
 * Please be careful about the configuration file, this configuration must be in `vertx.yml` file
 * instead of `lime` extension in zero framework, it is also no third-part configuration, the file
 * name must be fixed ( `vertx.yml` ).
 *
 * @author lang
 */
public class ClusterOptions implements Serializable {
    /** Static default value to enable cluster mode: false **/
    private static final boolean ENABLED = false;
    /** Static default ClusterManager: HazelcastClusterManager **/
    private static final ClusterManager MANAGER = new HazelcastClusterManager();
    /** Static default options of JsonObject **/
    private static final JsonObject OPTIONS = new JsonObject();

    private boolean enabled;
    private ClusterManager manager;
    private JsonObject options;

    public ClusterOptions() {
        this.enabled = ENABLED;
        this.manager = MANAGER;
        this.options = OPTIONS;
    }

    public ClusterOptions(final ClusterOptions other) {
        this.enabled = other.isEnabled();
        this.manager = other.getManager();
        this.options = other.getOptions();
    }

    public ClusterOptions(final JsonObject json) {
        this();
        ClusterOptionsConverter.fromJson(json, this);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public ClusterOptions setEnabled(final boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public ClusterManager getManager() {
        return this.manager;
    }

    public ClusterOptions setManager(final ClusterManager manager) {
        this.manager = manager;
        return this;
    }

    public JsonObject getOptions() {
        return this.options;
    }

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
