package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.cv.em.ApiSpec;
import io.vertx.tp.crud.uca.desk.IxKit;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.desk.IxWeb;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.crud.uca.tran.Co;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

@Queue
@SuppressWarnings("all")
public class PutActor {

    @Address(Addr.Put.BY_ID)
    public Future<Envelop> update(final Envelop envelop) {
        /* Module and Key Extract  */
        final IxWeb request = IxWeb.create(ApiSpec.BODY_WITH_KEY).build(envelop);
        return IxPanel.on(request)
            .input(
                Pre.head()::inJAsync,                       /* Header */
                Pre.codex()::inJAsync                       /* Codex */
            )
            .next(in -> Co.nextJ(in)::next)
            .passion(Agonic.write(ChangeFlag.UPDATE)::runJAsync)
            .<JsonObject, JsonObject, JsonObject>runJ(request.dataK())
            /*
             * 404 / 200
             */
            .compose(IxKit::successPost);
    }

    @Address(Addr.Put.BATCH)
    public Future<Envelop> updateBatch(final Envelop envelop) {
        /*
         * IxPanel processing building to split mass update
         * */
        final IxWeb request = IxWeb.create(ApiSpec.BODY_ARRAY).build(envelop);
        final IxPanel panel = IxPanel.on(request);
        return Pre.qPk().inAJAsync(request.dataA(), panel.active()).compose(condition -> {
            final JsonObject params = new JsonObject();
            /*
             * IxPanel
             */
            params.put(KName.DATA, request.dataA());
            params.put(Qr.KEY_CRITERIA, condition);
            return panel
                .next(in -> Co.nextQ(in)::next)
                .passion(Agonic.write(ChangeFlag.UPDATE)::runJAAsync)
                .runJ(params);
        });
    }

    @Address(Addr.Put.COLUMN_MY)
    public Future<JsonObject> updateColumn(final Envelop envelop) {
        final IxWeb request = IxWeb.create(ApiSpec.BODY_JSON).build(envelop);
        /*
         * Fix issue of Data Region
         * Because `projection` and `criteria` are both spec
         * params
         * */
        final JsonObject params = request.dataV();
        final JsonObject viewData = request.dataJ();
        params.put(KName.DATA, Ut.sureJObject(viewData));
        params.put(KName.URI_IMPACT, viewData.getString(KName.URI_IMPACT));
        return IxPanel.on(request)
            .input(
                Pre.apeak(true)::inJAsync,              /* Apeak */
                Pre.head()::inJAsync                    /* Header */
            )
            .passion(Agonic.view()::runJAsync, null)
            .runJ(params);
    }
}
