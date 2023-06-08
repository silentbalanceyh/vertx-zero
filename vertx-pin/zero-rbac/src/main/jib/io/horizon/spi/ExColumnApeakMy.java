package io.horizon.spi;

import io.horizon.spi.ui.Anchoret;
import io.horizon.spi.ui.ApeakMy;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.acl.rapier.Quinn;
import io.vertx.mod.rbac.atom.ScOwner;
import io.vertx.mod.rbac.cv.AuthMsg;
import io.vertx.mod.rbac.cv.em.OwnerType;
import io.vertx.mod.rbac.logged.ScUser;
import io.vertx.up.commune.secure.DataBound;
import io.vertx.up.commune.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import static io.vertx.mod.rbac.refine.Sc.LOG;

public class ExColumnApeakMy extends Anchoret<ApeakMy> implements ApeakMy {

    @Override
    public Future<JsonArray> fetchMy(final JsonObject params) {
        final String resourceId = params.getString(ARG0);
        if (Ut.isNil(resourceId)) {
            return Ux.futureA();
        }
        final String userId = params.getString(ARG1);
        final Vis view = Vis.smart(params.getValue(ARG2));
        // DataBound Building
        final ScOwner owner = new ScOwner(userId, OwnerType.USER);
        owner.bind(view);
        // ScResource building
        return Quinn.vivid().<DataBound>fetchAsync(resourceId, owner).compose(bound -> {
            final JsonArray projection = bound.vProjection();
            /*
             * No view found                        -> []
             * View found and get projection        -> [?,?,...]
             */
            return Ux.future(projection);
        });
    }

    @Override
    public Future<JsonObject> saveMy(final JsonObject params, final JsonObject viewInput) {
        final String resourceId = params.getString(ARG0);
        if (Ut.isNil(resourceId)) {
            return Ux.futureJ();
        }
        final String userId = params.getString(ARG1);
        /* Normalize data for `language` and `sigma` etc.*/
        final JsonObject viewData = params.copy();

        final ScOwner owner = new ScOwner(userId, OwnerType.USER);
        final Vis vis = Vis.smart(viewData.getValue(KName.VIEW));
        owner.bind(vis);
        /* Two Params: projection, criteria, rows */
        Ut.valueCopy(viewData, viewInput,
            Ir.KEY_PROJECTION,
            Ir.KEY_CRITERIA,
            KName.Rbac.ROWS
        );
        viewData.put(KName.UPDATED_BY, userId);     // updatedBy = userId
        /* Save View */
        return Quinn.vivid().<JsonObject>saveAsync(resourceId, owner, viewData)
            /*
             * Flush cache of session on impacted uri
             * This method is for projection refresh here
             * /api/columns/{actor}/my -> save projection on
             * /api/{actor}/search
             * This impact will be in time when this method called.
             * The method is used in this class only and could not be shared.
             */
            .compose(flushed -> this.flushImpact(params, flushed))
            /*
             * Here should flush the key of
             */
            .compose(flushed -> this.flushMy(params, flushed));
    }

    private Future<JsonObject> flushMy(final JsonObject params, final JsonObject updated) {

        return Ux.futureJ(updated);
    }

    private Future<JsonObject> flushImpact(final JsonObject params, final JsonObject updated) {
        /*
         * ScHabitus instance
         */
        final String habitus = params.getString(ARG3);
        final ScUser user = ScUser.login(habitus);
        /*
         * Method / Uri
         */
        final String dataKey = params.getString(ARG4);
        /*
         * projection / criteria only
         */
        final JsonObject updatedData = new JsonObject();
        updatedData.put(Ir.KEY_PROJECTION, updated.getJsonArray(Ir.KEY_PROJECTION));
        updatedData.put(Ir.KEY_CRITERIA, updated.getJsonObject(Ir.KEY_CRITERIA));
        return user.view(dataKey, updatedData).compose(nil -> {
            LOG.Auth.info(this.getLogger(), AuthMsg.REGION_FLUSH, habitus, dataKey,
                nil.getJsonObject(dataKey, new JsonObject()).encodePrettily());
            return Ux.future(updated);
        });
    }
}
