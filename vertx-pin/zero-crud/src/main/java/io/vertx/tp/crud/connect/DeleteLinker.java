package io.vertx.tp.crud.connect;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.unity.Ux;

/*
 * Linker deletion
 */
class DeleteLinker implements IxLinker {
    @Override
    public Future<Envelop> procAsync(final Envelop request, final JsonObject original,
                                     final IxModule module) {
        return OxSwitcher.moveOn(original, request.headers(), module, (dao, config) -> {
            /*
             * Extract identifier from original json data
             * Because delete by id fetch the data from database first and then
             * 1) Could fetch, delete
             * 2) Could not fetch, returned to 204
             */
            final JsonObject filters = OxSwitcher.getCondition(original, module);
            return dao.deleteByAsync(filters)
                    .compose(deleted -> Ux.future(Envelop.success(deleted)));
        });
    }
}
