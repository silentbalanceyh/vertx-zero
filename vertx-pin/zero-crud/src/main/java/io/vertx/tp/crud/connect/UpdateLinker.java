package io.vertx.tp.crud.connect;

import cn.vertxup.crud.api.IxHttp;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.actor.IxActor;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

/*
 * Update by id linker
 */
class UpdateLinker implements IxLinker {
    private static final Annal LOGGER = Annal.get(UpdateLinker.class);

    @Override
    public Future<Envelop> joinJAsync(final Envelop request, final JsonObject original,
                                      final KModule module) {
        return IxSwitcher.moveOn(original, request.headers(), module, (dao, config) -> {
            /*
             * In updated, not needed to get key
             */
            final JsonObject inputData = IxSwitcher.getData(original, module);
            /*
             * Get mapped to value here.
             */
            final JsonObject filters = IxSwitcher.getCondition(original, module);

            return dao.fetchOneAsync(filters).compose(queried -> null == queried ?
                    /* Create New Record */
                    IxLinker.create().joinJAsync(request, inputData, module) :
                    /* Save */
                    IxActor.key().bind(request).procAsync(
                                    /*
                                     * 1. Get key from current json data
                                     * 2. Merged data here.
                                     * 3. Update data
                                     */
                                    this.procAsync(queried, inputData, config),
                                    config)
                            /* Verify */
                            .compose(input -> IxActor.verify().bind(request).procAsync(input, config))
                            /* T */
                            .compose(input -> Ix.deserializeT(input, config)
                                    /* Save */
                                    .compose(entity -> {
                                        final String key = input.getString(config.getField().getKey());
                                        Ix.infoDao(LOGGER, "Update Linker id = {0}, data = {1}",
                                                key, input.encodePrettily());
                                        return dao.updateAsync(key, entity);
                                    })
                            )
                            /* 200, Envelop */
                            .compose(entity -> IxHttp.success200(entity, config))
            );
        });
    }

    private JsonObject procAsync(final Object original, final JsonObject inputData, final KModule config) {
        /*
         * Here must bind pojo file
         */
        final JsonObject originalData = Ux.toJson(original, config.getPojo());
        final String field = config.getField().getKey();
        final String key = originalData.getString(field);
        /*
         * Key extracted
         */
        originalData.mergeIn(inputData);
        originalData.put(field, key);
        return originalData;
    }
}
