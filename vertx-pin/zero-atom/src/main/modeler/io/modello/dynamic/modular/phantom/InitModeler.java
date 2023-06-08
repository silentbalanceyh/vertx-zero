package io.modello.dynamic.modular.phantom;

import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import java.util.function.Function;

class InitModeler implements AoModeler {

    private static final Annal LOGGER = Annal.get(InitModeler.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return entityJson -> {
            LOGGER.debug("[ Ox ] 1. AoModeler.init() ：{0}", entityJson.encode());
            final JsonObject modelJson = new JsonObject();
            modelJson.put(KName.MODEL, entityJson);
            return Ux.future(modelJson);
        };
    }

    @Override
    public JsonObject executor(final JsonObject entityJson) {
        LOGGER.debug("[ Ox ] (Sync) 1. AoModeler.init() ：{0}", entityJson.encode());
        final JsonObject modelJson = new JsonObject();
        modelJson.put(KName.MODEL, entityJson);
        return modelJson;
    }
}