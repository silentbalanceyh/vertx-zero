package io.vertx.tp.optic.phantom;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExIo;
import io.vertx.tp.optic.feature.Arbor;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractArbor implements Arbor {

    protected Future<JsonArray> ensureChildren(final JsonObject category, final JsonArray children, final JsonObject configuration) {
        /*
         * Extract data to calculate the default `store`
         * The input JsonArray should be as following
         * - arg0, JsonArray of data
         * - arg1, JsonObject of configuration
         */
        final JsonObject store = configuration.getJsonObject(KName.STORE);
        final JsonArray normalized = new JsonArray();
        normalized.add(this.storePathOn(category, store));
        Ut.itJArray(children).map(json -> this.storePathOn(json, store)).forEach(normalized::add);
        return Ke.channel(ExIo.class, () -> children, stub -> stub.mkdir(normalized, store));
    }

    protected Future<JsonArray> ensureChildren(final JsonObject category, final JsonObject configuration) {
        return this.ensureChildren(category, null, configuration);
    }

    /*
     * {
     *      "storeRoot": "",
     *      "storePath": ""
     * }
     */
    private JsonObject storePathOn(final JsonObject record, final JsonObject store) {
        final JsonObject storeInfo = store.copy();
        /*
         * Re-Calculate `storePath`
         */
        final String name = record.getString(KName.NAME);
        final String storePath = store.getString(KName.STORE_PATH);
        storeInfo.put(KName.STORE_PATH, Ut.ioPath(storePath, name));
        record.put(KName.STORE, storeInfo);
        return record;
    }
}
