package io.vertx.mod.rbac.extension;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mod.ke.cv.KeIpc;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.extension.PlugAuditor;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.unity.Ux;

import java.time.Instant;
import java.util.Objects;

import static io.vertx.mod.rbac.refine.Sc.LOG;

public class AuditorPin implements PlugAuditor {
    private final static Annal LOGGER = Annal.get(AuditorPin.class);
    private final transient JsonObject config = new JsonObject();

    @Override
    public PlugAuditor bind(final JsonObject config) {
        final JsonObject auditor;
        if (Objects.isNull(config)) {
            auditor = new JsonObject();
        } else {
            auditor = config.copy();
        }
        /*
         * Configured for empty config
         */
        final JsonArray include = auditor.getJsonArray(
            YmlCore.extension.auditor.config.INCLUDE, new JsonArray());
        include.addAll(KeIpc.Audit.INCLUDE);
        auditor.put(
            YmlCore.extension.auditor.config.INCLUDE, include);

        final JsonArray exclude = auditor.getJsonArray(
            YmlCore.extension.auditor.config.EXCLUDE, new JsonArray());
        exclude.addAll(KeIpc.Audit.EXCLUDE);
        auditor.put(
            YmlCore.extension.auditor.config.EXCLUDE, exclude);
        this.config.mergeIn(auditor);
        return this;
    }

    @Override
    public Future<Envelop> audit(final RoutingContext context,
                                 final Envelop envelop) {
        final HttpServerRequest request = context.request();
        if (this.isValid(request)) {
            final HttpMethod method = request.method();
            /* Get user id */
            final String userId = envelop.userId();
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
                LOG.Audit.info(LOGGER, "Full auditing: userId = `{0}`, at = `{1}`", userId, instant.toString());
            } else {
                /*
                 * /api/xxx
                 * The method definition
                 * method(String, JsonObject)
                 */
                envelop.value(KName.UPDATED_BY, userId);
                envelop.value(KName.UPDATED_AT, instant);
                LOG.Audit.info(LOGGER, "Update auditing: userId = `{0}`, at = `{1}`", userId, instant.toString());
            }
        } else {
            LOG.Auth.debug(LOGGER, "Do not match: {0}", request.path());
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
        final String recovery = ZeroAnno.recoveryUri(request.path(), request.method());
        if (Objects.isNull(exclude) || exclude.isEmpty()) {
            /*
             * Exclude counter = 0, only include valid
             */
            return 0 < counter;
        } else {
            final long except = exclude.stream().filter(Objects::nonNull)
                .map(item -> (String) item)
                .filter(recovery::startsWith)
                .count();
            return 0 < counter && except <= 0;
        }
    }
}
