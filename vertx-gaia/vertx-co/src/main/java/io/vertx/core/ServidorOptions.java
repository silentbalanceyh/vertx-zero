package io.vertx.core;

import io.vertx.core.json.JsonObject;

import java.io.Serializable;

public class ServidorOptions implements Serializable {

    private static final String DEFAULT_NAME = "__RPC__";
    private static final String DEFAULT_HOST = "0.0.0.0";
    public static final Integer DEFAULT_PORT = 6084;

    private transient String name = DEFAULT_NAME;
    private transient String host = DEFAULT_HOST;
    private transient Integer port = DEFAULT_PORT;
    private transient JsonObject options = new JsonObject();

    public ServidorOptions() {
    }

    public ServidorOptions(final ServidorOptions other) {
        this.name = other.getName();
        this.host = other.getHost();
        this.port = other.getPort();
        this.options = other.getOptions();
    }

    public ServidorOptions(final JsonObject json) {
        ServidorOptionsConverter.fromJson(json, this);
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }

    public JsonObject getOptions() {
        return this.options;
    }

    public void setOptions(final JsonObject options) {
        this.options = options;
    }

    public JsonObject toJson() {
        final JsonObject data = new JsonObject();
        data.put("name", this.name);
        final JsonObject config = null == this.options ? new JsonObject() : this.options.copy();
        config.put("host", this.host);
        config.put("port", this.port);
        data.put("config", config);
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
