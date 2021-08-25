package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
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
        final JsonObject body = Ux.getJson1(envelop);
        final String module = Ux.getString(envelop, 3);           // module

        return IxPanel.on(envelop, module)
                .input(
                        /* Codex */
                        Pre.codex()::inAsync
                )
                .passion(Agonic.search()::runAsync, null)
                .runJ(body);
    }

    @Address(Addr.Post.EXISTING)
    public Future<Envelop> existing(final Envelop envelop) {
        final JsonObject body = Ux.getJson1(envelop);
        final String module = Ux.getString2(envelop);           // module
        return IxPanel.on(envelop, module)
                .input(
                        /* Codex */
                        Pre.codex()::inAsync
                )
                .passion(Agonic.count()::runAsync, null)
                .runJ(body);
    }

    @Address(Addr.Post.MISSING)
    public Future<Envelop> missing(final Envelop envelop) {
        final JsonObject body = Ux.getJson1(envelop);
        final String module = Ux.getString2(envelop);           // module
        return IxPanel.on(envelop, module)
                .input(
                        /* Codex */
                        Pre.codex()::inAsync
                )
                .passion(Agonic.count()::runAsync, null)
                .runJ(body);
    }
}
