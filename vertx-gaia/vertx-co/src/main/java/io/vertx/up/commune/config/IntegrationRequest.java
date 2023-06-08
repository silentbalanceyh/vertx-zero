package io.vertx.up.commune.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/*
 * IntegrationRequest for api description
 */
public class IntegrationRequest implements Serializable {
    /*
     * Http uri definition here.
     */
    private String path;
    /*
     * Http method
     */
    private HttpMethod method;
    /*
     * Some specific situation that required headers
     */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject headers = new JsonObject();

    @JsonIgnore
    private Function<JsonObject, String> executor;

    public String getPath() {
        return this.path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public String getPath(final JsonObject params) {
        if (Objects.nonNull(this.executor)) {
            final String delayPath = this.executor.apply(params);
            this.path = delayPath;
            return delayPath;
        } else {
            return null;
        }
    }

    public void setExecutor(final String endpoint, final String expr) {
        this.executor = params -> {
            final JsonObject normalized = Ut.valueJObject(params);
            final String result = Ut.fromExpression(expr, normalized);
            return endpoint + result;
        };
    }

    public boolean isExpr() {
        return Objects.nonNull(this.executor);
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public void setMethod(final HttpMethod method) {
        this.method = method;
    }

    public JsonObject getHeaders() {
        return this.headers;
    }

    public void setHeaders(final JsonObject headers) {
        this.headers = headers;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntegrationRequest)) {
            return false;
        }
        final IntegrationRequest request = (IntegrationRequest) o;
        return this.path.equals(request.path) &&
            this.method == request.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path, this.method);
    }

    @Override
    public String toString() {
        return "IntegrationRequest{" +
            "path='" + this.path + '\'' +
            ", method=" + this.method +
            ", headers=" + this.headers +
            '}';
    }
}
