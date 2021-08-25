package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.actor.IxActor;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

@Deprecated
public class IxHub {
    /*
     * Split code logical
     * 1) params
     * Envelop:  request,
     * JsonObject: dataPart
     * UxJooq:   dao
     * IxModule: module configuration
     * 2) two code flow:
     * -- Create directly
     * -- Create joined object: 2 tables
     */
    public static Future<Envelop> createAsync(final Envelop request, final JsonObject body,
                                              final UxJooq dao, final KModule config) {
        return Ux.future(body)
                /* Header */
                .compose(input -> IxActor.header().bind(request).procAsync(input, config))
                /* Verify */
                .compose(input -> IxActor.verify().bind(request).procAsync(input, config))
                /* Serial */
                .compose(input -> IxActor.serial().bind(request).procAsync(input, config))
                /* Unique Filters */
                .compose(input -> IxActor.unique().procAsync(input, config))
                /* Filters */
                .compose(filters -> Ix.search(filters, config).apply(dao))
                /* Unique Extract from { list, count } */
                .compose(result -> Ix.isExist(result) ?
                        /* Unique */
                        Ix.serializePO(result, config)
                                /* Update */
                                .compose(input -> IxActor.update().bind(request).procAsync(input, config))
                                /* Deserialize */
                                .compose(json -> Ix.deserializeT(json, config))
                                /* 201, Envelop */
                                .compose(entity -> IxHttp.success201(entity, config)) :
                        /* Primary Key Add */
                        IxActor.uuid().procAsync(body, config)
                                /* Create */
                                .compose(input -> IxActor.create().bind(request).procAsync(input, config))
                                /* Update */
                                .compose(input -> IxActor.update().bind(request).procAsync(input, config))
                                /* Build Data */
                                .compose(input -> Ix.deserializeT(input, config))
                                /* T */
                                .compose(dao::insertAsync)
                                /* 200, Envelop */
                                .compose(entity -> IxHttp.success200(entity, config))
                );
    }
}
