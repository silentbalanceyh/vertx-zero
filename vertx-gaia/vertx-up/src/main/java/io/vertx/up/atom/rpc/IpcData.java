package io.vertx.up.atom.rpc;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.IpcType;

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
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public IpcType getType() {
        return type;
    }

    public void setType(final IpcType type) {
        this.type = type;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public Buffer getData() {
        return data;
    }

    public void setData(final Buffer data) {
        this.data = data;
    }

    public JsonObject getConfig() {
        return config;
    }

    public void setConfig(final JsonObject config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "IpcData{" +
            "type=" + type +
            ", port=" + port +
            ", host='" + host + '\'' +
            ", data=" + data +
            ", address=" + address +
            ", config=" + config +
            '}';
    }
}
