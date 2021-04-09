package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.metadata.KModule;
import io.vertx.tp.ke.cv.KeDefault;
import io.vertx.tp.ke.cv.KeField;

class ViewActor extends AbstractActor {
    @Override
    public JsonObject proc(final JsonObject data, final KModule module) {
        data.put(KeField.VIEW, KeDefault.VIEW_DEFAULT);
        return data;
    }
}
