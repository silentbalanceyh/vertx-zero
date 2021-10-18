package io.vertx.up.commune;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.up.commune.envelop.Rib;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
 * Envelop Assist for additional Data here.
 */
class Assist implements Serializable {
    private final Map<String, Object> context = new HashMap<>();
    /* Fixed header null pointer */
    private MultiMap headers = MultiMap.caseInsensitiveMultiMap();
    private User user;
    private String uri;
    private HttpMethod method;
    private Session session;
    private RoutingContext reference;

    <T> T getContextData(final String key, final Class<?> clazz) {
        T reference = null;
        if (this.context.containsKey(key)) {
            reference = Rib.deserialize(this.context.get(key), clazz);
        }
        return reference;
    }

    @SuppressWarnings("all")
    String principal(final String field) {
        return Fn.getJvm(Strings.EMPTY, () -> {
            final JsonObject credential = this.user.principal();
            return Fn.getSemi(null != credential && credential.containsKey(field),
                () -> credential.getString(field),
                () -> Strings.EMPTY);
        }, this.user);
    }

    void bind(final RoutingContext context) {
        this.reference = context;
    }

    RoutingContext reference() {
        return this.reference;
    }

    User user() {
        return this.user;
    }

    void user(final User user) {
        this.user = user;
    }

    MultiMap headers() {
        return this.headers;
    }

    void headers(final MultiMap headers) {
        this.headers = headers;
    }

    Session session() {
        return this.session;
    }

    void session(final Session session) {
        this.session = session;
    }

    String uri() {
        return this.uri;
    }

    void uri(final String uri) {
        this.uri = uri;
    }

    HttpMethod method() {
        return this.method;
    }

    void method(final HttpMethod method) {
        this.method = method;
    }

    void context(final Map<String, Object> data) {
        this.context.clear();
        this.context.putAll(data);
    }

    @Override
    public String toString() {
        return "Assist{" +
            // Stack Overflow here
            // "context=" + this.context +
            ", headers=" + this.headers +
            ", user=" + this.user +
            ", uri='" + this.uri + '\'' +
            ", method=" + this.method +
            ", session=" + this.session +
            '}';
    }
}
