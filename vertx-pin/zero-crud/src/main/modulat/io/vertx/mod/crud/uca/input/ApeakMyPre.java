package io.vertx.mod.crud.uca.input;

import io.aeon.experiment.specification.KColumn;
import io.aeon.experiment.specification.KModule;
import io.horizon.atom.common.Kv;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.refine.Ix;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ApeakMyPre extends ApeakPre {
    /*
     *
     * This method is for uniform safeCall for Future<JsonArray> returned
     * It's shared by
     * /api/columns/{actor}/full
     * /api/columns/{actor}/my
     * Because all of above api returned JsonArray of columns on model
     *
     * Uri, Method instead
     * This method is only for save my columns, it provided fixed impact uri for clean cache
     * 1) Save my columns
     * 2) Clean up impact uri about cache flush
     * {
     *      "uri": "",
     *      "method": "",
     *      "view": "view"
     * }
     */
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        final KModule module = in.module();
        /* Column definition */
        final KColumn column = module.getColumn();
        assert null != column : "The column definition should not be null";
        this.viewProc(data, column);
        /*
         * Uri and method
         */
        final Kv<String, HttpMethod> impactUri = Ix.onImpact(in);
        return Ux.future(data
            .put(KName.URI, impactUri.key())
            .put(KName.METHOD, impactUri.value().name()));
    }
}
