package io.vertx.tp.rbac.logged;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.eon.ID;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxPool;

/**
 * For annotation @AuthorizedResource to stored resource data structure
 * 1. The key: resource-[method]-[uri]
 * 2. The profile key could be calculated
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ScResource {
    private static final ScConfig CONFIG = ScPin.getConfig();
    private transient final UxPool pool;

    private transient final String resourceKey;
    private transient final String uri;
    private transient final String requestUri;
    private transient final String sigma;
    private transient final Vis view;
    private transient final HttpMethod method;

    /*
     * 1. Fetch resource from cache
     * 2. When cache is null, fetch data from database here.
     * {
     *     "metadata" : {
     *         "uri" : "xxx",
     *         "requestUri" : "xxx",
     *         "method" : "GET"
     *     },
     *     "access_token" : "xxxxx",
     *     "headers" : {
     *         "X-Sigma" : "xxx"
     *     },
     *     "options" : { }
     * }
     */
    private ScResource(final JsonObject data) {
        final JsonObject metadata = data.getJsonObject(AuthKey.F_METADATA);
        final String uri = metadata.getString(AuthKey.F_URI);
        this.requestUri = metadata.getString(AuthKey.F_URI_REQUEST);
        this.method = HttpMethod.valueOf(metadata.getString(AuthKey.F_METHOD));
        /*
         * Extension for orbit
         */
        this.uri = Ke.uri(uri, this.requestUri);
        this.view = Vis.smart(metadata.getValue(KName.VIEW));
        /*
         * Support multi applications
         */
        if (CONFIG.getSupportMultiApp()) {
            final JsonObject headers = data.getJsonObject(AuthKey.F_HEADERS);
            this.sigma = headers.getString(ID.Header.X_SIGMA);
        } else {
            this.sigma = null;
        }
        this.resourceKey = Ke.keyResource(this.method.name(), this.uri);
        this.pool = Ux.Pool.on(CONFIG.getResourcePool());
    }

    public static ScResource create(final JsonObject data) {
        return new ScResource(data);
    }

    public String uri() {
        return this.uri;
    }

    public String uriRequest() {
        return this.requestUri;
    }

    public String sigma() {
        return this.sigma;
    }

    public Vis view() {
        return this.view;
    }

    public String key() {
        return this.resourceKey;
    }

    public String keyView() {
        return Ke.keyView(this.method.name(), this.uri, this.view);
    }

    public HttpMethod method() {
        return this.method;
    }

    public boolean isNormalized() {
        return !this.requestUri.equals(this.uri);
    }

    // ------------------------- Resource Fetch ------------------------
    /*
     * Fetch PROFILE -> PERMISSIONS
     * {
     *      "profile": {
     *          "profileName": ["p1", "p2"]
     *      },
     *      "record": {
     *          "key": "xxxx"
     *      }
     * }
     */
    public Future<JsonObject> resource() {
        return this.pool.get(this.resourceKey);
    }

    public Future<JsonObject> resource(final JsonObject data) {
        return this.pool.put(this.resourceKey, data)
            .compose(item -> Ux.future(item.getValue()));
    }
}
