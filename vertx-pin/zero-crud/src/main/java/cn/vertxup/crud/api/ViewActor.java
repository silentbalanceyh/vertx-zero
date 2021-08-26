package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.crud.uca.output.Post;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/*
 * 「新版定制完成」
 */
@Queue
public class ViewActor {

    /*
     * GET: /api/columns/{actor}/full
     */
    @Address(Addr.Get.COLUMN_FULL)
    public Future<JsonArray> getFull(final Envelop envelop) {
        final JsonObject params = new JsonObject();
        params.put(KName.VIEW, Ux.getString1(envelop));         // view
        final String module = Ux.getString2(envelop);           // module

        return T.fetchFull(envelop, module).runJ(params);
    }

    /*
     * GET: /api/columns/{actor}/my
     */
    @Address(Addr.Get.COLUMN_MY)
    @SuppressWarnings("all")
    public Future<JsonArray> getMy(final Envelop envelop) {
        final JsonObject params = new JsonObject();
        params.put(KName.VIEW, Ux.getString1(envelop));         // view
        final String module = Ux.getString2(envelop);           // module

        return IxPanel.on(envelop, module)
            .input(
                Pre.apeak(true)::inJAsync,              /* Apeak */
                Pre.head()::inJAsync                    /* Header */
            )
            /*
             * {
             *     "view": "The view name, if not put DEFAULT",
             *     "uri": "http path",
             *     "method": "http method",
             *     "sigma": "The application uniform"
             * }
             */
            .parallel(/* Active */Agonic.apeak(true)::runJAAsync, null)
            .output(/* Columns connected */Post.apeak(true)::outAsync)
            .runJ(params);
    }
}

class T {
    /*
     * Shared Method mask as static method for two usage
     */
    static IxPanel fetchFull(final Envelop envelop, final String module) {
        return IxPanel.on(envelop, module)
            .input(
                Pre.apeak(false)::inJAsync,             /* Apeak */
                Pre.head()::inJAsync                    /* Header */
            )
            /*
             * {
             *     "identifier": "Model identifier",
             *     "view": "The view name, if not put DEFAULT",
             *     "dynamic": "true if use dynamic",
             *     "sigma": "The application uniform"
             * }
             */
            .parallel(/* Active */Agonic.apeak(false)::runJAAsync)
            .output(/* Columns connected */Post.apeak(false)::outAsync);
    }
}
