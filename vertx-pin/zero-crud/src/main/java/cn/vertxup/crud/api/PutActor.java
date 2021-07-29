package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.actor.IxActor;
import io.vertx.tp.crud.connect.IxLinker;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.cv.IxMsg;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.ApeakMy;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

@Queue
public class PutActor {
    private static final Annal LOGGER = Annal.get(PutActor.class);

    @Address(Addr.Put.BY_ID)
    public <T> Future<Envelop> update(final Envelop request) {
        /* Module and Key Extract  */
        return Ix.create(this.getClass()).input(request).envelop((dao, config) -> {
            /* Data Get */
            final JsonObject body = Ux.getJson2(request);
            final String key = Ux.getString1(request);
            return dao.fetchByIdAsync(key).compose(queried -> null == queried ?
                    /* 204, No Content */
                    IxHttp.success204(null) :
                    /* Save */
                    IxActor.key().bind(request).procAsync(body, config)
                            /* Verify */
                            .compose(input -> IxActor.verify().bind(request).procAsync(input, config))
                            /* T */
                            .compose(input -> Ix.deserializeT(input, config))
                            /* Save */
                            .compose(entity -> dao.updateAsync(key, entity))
                            /* 200, Envelop */
                            .compose(entity -> IxHttp.success200(entity, config)))
                    /* Must merged */
                    .compose(response -> IxLinker.update().procAsync(request,
                            body.mergeIn(response.data()), config));
        });
    }

    @Address(Addr.Put.BATCH)
    public <T> Future<Envelop> updateBatch(final Envelop request) {
        /* Batch Extract */
        return Ix.create(this.getClass()).input(request).envelop((dao, config) -> {
            /* Data Get */
            final JsonArray array = Ux.getArray1(request);
            return Ix.inKeys(array, config)
                    /* Search List */
                    .compose(filters -> Ix.search(filters, config).apply(dao))
                    /* Extract List */
                    .compose(data -> Ix.serializePL(data, config))
                    /* JsonArray */
                    .compose(queried -> Ix.serializeA(queried, array, config))
                    /* JsonArray */
                    .compose(dataArr -> Ix.deserializeT(dataArr, config))
                    /* List<T> */
                    .compose(dao::updateAsync)
                    /* JsonArray */
                    .compose(IxHttp::success200);
        });
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
        final String sessionKey = Ke.keySession(params.getString(KeField.METHOD), params.getString(KeField.URI));
        Ix.infoDao(LOGGER, IxMsg.CACHE_KEY_PROJECTION, sessionKey);
        original.put(KeField.DATA_KEY, sessionKey);
        return Ux.future(original);
    }
}
