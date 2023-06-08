package cn.vertxup.crud.api;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
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
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

@Queue
@SuppressWarnings("all")
public class PutActor {

    @Address(Addr.Put.BY_ID)
    public Future<Envelop> update(final Envelop envelop) {
        /* Module and Key Extract  */
        final IxWeb request = IxWeb.create(ApiSpec.BODY_WITH_KEY).build(envelop);
        final Co co = Co.nextJ(request.active(), false);
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
            .next(in -> co::next)

            /*
             * 3. passion will set sequence = true
             *
             * (J) -> (J) Active (J) -> StandBy (J)
             *
             */
            .passion(
                /* Active */Agonic.write(ChangeFlag.UPDATE)::runJAsync,
                /* StandBy */Agonic.saveYou(request.active())::runJAsync
            )


            /*
             * 4.1 The response is as following ( JsonObject Merged )
             */
            .output(co::ok)


            /*
             *
             * 0. Input
             *
             * JsonObject -> JsonObject -> JsonObject
             *
             */
            .<JsonObject, JsonObject, JsonObject>runJ(request.dataKJ())


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
        /*
         * 1. Fetch all original JsonArray data
         */
        return Agonic.fetch().runAAsync(request.dataA(), request.active()).compose(data -> {
            final IxPanel panel = IxPanel.on(request);
            final Co co = Co.nextJ(request.active(), true);
            /*
             * 2. Data Merge
             */
            final JsonArray parameters = Ux.updateJ(data, request.dataA());
            return panel


                /*
                 * 1. Input = JsonArray
                 * -- io.vertx.mod.crud.uca.input.HeadPre
                 * -- io.vertx.mod.crud.uca.input.CodexPre ( Validation )
                 */
                .input(
                    Pre.head()::inAAsync                       /* Header */
                )


                .next(in -> co::next)

                /* Active / StandBy Shared */
                .passion(
                    Agonic.write(ChangeFlag.UPDATE)::runAAsync,
                    Agonic.saveYou(request.active())::runAAsync
                )

                .output(co::ok)
                /*
                 *
                 * 0. Input
                 *
                 * JsonArray -> JsonArray -> JsonArray
                 *
                 */
                .runA(parameters);
        });
    }

    @Address(Addr.Put.COLUMN_MY)
    public Future<JsonObject> updateColumn(final Envelop envelop) {
        final IxWeb request = IxWeb.create(ApiSpec.BODY_JSON).build(envelop);
        /*
         * Fix issue of Data Region
         * Because `projection` and `criteria` are both spec params
         *
         * The parameters data structure
         * {
         *      "data":  {
         *          "comment": "came from `viewData` field include view"
         *      },
         *      "impactUri": "The url that will be impact of my view, common is /api/xxx/search"
         * }
         * */
        final JsonObject params = request.dataV();
        final JsonObject requestData = request.dataJ();
        final JsonObject viewData = requestData.getJsonObject("viewData", new JsonObject());
        params.put(KName.DATA, Ut.valueJObject(viewData));
        params.put(KName.URI_IMPACT, viewData.getString(KName.URI_IMPACT));
        return IxPanel.on(request)

            /*
             * 1. Input = JsonObject
             * -- io.vertx.mod.crud.uca.input.ApeakMyPre
             * -- io.vertx.mod.crud.uca.input.CodexPre ( Validation )
             */
            .input(
                Pre.apeak(true)::inJAsync,              /* Apeak */
                Pre.head()::inJAsync                    /* Header */
            )

            /*
             * 2. 「Active Only」, no standBy defined
             */
            .passion(Agonic.view()::runJAsync, null)


            /*
             * 0. Input
             * JsonObject -> JsonObject -> JsonObject
             */
            .runJ(params);
    }
}
