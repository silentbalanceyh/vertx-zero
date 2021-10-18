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
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.em.ChangeFlag;

@Queue
public class DeleteActor {
    /*
     * DELETE: /api/{actor}/{key}
     *     200: Delete existing record
     *     204: Second deleting, The record has been gone
     */
    @Address(Addr.Delete.BY_ID)
    public Future<Envelop> delete(final Envelop envelop) {
        final IxWeb request = IxWeb.create(ApiSpec.BODY_STRING).build(envelop);
        final JsonObject params = request.dataK();
        return IxPanel.on(request)
            .passion(Agonic.write(ChangeFlag.DELETE)::runJAsync, null)
            .<JsonObject, JsonObject, JsonObject>runJ(params)
            /*
             * 204 / 200
             */
            .compose(IxKit::successPostB);
    }

    /*
     * DELETE: /api/batch/{actor}/delete
     *     200: Delete existing records
     *     204: Second deleting, The records have been gone
     */
    @Address(Addr.Delete.BATCH)
    public Future<Envelop> deleteBatch(final Envelop envelop) {
        final IxWeb request = IxWeb.create(ApiSpec.BODY_ARRAY).build(envelop);
        return IxPanel.on(request)
            .input(
                Pre.qPk()::inAJAsync                        /* keys,in */
            )
            .passion(Agonic.write(ChangeFlag.DELETE)::runJAAsync, null)
            .runA(request.dataA());
    }
}
