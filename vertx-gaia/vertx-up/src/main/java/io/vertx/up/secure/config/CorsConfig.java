package io.vertx.up.secure.config;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.horizon.uca.log.Annal;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.up.eon.KWeb;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.runtime.env.MatureOn;

import java.io.Serializable;
import java.util.Objects;

/*
 * Cors configuration here.
 * The data came from `secure -> cors`
 */
public class CorsConfig implements Serializable {
    private static final Annal LOGGER = Annal.get(CorsConfig.class);
    private static CorsConfig INSTANCE;

    static {
        if (ZeroStore.is(YmlCore.cors.__KEY)) {
            INSTANCE = ZeroStore.option(YmlCore.cors.__KEY, CorsConfig.class, null);
            // Cors Connected
            final JsonArray origins = MatureOn.envDomain(INSTANCE.getOrigin());
            INSTANCE.setOrigin(origins);
            LOGGER.info("[ CORS ] Origin Configured = {0}", INSTANCE.getOrigin());
        }
        if (Objects.isNull(INSTANCE)) {
            INSTANCE = new CorsConfig();
        }
    }

    private Boolean credentials = Boolean.FALSE;
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray methods = new JsonArray();
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray headers = new JsonArray();
    /*
     * Modified from 4.3.1, here the origin has been modified
     * From Vert.x 4.3.1, instead the origin must be configured
     */
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray origin = new JsonArray();

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
                .add(KWeb.HEADER.X_APP_ID)
                .add(KWeb.HEADER.X_APP_KEY)
                .add(KWeb.HEADER.X_SIGMA);
        } else {
            return this.headers;
        }
    }

    public void setHeaders(final JsonArray headers) {
        this.headers = headers;
    }

    /*
     * This issue came from frontend:
     * Access to fetch at 'http://xxx:xxx/app/name/xxx?name=xxx'
     * from origin 'http://xxx:xxx' has been blocked by CORS policy:
     * Response to preflight request doesn't pass access control check:
     * No 'Access-Control-Allow-Origin' header is present on the requested resource.
     * If an opaque response serves your needs,
     * set the request's mode to 'no-cors' to fetch the resource with CORS disabled.
     */
    public JsonArray getOrigin() {
        return this.origin;
    }

    public void setOrigin(final JsonArray origin) {
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
