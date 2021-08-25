package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.actor.IxActor;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.normalize.Pre;
import io.vertx.tp.crud.uca.op.Angle;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.ApeakMy;
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
    public Future<Envelop> getFull(final Envelop envelop) {
        final JsonObject params = new JsonObject();
        params.put(KName.VIEW, Ux.getString1(envelop));         // view

        final String module = Ux.getString2(envelop);           // module
        return IxPanel.on(envelop, module)
                .input(
                        Pre.head()::setUp,      /* Header */
                        Pre.Apeak()::setUp      /* Apeak */
                )
                /*
                 * {
                 *     "identifier": "Model identifier",
                 *     "view": "The view name, if not put DEFAULT",
                 *     "dynamic": "true if use dynamic",
                 *     "sigma": "The application uniform"
                 * }
                 */
                .parallel(Angle.full()::procAsync)
                .runJ(params, (s, a) -> null);

    }

    /*
     * GET: /api/columns/{actor}/my
     */
    @Address(Addr.Get.COLUMN_MY)
    @SuppressWarnings("all")
    public Future<Envelop> getMy(final Envelop request) {
        return Ix.create(this.getClass()).input(request).envelop((dao, config) ->
                /* Get Stub */
                Ke.channelAsync(ApeakMy.class, () -> Ux.future(new JsonArray()).compose(IxHttp::success200),
                        stub -> Unity.fetchView(dao, request, config)
                                /* View parameters filling */
                                .compose(input -> IxActor.view().procAsync(input, config))
                                /* Uri filling, replace inited information: uri , method */
                                .compose(input -> IxActor.uri().bind(request).procAsync(input, config))
                                /* User filling */
                                .compose(input -> IxActor.user().bind(request).procAsync(input, config))
                                /* Fetch My Columns */
                                .compose(stub.on(dao)::fetchMy)
                                /* Return Result */
                                .compose(IxHttp::success200)));
    }
}
