package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.connect.IxLinker;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

/*
 * Create new Record that defined in zero system.
 * The definition file are stored under
 *      `plugin/crud/module/`
 * The validation rule file are stored under
 *      `plugin/crud/validator/`
 */
@Queue
public class PostActor {

    /*
     * POST: /api/{actor}
     *     200: Created new record
     *     201: The record existing in database ( Could not do any things )
     */
    @Address(Addr.Post.ADD)
    public Future<Envelop> create(final Envelop request) {
        /* Actor Extraction */
        return Ix.create(this.getClass()).input(request).envelop((dao, config) -> {
            /* Data Get */
            final JsonObject body = Ux.getJson1(request);
            return IxHub.createAsync(request, body, dao, config)
                    /* Extension by connect here for creation */
                    .compose(response -> IxLinker.create().joinJAsync(request,
                            /* Must merged */
                            body.mergeIn(response.data()), config));
        });
    }
}
