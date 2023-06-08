package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.mod.crud.cv.Addr;
import io.vertx.mod.crud.cv.em.ApiSpec;
import io.vertx.mod.crud.uca.desk.IxPanel;
import io.vertx.mod.crud.uca.desk.IxWeb;
import io.vertx.mod.crud.uca.input.Pre;
import io.vertx.mod.crud.uca.next.Co;
import io.vertx.mod.crud.uca.op.Agonic;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

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
        final IxWeb request = IxWeb.create(ApiSpec.BODY_NONE).build(envelop);
        return T.fetchFull(request).runJ(request.dataV());
    }

    /*
     * GET: /api/columns/{actor}/my
     */
    @Address(Addr.Get.COLUMN_MY)
    @SuppressWarnings("all")
    public Future<JsonArray> getMy(final Envelop envelop) {
        final IxWeb request = IxWeb.create(ApiSpec.BODY_NONE).build(envelop);
        return IxPanel.on(request)
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
            .output(/* Columns connected */Co.endV(true)::ok)
            .runJ(request.dataV());
    }
}

class T {
    /*
     * Shared Method mask as static method for two usage
     */
    @SuppressWarnings("all")
    static IxPanel fetchFull(final IxWeb request) {
        return IxPanel.on(request)
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
            .output(/* Columns connected */Co.endV(false)::ok);
    }
}
