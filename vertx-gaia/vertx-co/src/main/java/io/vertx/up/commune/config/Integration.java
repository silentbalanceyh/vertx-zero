package io.vertx.up.commune.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Json;
import io.vertx.up.commune.exchange.DiConsumer;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * DTO for third part integration basic configuration instead of other
 * {
 *      "endpoint": "http://www.demo.cn/ws/api/",
 *      "port": 1234,
 *      "username": "lang",
 *      "password": "xxxx",
 *      "hostname": "www.demo.cn or 192.168.0.12",
 *      "publicKeyFile": "public key path",
 *      "apis":{
 *          "get.username": {
 *              "method": "POST",
 *              "uri": "/uri/getinfo",
 *              "headers": {}
 *          },
 *          "post.test": {
 *              "method": "GET",
 *              "uri": "/uri/getinfo",
 *              "headers": {}
 *          }
 *      },
 *      "options":{
 *
 *      }
 * }
 */
public class Integration implements Json, Serializable {

    private final transient ConcurrentMap<String, IntegrationRequest> apis
        = new ConcurrentHashMap<>();
    /*
     * Restful / Web Service information ( such as jdbcUrl )
     * The target service should be: endpoint + api ( IntegrationRequest )
     */
    private transient String endpoint;
    private transient Integer port;
    private transient String username;
    private transient String protocol;
    /*
     * SSL enabled, these two fields stored
     * 1) publicKeyFile
     * 2) Authentication
     */
    private transient String password;
    private transient String hostname;
    private transient String publicKeyFile;

    /*
     * options for configuration of JSON format
     */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject options = new JsonObject();
    @JsonIgnore
    private transient ConcurrentMap<String, DiConsumer> epsilon = new ConcurrentHashMap<>();
    @JsonIgnore
    private transient String vendorConfig;
    @JsonIgnore
    private transient String vendor;

    public String getVendorConfig() {
        return this.vendorConfig;
    }

    public void setVendorConfig(final String vendorConfig) {
        this.vendorConfig = vendorConfig;
    }

    public String getVendor() {
        return this.vendor;
    }

    public void setVendor(final String vendor) {
        this.vendor = vendor;
    }

    public ConcurrentMap<String, DiConsumer> getEpsilon() {
        return this.epsilon;
    }

    public void setEpsilon(final ConcurrentMap<String, DiConsumer> epsilon) {
        this.epsilon = epsilon;
    }

    public ConcurrentMap<String, IntegrationRequest> getApis() {
        return this.apis;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public String getPublicKeyFile() {
        return this.publicKeyFile;
    }

    public void setPublicKeyFile(final String publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    public JsonObject getOptions() {
        return this.options;
    }

    public void setOptions(final JsonObject options) {
        this.options = options;
    }

    @SuppressWarnings("unchecked")
    public <T> T getOption(final String optionKey) {
        final JsonObject options = this.getOptions();
        final Object value = options.getValue(optionKey);
        return (T) value;
    }

    public <T> T getOption(final String optionKey, final T defaultValue) {
        final T result = this.getOption(optionKey);
        return Objects.isNull(result) ? defaultValue : result;
    }

    /*
     * Control for debug
     */
    public void mockOn() {
        this.options.put("debug", true);
    }

    public void mockOff() {
        this.options.put("debug", false);
    }

    @Override
    public JsonObject toJson() {
        return Ut.serializeJson(this);
    }

    @Override
    public void fromJson(final JsonObject data) {
        this.endpoint = data.getString("endpoint");
        this.hostname = data.getString("hostname");
        this.username = data.getString("username");
        this.password = data.getString("password");
        this.protocol = data.getString("protocol");
        this.port = data.getInteger("port");
        this.publicKeyFile = data.getString("publicKeyFile");
        /*
         * Integration Request
         */
        final JsonObject apis = data.getJsonObject("apis");
        if (Ut.notNil(apis)) {
            Ut.<JsonObject>itJObject(apis, (json, field) -> {
                final IntegrationRequest request = Ut.deserialize(json, IntegrationRequest.class);
                this.apis.put(field, request);
            });
        }
        final JsonObject options = data.getJsonObject("options");
        if (Ut.notNil(options)) {
            this.options = options.copy();
        }
    }

    public IntegrationRequest createRequest(final String key) {
        final IntegrationRequest request = new IntegrationRequest();
        final IntegrationRequest original = this.apis.get(key);
        request.setHeaders(original.getHeaders().copy());
        request.setMethod(original.getMethod());
        if (0 <= original.getPath().indexOf('`')) {
            /*
             * Expression Path
             */
            request.setExecutor(this.endpoint, original.getPath());
        } else {
            /*
             * Standard Path
             */
            request.setPath(this.endpoint + original.getPath());
        }
        return request;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Integration)) {
            return false;
        }
        final Integration that = (Integration) o;
        return this.endpoint.equals(that.endpoint) &&
            this.port.equals(that.port) &&
            this.protocol.equals(that.protocol) &&
            this.username.equals(that.username) &&
            this.password.equals(that.password) &&
            this.hostname.equals(that.hostname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.endpoint, this.port, this.protocol, this.username, this.password, this.hostname);
    }

    @Override
    public String toString() {
        return "Integration{" +
            "apis=" + this.apis +
            ", vendor=" + this.vendorConfig +
            ", endpoint='" + this.endpoint + '\'' +
            ", port=" + this.port +
            ", protocol='" + this.protocol + '\'' +
            ", username='" + this.username + '\'' +
            ", password='" + this.password + '\'' +
            ", hostname='" + this.hostname + '\'' +
            ", publicKeyFile='" + this.publicKeyFile + '\'' +
            '}';
    }
}
