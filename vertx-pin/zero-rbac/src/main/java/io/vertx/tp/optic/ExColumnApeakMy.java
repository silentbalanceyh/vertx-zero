package io.vertx.tp.optic;

import cn.vertxup.rbac.service.view.ViewService;
import cn.vertxup.rbac.service.view.ViewStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.fantom.Anchoret;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.permission.ScHabitus;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Function;

public class ExColumnApeakMy extends Anchoret<ApeakMy> implements ApeakMy {

    private final transient ViewStub stub = Ut.singleton(ViewService.class);

    @Override
    public Future<JsonArray> fetchMy(final JsonObject params) {
        final String resourceId = params.getString(ARG0);
        if (Ut.isNil(resourceId)) {
            return Ux.futureA();
        }
        final String userId = params.getString(ARG1);
        final String view = params.getString(ARG2);
        return this.stub.fetchMatrix(userId, resourceId, view)
            .compose(queried -> Objects.isNull(queried) ?
                /* No view found */
                Ux.future(new JsonArray()) :
                /* View found and get projection */
                Ux.future(Ut.toJArray(queried.getProjection()))
            );
    }

    @Override
    public Future<JsonObject> saveMy(final JsonObject params, final JsonObject viewInput) {
        final String resourceId = params.getString(ARG0);
        if (Ut.isNil(resourceId)) {
            return Ux.futureJ();
        }
        final String userId = params.getString(ARG1);
        final String view = params.getString(ARG2);
        /*
         * Normalize data for `language` and `sigma` etc.
         */
        final JsonObject viewData = params.copy();
        viewData.put(ARG2, view);
        /*
         * Two Params
         */
        final JsonArray projection = viewInput.getJsonArray(Qr.KEY_PROJECTION);
        final JsonObject criteria = viewInput.getJsonObject(Qr.KEY_CRITERIA);
        /* Save View */
        return this.stub.saveMatrix(userId, viewData.put(ARG0, resourceId), projection, criteria)
            /*
             * Flush cache of session on impacted uri
             * This method is for projection refresh here
             * /api/columns/{actor}/my -> save projection on
             * /api/{actor}/search
             * This impact will be in time when this method called.
             * The method is used in this class only and could not be shared.
             */
            .compose(flushed -> this.flush(params, flushed));
    }

    private Future<JsonObject> flush(final JsonObject params, final JsonObject updated) {
        /*
         * ScHabitus instance
         */
        final String habitus = params.getString(ARG3);
        final ScHabitus habit = ScHabitus.initialize(habitus);
        /*
         * Method / Uri
         */
        final String dataKey = params.getString(ARG4);

        return habit.<JsonObject>get(dataKey).compose(stored -> {
            if (Objects.isNull(stored)) {
                return Ux.future(updated);
            } else {
                final JsonObject updatedJson = stored.copy();
                updatedJson.put(Qr.KEY_PROJECTION, updated.getJsonArray(Qr.KEY_PROJECTION));
                updatedJson.put(Qr.KEY_CRITERIA, updated.getJsonObject(Qr.KEY_CRITERIA));
                return habit.set(dataKey, updatedJson)
                    .compose(retured -> {
                        Sc.infoAuth(this.getLogger(), AuthMsg.REGION_FLUSH, habitus, dataKey,
                            stored.encodePrettily(), retured.encodePrettily());
                        return Ux.future(updated);
                    });
            }
        });
    }

    /*
     * consumer: resourceId
     */
    private Future<JsonArray> uniform(final JsonObject params, final Function<String, Future<JsonArray>> function) {
        final String resourceId = params.getString(ARG0);
        if (Ut.isNil(resourceId)) {
            return Ux.future(new JsonArray());
        } else {
            return function.apply(resourceId);
        }
    }
}
