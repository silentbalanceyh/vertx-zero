package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.next.WJoin;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.crud.uca.output.Post;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/*
 * Create new Record that defined in zero system.
 * The definition file are stored under
 *      `plugin/crud/module/`
 * The validation rule file are stored under
 *      `plugin/crud/validator/`
 * 「新版定制完成」
 */
@Queue
public class PostActor {

    /*
     * POST: /api/{actor}
     *     200: Created new record
     *     201: The record existing in database ( Could not do any things )
     */
    @Address(Addr.Post.ADD)
    public Future<Envelop> create(final Envelop envelop) {
        /* Actor Extraction */
        final JsonObject body = Ux.getJson1(envelop);
        final String module = body.getString(KName.IDENTIFIER); // module
        return IxPanel.on(envelop, module)
                .input(
                        /* Header */
                        Pre.head()::inAsync,
                        /* Codex */
                        Pre.codex()::inAsync,
                        /* Number */
                        Pre.serial()::inAsync
                )
                .next(in -> WJoin.on(in)::runAsync)
                .passion(Agonic.create()::runAsync)
                .<JsonObject, JsonObject, JsonObject>runJ(body)
                /*
                 * 201 / 200
                 */
                .compose(Post::successPost);
    }
}
