package io.vertx.tp.crud.connect;

import cn.vertxup.crud.api.IxHub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.up.commune.Envelop;

/*
 * Linker creation
 */
class CreateLinker implements IxLinker {

    @Override
    public Future<Envelop> procAsync(final Envelop request, final JsonObject original, final IxModule module) {

        return OxSwitcher.moveOn(original, request.headers(), module, (dao, config) -> {
            /*
             * Remove primary key, it will generate new.
             */
            final JsonObject inputData = OxSwitcher.getData(original, module);
            inputData.remove(module.getField().getKey());
            return IxHub.createAsync(request, inputData, dao, config)
                    .compose(response -> OxSwitcher.moveEnd(original, response, config));
        });
    }
}
