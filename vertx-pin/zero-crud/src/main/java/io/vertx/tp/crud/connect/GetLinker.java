package io.vertx.tp.crud.connect;

import cn.vertxup.crud.api.IxHttp;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;

/*
 * Get by id linker for Get uri
 */
class GetLinker implements IxLinker {
    @Override
    public Future<Envelop> procAsync(final Envelop request, final JsonObject original, final KModule module) {
        /*
         * Ke mount
         * `metadata` field processing
         */
        Ke.mount(original, KName.METADATA);
        return IxSwitcher.moveOn(original, request.headers(), module, (dao, config) -> {
            /*
             * Get unique condition
             */
            final JsonObject filters = IxSwitcher.getCondition(original, module);
            return dao.fetchOneAsync(filters)
                    .compose(queried -> null == queried ?
                            /* 204 */
                            IxHttp.success204(queried) :
                            /* 200 */
                            IxHttp.success200(queried, config))
                    .compose(response -> IxSwitcher.moveEnd(original, response, config));
        });
    }
}
