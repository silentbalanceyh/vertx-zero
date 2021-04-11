package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;

class UserActor extends AbstractActor {
    @Override
    public JsonObject proc(final JsonObject data, final IxModule module) {
        data.put("user", this.getUser());
        data.put("habitus", this.getHabitus());
        return data;
    }
}
