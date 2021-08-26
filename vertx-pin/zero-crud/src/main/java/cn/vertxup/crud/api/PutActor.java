package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.actor.IxActor;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.cv.IxMsg;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.next.WJoin;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.crud.uca.output.Post;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.ApeakMy;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

@Queue
public class PutActor {
    private static final Annal LOGGER = Annal.get(PutActor.class);

    @Address(Addr.Put.BY_ID)
    public <T> Future<Envelop> update(final Envelop envelop) {
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
    public <T> Future<Envelop> updateBatch(final Envelop envelop) {
        final JsonArray array = Ux.getArray1(envelop);
        return IxPanel.on(envelop, null)
            .input(
                Pre.qPk()::inAJAsync                        /* keys,in */
            )
            .next(in -> WJoin.on(in)::runAAsync)
            .passion(Agonic.write(ChangeFlag.UPDATE)::runJAAsync)
            .runA(array);
    }

    @Address(Addr.Put.COLUMN_MY)
    public <T> Future<Envelop> updateColumn(final Envelop request) {
        /* Batch Extract */
        return Ix.create(this.getClass()).input(request).envelop((dao, config) -> {
            /* Data Get */
            final JsonArray projection = Ux.getArray1(request);
            /* Put Stub */
            return Ke.channelAsync(ApeakMy.class, () -> Ux.future(new JsonArray()).compose(IxHttp::success200),
                stub -> Unity.fetchView(dao, request, config)
                    /* View parameters filling */
                    .compose(input -> IxActor.view().procAsync(input, config))
                    /* User filling */
                    .compose(input -> IxActor.user().bind(request).procAsync(input, config))
                    /* params `dataKey` calculation */
                    .compose(params -> this.prepareDataKey(params, request))
                    /* Fetch My Columns */
                    .compose(params -> stub.on(dao).saveMy(params, projection))
                    /* Flush Cache based on Ke */
                    // .compose(updated -> flush(request, updated))
                    /* Return Result */
                    .compose(IxHttp::success200));
        });
    }

    private Future<JsonObject> prepareDataKey(final JsonObject original, final Envelop request) {
        final JsonObject params = Unity.initMy(request);
        final String sessionKey = Ke.keySession(params.getString(KName.METHOD), params.getString(KName.URI));
        Ix.infoDao(LOGGER, IxMsg.CACHE_KEY_PROJECTION, sessionKey);
        original.put(KName.DATA_KEY, sessionKey);
        return Ux.future(original);
    }
}
