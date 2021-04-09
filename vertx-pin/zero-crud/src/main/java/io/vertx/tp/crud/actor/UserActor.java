package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.metadata.KModule;

class UserActor extends AbstractActor {
    @Override
    public JsonObject proc(final JsonObject data, final KModule module) {
        data.put("user", this.getUser());
        data.put("habitus", this.getHabitus());
        return data;
    }
}
