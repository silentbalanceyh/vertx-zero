package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.tp.ke.cv.KeDefault;
import io.vertx.up.eon.KName;

class ViewActor extends AbstractActor {
    @Override
    public JsonObject proc(final JsonObject data, final IxModule module) {
        data.put(KName.VIEW, KeDefault.VIEW_DEFAULT);
        return data;
    }
}
