package io.vertx.tp.rbac.atom;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeDefault;
import io.vertx.up.eon.KName;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.tp.rbac.permission.ScPrivilege;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.eon.ID;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.io.Serializable;

public class ScRequest implements Serializable {

    private static final ScConfig CONFIG = ScPin.getConfig();
    private static final Annal LOGGER = Annal.get(ScRequest.class);

    private transient final String uri;
    private transient final String requestUri;
    private transient final String sigma;
    private transient final String user;
    private transient final String sessionId;
    private transient final String view = KeDefault.VIEW_DEFAULT;
    private transient final HttpMethod method;

    /*
     * {
     *     "metadata" : {
     *         "uri" : "xxx",
     *         "requestUri" : "xxx",
     *         "method" : "GET"
     *     },
     *     "jwt" : "xxxxx",
     *     "headers" : {
     *         "X-Sigma" : "xxx"
     *     },
     *     "options" : { }
     * }
     */
    public ScRequest(final JsonObject data) {
        final JsonObject metadata = data.getJsonObject(AuthKey.F_METADATA);
        final String uri = metadata.getString(AuthKey.F_URI);
        this.requestUri = metadata.getString(AuthKey.F_URI_REQUEST);
        this.method = HttpMethod.valueOf(metadata.getString(AuthKey.F_METHOD));
        /*
         * Extension for orbit
         */
        this.uri = Sc.uri(uri, this.requestUri);
        /*
         * Support multi applications
         */
        if (CONFIG.getSupportMultiApp()) {
            final JsonObject headers = data.getJsonObject(AuthKey.F_HEADERS);
            this.sigma = headers.getString(ID.Header.X_SIGMA);
        } else {
            this.sigma = null;
        }
        /*
         * Token analyze
         */
        final String token = data.getString("jwt");
        final JsonObject userData = Ux.Jwt.extract(token);
        this.user = userData.getString("user");
        this.sessionId = userData.getString(KName.HABITUS);
    }

    public String getNormalizedUri() {
        return this.uri;
    }

    public boolean normalized() {
        return !this.requestUri.equals(this.uri);
    }

    public String getRequestUri() {
        return this.requestUri;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getSigma() {
        return this.sigma;
    }

    public String getUser() {
        return this.user;
    }

    public String getView() {
        return this.view;
    }

    public String getCacheKey() {
        return Ke.keySession(this.method.name(), this.uri);
    }

    public String getAuthorizedKey() {
        return Ke.keyAuthorized(this.method.name(), this.uri);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public Future<ScPrivilege> openSession() {
        LOGGER.debug("Open session: {0}", this.sessionId);
        return ScPrivilege.open(this.sessionId);
    }
}
