package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.metadata.KField;
import io.vertx.up.util.Ut;

/*
 * {
 *      "updatedAt":"",
 *      "updatedBy":""
 * }
 */
class UpdateActor extends AbstractActor {

    @Override
    public JsonObject proc(final JsonObject data, final IxModule config) {
        /* UserId */
        final String userId = this.getUser();
        if (Ut.notNil(userId)) {
            final KField field = config.getField();
            /* Created */
            Ix.audit(data, field.getUpdated(), userId);
        }
        return data;
    }
}
