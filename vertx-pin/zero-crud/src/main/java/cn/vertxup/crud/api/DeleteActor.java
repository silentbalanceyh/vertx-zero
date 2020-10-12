package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.connect.IxLinker;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.Trash;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

@Queue
public class DeleteActor {
    /*
     * DELETE: /api/{actor}/{key}
     *     200: Delete existing record
     *     204: Second deleting, The record has been gone
     */
    @Address(Addr.Delete.BY_ID)
    public Future<Envelop> delete(final Envelop request) {
        return Ix.create(this.getClass()).input(request).envelop((dao, config) -> {
            /* Key */
            final String key = Ux.getString1(request);
            return dao.fetchByIdAsync(key).compose(result -> null == result ?
                    /* 204 */
                    IxHttp.success204(Boolean.TRUE) :
                    /* Backup future */
                    Ux.future((JsonObject) Ut.serializeJson(result))
                            /* Connect Trash to back Up */
                            .compose(original -> Ke.channelAsync(Trash.class,
                                    () -> Ux.future(original),
                                    stub -> stub.backupAsync(config.getIdentifier(), original))
                            )
                            /* 200, IxLinker deleted first and then deleted current record */
                            .compose(original -> IxLinker.delete().procAsync(request, Ut.serializeJson(original), config)
                                    .compose(nil ->
                                            /*
                                             * Delete current record
                                             */
                                            dao.deleteByIdAsync(key).compose(IxHttp::success200)))
            );


        });
    }

    /*
     * DELETE: /api/batch/{actor}/delete
     *     200: Delete existing records
     *     204: Second deleting, The records have been gone
     */
    @Address(Addr.Delete.BATCH)
    public Future<Envelop> deleteBatch(final Envelop request) {
        return Ix.create(this.getClass()).input(request).envelop((dao, config) -> {
            /* Keys */
            final JsonArray keys = Ux.getArray1(request);
            /* ID */
            return Ix.inKeys(keys, config)
                    /* List */
                    .compose(dao::deleteAsync)
                    /* Boolean */
                    .compose(IxHttp::success200);
        });
    }
}
