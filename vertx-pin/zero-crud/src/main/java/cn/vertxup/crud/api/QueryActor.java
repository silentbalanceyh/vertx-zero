package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.Addr;
import io.vertx.mod.crud.cv.em.ApiSpec;
import io.vertx.mod.crud.uca.desk.IxPanel;
import io.vertx.mod.crud.uca.desk.IxWeb;
import io.vertx.mod.crud.uca.input.Pre;
import io.vertx.mod.crud.uca.op.Agonic;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/*
 * JqTool Engine for
 * 1) Pagination
 * 2) Projection
 * 3) Sorter
 * 4) Criteria
 *
 * The parameter format is as:
 * {
 *      "criteria":{},
 *      "projection":[],
 *      "sorter":[],
 *      "pager":{
 *          "size":10,
 *          "page":1
 *      }
 * }
 * 「新版定制完成」
 */
@Queue
public class QueryActor {
    /*
     * POST: /api/{actor}/search
     *      200: Search Record
     */
    @Address(Addr.Post.SEARCH)
    public Future<Envelop> search(final Envelop envelop) {
        final IxWeb request = IxWeb.create(ApiSpec.BODY_JSON).build(envelop);
        return IxPanel.on(request)
            .input(
                Pre.codex()::inJAsync                   /* Codex */
            )
            .passion(Agonic.search()::runJAsync, null)
            .runJ(request.dataJ());
    }

    @Address(Addr.Post.EXISTING)
    public Future<Envelop> existing(final Envelop envelop) {
        final IxWeb request = IxWeb.create(ApiSpec.BODY_JSON).build(envelop);
        return IxPanel.on(request)
            .input(
                Pre.codex()::inJAsync                   /* Codex */
            )
            .passion(Agonic.count()::runJAsync, null)
            .<JsonObject, JsonObject, JsonObject>runJ(request.dataJ())
            .compose(item -> Ux.future(Envelop.success(0 < item.getInteger(KName.COUNT))));
    }

    @Address(Addr.Post.MISSING)
    public Future<Envelop> missing(final Envelop envelop) {
        final IxWeb request = IxWeb.create(ApiSpec.BODY_JSON).build(envelop);
        return IxPanel.on(request)
            .input(
                Pre.codex()::inJAsync                   /* Codex */
            )
            .passion(Agonic.count()::runJAsync, null)
            .<JsonObject, JsonObject, JsonObject>runJ(request.dataJ())
            .compose(item -> Ux.future(Envelop.success(0 == item.getInteger(KName.COUNT))));
    }
}
