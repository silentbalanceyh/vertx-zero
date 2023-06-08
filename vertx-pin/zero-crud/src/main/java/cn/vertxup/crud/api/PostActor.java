package cn.vertxup.crud.api;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.Addr;
import io.vertx.mod.crud.cv.em.ApiSpec;
import io.vertx.mod.crud.uca.desk.IxKit;
import io.vertx.mod.crud.uca.desk.IxPanel;
import io.vertx.mod.crud.uca.desk.IxWeb;
import io.vertx.mod.crud.uca.input.Pre;
import io.vertx.mod.crud.uca.next.Co;
import io.vertx.mod.crud.uca.op.Agonic;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

/*
 * Create new Record that defined in zero system.
 * The definition file are stored under
 *      `plugin/crud/module/`
 * The validation rule file are stored under
 *      `plugin/crud/validator/`
 * 「新版定制完成」
 */
@Queue
@SuppressWarnings("all")
public class PostActor {

    /*
     * POST: /api/{actor}
     *     200: Created new record
     *     201: The record existing in database ( Could not do any things )
     */
    @Address(Addr.Post.ADD)
    public Future<Envelop> create(final Envelop envelop) {
        /* Actor Extraction */
        final IxWeb request = IxWeb.create(ApiSpec.BODY_JSON).build(envelop);
        final Co coJ = Co.nextJ(request.active(), false);
        return IxPanel.on(request)

            /*
             * 1. Input = JsonObject
             * -- io.vertx.mod.crud.uca.input.HeadPre
             * -- io.vertx.mod.crud.uca.input.CodexPre ( Validation )
             */
            .input(
                Pre.head()::inJAsync,                       /* Header */
                Pre.codex()::inJAsync                       /* Codex */
            )


            /*
             * 2. io.vertx.mod.crud.uca.next.NtJData
             * JsonObject ( active ) -> JsonObject ( standBy )
             */
            .next(in -> coJ::next)


            /*
             * 3. passion will set sequence = true
             *
             * (J) -> (J) Active (J) -> StandBy (J)
             *
             */
            .passion(Agonic.write(ChangeFlag.ADD)::runJAsync)


            /*
             * 4.1 The response is as following ( JsonObject Merged )
             */
            .output(coJ::ok)


            /*
             * 0. Input
             *
             * JsonObject -> JsonObject -> JsonObject
             */
            .<JsonObject, JsonObject, JsonObject>runJ(request.dataJ())


            /*
             * 4.2. 201 / 200
             */
            .compose(IxKit::successPost);
    }
}
