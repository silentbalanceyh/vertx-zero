package io.vertx.tp.crud.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.specification.KField;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class IxSerialize {

    private static final Annal LOGGER = Annal.get(IxSerialize.class);

    private static void serializeInternal(final JsonObject data, final KModule config) {
        /*
         * Deserialize First
         */
        Ut.ifJObject(data, KName.METADATA);
        final KField field = config.getField();
        field.fieldObject().forEach(each -> Ut.ifJObject(data, each));
        field.fieldArray().forEach(each -> Ut.ifJObject(data, each));
    }

    static <T> JsonObject serializeJ(final T input, final KModule module) {
        final JsonObject serializedJ;
        if (input instanceof JsonObject) {
            serializedJ = (JsonObject) input;
        } else {
            serializedJ = Ux.toJson(input, module.getPojo());
        }
        serializeInternal(serializedJ, module);
        return serializedJ;
    }

    static <T> JsonArray serializeA(final List<T> input, final KModule module) {
        if (Objects.isNull(input)) {
            return new JsonArray();
        } else {
            final JsonArray serializedA;
            if (input instanceof JsonArray) {
                serializedA = (JsonArray) input;
            } else {
                serializedA = Ux.toJson(input, module.getPojo());
            }
            Ut.itJArray(serializedA).forEach(refJson -> serializeInternal(refJson, module));
            return serializedA;
        }
    }

    @SuppressWarnings("all")
    static <T> T deserializeT(final JsonObject data, final KModule config) {
        IxLog.infoDao(LOGGER, "Normalized: \u001b[0;37m{0}\u001b[m", data.encode());
        {
            /*
             * JsonObject / JsonArray must be converted to string
             * New feature for serialization on
             * `array, object` fields for future usage
             */
            Ut.ifString(data, KName.METADATA);
            final KField field = config.getField();
            field.fieldArray().forEach(each -> Ut.ifString(data, each));         // JsonArray defined
            field.fieldObject().forEach(each -> Ut.ifString(data, each));        // JsonObject defined
        }
        final String pojo = config.getPojo();
        final T reference = Ut.isNil(pojo) ?
            Ux.fromJson(data, (Class<T>) config.getPojoCls()) :
            Ux.fromJson(data, (Class<T>) config.getPojoCls(), config.getPojo());
        IxLog.infoDao(LOGGER, "Deserialized: {0}", reference);
        return reference;
    }

    @SuppressWarnings("all")
    static <T> List<T> deserializeT(final JsonArray data, final KModule config) {
        final List<T> list = new ArrayList<>();
        data.stream()
            .filter(Objects::nonNull)
            .map(item -> (JsonObject) item)
            .map(entity -> (T) deserializeT(entity, config))
            .forEach(reference -> list.add(reference));
        return list;
    }
}
