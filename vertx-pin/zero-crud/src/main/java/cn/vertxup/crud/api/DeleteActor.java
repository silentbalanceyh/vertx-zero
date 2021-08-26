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
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;

@Queue
public class DeleteActor {
    /*
     * DELETE: /api/{actor}/{key}
     *     200: Delete existing record
     *     204: Second deleting, The record has been gone
     */
    @Address(Addr.Delete.BY_ID)
    public Future<Envelop> delete(final Envelop envelop) {
        final String key = Ux.getString1(envelop);
        // JsonObject Required
        final JsonObject params = new JsonObject().put(KName.KEY, key);
        return IxPanel.on(envelop, null)
            .passion(Agonic.write(ChangeFlag.DELETE)::runJAsync, null)
            .<JsonObject, JsonObject, JsonObject>runJ(params)
            /*
             * 204 / 200
             */
            .compose(Post::successPostB);
    }

    /*
     * DELETE: /api/batch/{actor}/delete
     *     200: Delete existing records
     *     204: Second deleting, The records have been gone
     */
    @Address(Addr.Delete.BATCH)
    public Future<Envelop> deleteBatch(final Envelop envelop) {
        final JsonArray keys = Ux.getArray1(envelop);
        return IxPanel.on(envelop, null)
            .input(
                Pre.qPk()::inAJAsync                        /* keys,in */
            )
            .passion(Agonic.write(ChangeFlag.DELETE)::runJAAsync, null)
            .runA(keys);
    }
}
