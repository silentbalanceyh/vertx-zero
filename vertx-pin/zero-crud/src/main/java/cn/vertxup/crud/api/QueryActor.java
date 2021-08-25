package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.actor.IxActor;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.uca.jooq.UxJooq;
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
    public Future<Envelop> existing(final Envelop request) {
        return Ix.create(this.getClass()).input(request).envelop((dao, config) -> {
            /* Pojo Extract */
            return this.isExisting(dao, config, request)
                    /* Completed */
                    .compose(IxHttp::success200);
        });
    }

    @Address(Addr.Post.MISSING)
    public Future<Envelop> missing(final Envelop request) {
        return Ix.create(this.getClass()).input(request).envelop((dao, config) -> {
            /* Pojo Extract */
            return this.isExisting(dao, config, request)
                    /* Completed */
                    .compose(result -> IxHttp.success200(!result));
        });
    }

    private Future<Boolean> isExisting(final UxJooq dao, final KModule config, final Envelop request) {
        /* Parameters Extraction */
        final JsonObject body = Ux.getJson1(request);
        /* Pojo Extract */
        return Ux.future(body)
                /* Verify */
                .compose(input -> IxActor.verify().bind(request).procAsync(input, config))
                /* Execution */
                .compose(params -> Ix.existing(params, config).apply(dao));
    }
}
