package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxField;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.up.util.Ut;

/*
 * Creation for body processing and inject auditor information
 * {
 *     "createdAt": "x",
 *     "createdBy": "xx"
 * }
 */
class CreateActor extends AbstractActor {

    @Override
    public JsonObject proc(final JsonObject data, final IxModule config) {
        /* UserId */
        final String userId = this.getUser();
        if (Ut.notNil(userId)) {
            final IxField field = config.getField();
            /* Created */
            Ix.audit(data, field.getCreated(), userId);
        }
        return data;
    }
}
