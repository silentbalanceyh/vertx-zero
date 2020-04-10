package io.vertx.core;

import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.io.Serializable;

public class ClusterOptions implements Serializable {
    private static final boolean ENABLED = false;
    /**
     * Default -> HazelcastClusterManager
     */
    private static final ClusterManager MANAGER = new HazelcastClusterManager();
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

    public ClusterManager getManager() {
        return this.manager;
    }

    public JsonObject getOptions() {
        return this.options;
    }

    public ClusterOptions setEnabled(final boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public ClusterOptions setManager(final ClusterManager manager) {
        this.manager = manager;
        return this;
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
