package io.vertx.up.commune;

import io.horizon.eon.VString;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.up.commune.envelop.Rib;
import io.vertx.up.commune.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.fn.Fn;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * Envelop Assist for additional Data here.
 */
class Assist implements Serializable {
    private final Map<String, Object> context = new HashMap<>();
    /* Fixed header null dot */
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
        return Fn.failOr(VString.EMPTY, () -> {
            final JsonObject credential = this.user.principal();
            return Fn.bugOr(null != credential && credential.containsKey(field),
                () -> credential.getString(field),
                () -> VString.EMPTY);
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

    JsonObject requestSmart() {
        final Object[] arguments = this.reference.get(KWeb.ARGS.REQUEST_CACHED);
        final JsonObject argumentJ = new JsonObject();
        // Path + Query ( Low Priority )
        // 如果出现 view 参数，则需要被 Vis 覆盖
        this.reference.pathParams().forEach(argumentJ::put);
        this.reference.queryParams().forEach(argumentJ::put);
        // Iterate each arguments to check the `JsonObject`
        Arrays.stream(arguments).forEach(value -> {
            if (value instanceof final Vis vis) {
                // Vis ( Inherit from JsonObject )
                argumentJ.put(KName.VIEW, vis.view());
                argumentJ.put(KName.POSITION, vis.position());
            } else if (value instanceof final JsonObject json) {
                argumentJ.mergeIn(json, false);
            }
        });
        return argumentJ;
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
