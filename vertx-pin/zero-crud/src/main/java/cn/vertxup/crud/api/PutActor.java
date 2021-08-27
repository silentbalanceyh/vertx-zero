package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.next.WJoin;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.crud.uca.output.Post;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;

@Queue
public class PutActor {

    @Address(Addr.Put.BY_ID)
    public Future<Envelop> update(final Envelop envelop) {
        /* Module and Key Extract  */
        final JsonObject body = Ux.getJson2(envelop);
        final String key = Ux.getString1(envelop);
        body.put(KName.KEY, key);
        return IxPanel.on(envelop, null)
            .input(
                Pre.head()::inJAsync,                       /* Header */
                Pre.codex()::inJAsync                       /* Codex */
            )
            .next(in -> WJoin.on(in)::runJAsync)
            .passion(Agonic.write(ChangeFlag.UPDATE)::runJAsync)
            .<JsonObject, JsonObject, JsonObject>runJ(body)
            /*
             * 404 / 200
             */
            .compose(Post::successPost);
    }

    @Address(Addr.Put.BATCH)
    public Future<Envelop> updateBatch(final Envelop envelop) {
        final String module = Ux.getString1(envelop);
        final JsonArray array = Ux.getArray2(envelop);
        /*
         * IxPanel processing building to split mass update
         * */
        final IxPanel panel = IxPanel.on(envelop, module);
        return Pre.qPk().inAJAsync(array, panel.active()).compose(condition -> {
            final JsonObject params = new JsonObject();
            /*
             * IxPanel
             */
            params.put(KName.DATA, array);
            params.put(Qr.KEY_CRITERIA, condition);
            return panel
                .passion(Agonic.write(ChangeFlag.UPDATE)::runJAsync)
                .runJ(params);
        });
    }

    @Address(Addr.Put.COLUMN_MY)
    public Future<Envelop> updateColumn(final Envelop envelop) {
        final String view = Ux.getString1(envelop);
        final JsonArray projection = Ux.getArray(envelop, 3);

        final String module = Ux.getString2(envelop);
        /* Batch Extract */
        final JsonObject params = new JsonObject();
        params.put(KName.VIEW, view);
        params.put(KName.DATA, projection);
        return IxPanel.on(envelop, module)
            .input(
                Pre.apeak(true)::inJAsync,              /* Apeak */
                Pre.head()::inJAsync                    /* Header */
            )
            .passion(Agonic.view()::runJAAsync, null)
            .runJ(params);
    }
}
