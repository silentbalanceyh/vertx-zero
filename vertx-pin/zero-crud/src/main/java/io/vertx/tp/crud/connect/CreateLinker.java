package io.vertx.tp.crud.connect;

import cn.vertxup.crud.api.IxHub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;

/*
 * Linker creation
 */
class CreateLinker implements IxLinker {

    @Override
    public Future<Envelop> joinJAsync(final Envelop request, final JsonObject original, final KModule module) {

        return IxSwitcher.moveOn(original, request.headers(), module, (dao, config) -> {
            /*
             * Remove primary key, it will generate new.
             */
            final JsonObject inputData = IxSwitcher.getData(original, module);
            inputData.remove(module.getField().getKey());
            return IxHub.createAsync(request, inputData, dao, config)
                .compose(response -> IxSwitcher.moveEnd(original, response, config));
        });
    }
}
