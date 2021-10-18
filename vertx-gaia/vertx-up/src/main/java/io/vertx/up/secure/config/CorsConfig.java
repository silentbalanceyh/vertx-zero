package io.vertx.up.secure.config;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.ID;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/*
 * Cors configuration here.
 * The data came from `secure -> cors`
 */
public class CorsConfig implements Serializable {
    private static final String KEY = "cors";
    private static CorsConfig INSTANCE;

    static {
        final Node<JsonObject> visitor = Ut.singleton(ZeroUniform.class);
        final JsonObject config = visitor.read();
        if (config.containsKey(KEY)) {
            final JsonObject cors = config.getJsonObject(KEY);
            if (Ut.notNil(cors)) {
                INSTANCE = Ut.deserialize(cors, CorsConfig.class);
            }
        }
        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new CorsConfig();
        }
    }

    private transient Boolean credentials = Boolean.FALSE;
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray methods = new JsonArray();
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray headers = new JsonArray();
    private transient String origin;

    public static CorsConfig get() {
        return INSTANCE;
    }

    public Boolean getCredentials() {
        return this.credentials;
    }

    public void setCredentials(final Boolean credentials) {
        this.credentials = credentials;
    }

    public JsonArray getMethods() {
        if (this.methods.isEmpty()) {
            return new JsonArray()
                .add(HttpMethod.GET.name())
                .add(HttpMethod.POST.name())
                .add(HttpMethod.PUT.name())
                .add(HttpMethod.DELETE.name())
                .add(HttpMethod.OPTIONS.name());
        } else {
            return this.methods;
        }
    }

    public void setMethods(final JsonArray methods) {
        this.methods = methods;
    }

    public JsonArray getHeaders() {
        if (this.headers.isEmpty()) {
            return new JsonArray()
                .add(HttpHeaders.AUTHORIZATION.toString())
                .add(HttpHeaders.ACCEPT.toString())
                .add(HttpHeaders.CONTENT_DISPOSITION.toString())
                .add(HttpHeaders.CONTENT_ENCODING.toString())
                .add(HttpHeaders.CONTENT_LENGTH.toString())
                .add(HttpHeaders.CONTENT_TYPE.toString())
                /* User defined header */
                .add(ID.Header.X_APP_ID)
                .add(ID.Header.X_APP_KEY)
                .add(ID.Header.X_SIGMA);
        } else {
            return this.headers;
        }
    }

    public void setHeaders(final JsonArray headers) {
        this.headers = headers;
    }

    public String getOrigin() {
        return Objects.isNull(this.origin) ? "*" : this.origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "CorsConfig{" +
            "credentials=" + this.credentials +
            ", methods=" + this.methods +
            ", headers=" + this.headers +
            ", origin='" + this.origin + '\'' +
            '}';
    }
}
