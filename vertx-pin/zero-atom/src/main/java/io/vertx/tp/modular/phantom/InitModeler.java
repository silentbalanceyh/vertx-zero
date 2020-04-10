package io.vertx.tp.modular.phantom;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.util.function.Function;

class InitModeler implements AoModeler {

    private static final Annal LOGGER = Annal.get(InitModeler.class);

    @Override
    public Function<JsonObject, Future<JsonObject>> apply() {
        return entityJson -> {
            LOGGER.debug("[ Ox ] 1. AoModeler.init() ：{0}", entityJson.encode());
            final JsonObject modelJson = new JsonObject();
            modelJson.put(KeField.MODEL, entityJson);
            return Ux.future(modelJson);
        };
    }

    @Override
    public JsonObject executor(final JsonObject entityJson) {
        LOGGER.debug("[ Ox ] (Sync) 1. AoModeler.init() ：{0}", entityJson.encode());
        final JsonObject modelJson = new JsonObject();
        modelJson.put(KeField.MODEL, entityJson);
        return modelJson;
    }
}