package io.vertx.tp.rbac.extension;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.eon.KName;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.commune.Envelop;
import io.vertx.up.extension.PlugAuditor;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.util.Objects;

public class AuditorPin implements PlugAuditor {
    private final static Annal LOGGER = Annal.get(AuditorPin.class);
    private final transient JsonObject config = new JsonObject();

    @Override
    public PlugAuditor bind(final JsonObject config) {
        if (Ut.notNil(config)) {
            this.config.mergeIn(config);
        }
        return this;
    }

    @Override
    public Future<Envelop> audit(final RoutingContext context,
                                 final Envelop envelop) {
        final HttpServerRequest request = context.request();
        if (this.isValid(request)) {
            final HttpMethod method = request.method();
            /* Get user id */
            final String userId = Ke.keyUser(envelop);
            final Instant instant = Instant.now();
            /*
             * counter is not 0, it means match
             * Find the first JsonObject instead of provide index value here
             */
            if (HttpMethod.POST == method) {
                /*
                 * /api/xxx
                 * The method definition
                 * method(JsonObject data)
                 */
                envelop.value(KName.CREATED_BY, userId);
                envelop.value(KName.CREATED_AT, instant);
                envelop.value(KName.UPDATED_BY, userId);
                envelop.value(KName.UPDATED_AT, instant);
                Sc.infoAudit(LOGGER, "Full auditing: userId = `{0}`, at = `{1}`", userId, instant.toString());
            } else {
                /*
                 * /api/xxx
                 * The method definition
                 * method(String, JsonObject)
                 */
                envelop.value(KName.UPDATED_BY, userId);
                envelop.value(KName.UPDATED_AT, instant);
                Sc.infoAudit(LOGGER, "Update auditing: userId = `{0}`, at = `{1}`", userId, instant.toString());
            }
        } else {
            Sc.debugAuth(LOGGER, "Do not match: {0}", request.path());
        }
        return Ux.future(envelop);
    }

    private boolean isValid(final HttpServerRequest request) {
        final JsonArray include = this.config.getJsonArray("include");
        if (Objects.isNull(include) || include.isEmpty()) {
            /*
             * Must set `include` and `exclude`
             */
            return false;
        }
        final HttpMethod method = request.method();
        if (HttpMethod.PUT != method && HttpMethod.POST != method) {
            /*
             * Must be impact on `PUT` or `POST`
             */
            return false;
        }
        final String path = request.path();
        final long counter = include.stream().filter(Objects::nonNull)
                .map(item -> (String) item)
                .filter(path::startsWith)
                .count();
        final JsonArray exclude = this.config.getJsonArray("exclude");
        if (Objects.isNull(exclude) || exclude.isEmpty()) {
            /*
             * Exclude counter = 0, only include valid
             */
            return 0 < counter;
        } else {
            final long except = exclude.stream().filter(Objects::nonNull)
                    .map(item -> (String) item)
                    .filter(path::startsWith)
                    .count();
            return 0 < counter && except <= 0;
        }
    }
}
