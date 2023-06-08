package io.vertx.up.atom.agent;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.container.IpcType;

import java.io.Serializable;

public class IpcData implements Serializable {
    /**
     * Default community type
     */
    private IpcType type = IpcType.UNITY;
    /**
     * Community Name
     */
    private String name;
    /**
     * Community Port
     */
    private Integer port;
    /**
     * Community Host
     */
    private String host;
    /**
     * Community data
     */
    private Buffer data;
    /**
     * Community address
     */
    private String address;

    /**
     * Additional Config
     */
    private JsonObject config = new JsonObject();

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public IpcType getType() {
        return this.type;
    }

    public void setType(final IpcType type) {
        this.type = type;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public Buffer getData() {
        return this.data;
    }

    public void setData(final Buffer data) {
        this.data = data;
    }

    public JsonObject getConfig() {
        return this.config;
    }

    public void setConfig(final JsonObject config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "IpcData{" +
            "type=" + this.type +
            ", port=" + this.port +
            ", host='" + this.host + '\'' +
            ", data=" + this.data +
            ", address=" + this.address +
            ", config=" + this.config +
            '}';
    }
}
