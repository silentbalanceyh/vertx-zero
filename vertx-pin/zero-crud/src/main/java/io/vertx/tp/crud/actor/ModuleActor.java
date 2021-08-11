package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/*
 * {
 *      "actor": "module Name"
 * }
 */
class ModuleActor extends AbstractActor {
    @Override
    public JsonObject proc(final JsonObject data, final IxModule module) {
        final String actor = Ux.getString(this.getRequest());
        data.put(KName.ACTOR, actor);
        return data;
    }
}
