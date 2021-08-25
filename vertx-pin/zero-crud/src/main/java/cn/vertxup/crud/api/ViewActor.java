package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.op.Angle;
import io.vertx.tp.crud.uca.output.Post;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

@Queue
public class ViewActor {
    /*
     * GET: /api/columns/{actor}/full
     */
    @Address(Addr.Get.COLUMN_FULL)
    @SuppressWarnings("unchecked")
    public Future<JsonArray> getFull(final Envelop envelop) {
        final JsonObject params = new JsonObject();
        params.put(KName.VIEW, Ux.getString1(envelop));         // view

        final String module = Ux.getString2(envelop);           // module

        return IxPanel.on(envelop, module)
                .input(
                        /* Apeak */
                        Pre.apeak(false)::inAsync,
                        /* Header */
                        Pre.head()::inAsync
                )
                /*
                 * {
                 *     "identifier": "Model identifier",
                 *     "view": "The view name, if not put DEFAULT",
                 *     "dynamic": "true if use dynamic",
                 *     "sigma": "The application uniform"
                 * }
                 */
                .parallel(/* Active */Angle.apeak(false)::runAsync)
                .output(
                        /* Columns connected */
                        Post.apeak()::outAsync
                )
                .runJ(params);
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
                        /* Apeak */
                        Pre.apeak(true)::inAsync,
                        /* Header */
                        Pre.head()::inAsync
                )
                /*
                 * {
                 *     "view": "The view name, if not put DEFAULT",
                 *     "uri": "http path",
                 *     "method": "http method",
                 *     "sigma": "The application uniform"
                 * }
                 */
                .parallel(/* Active */Angle.apeak(true)::runAsync, null)
                .runJ(params);
    }
}
