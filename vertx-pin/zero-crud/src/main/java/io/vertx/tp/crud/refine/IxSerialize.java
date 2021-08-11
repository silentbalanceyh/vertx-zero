package io.vertx.tp.crud.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.tp.ke.atom.metadata.KField;
import io.vertx.up.eon.KName;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.atom.unity.Uarr;
import io.vertx.up.eon.Values;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class IxSerialize {

    private static final Annal LOGGER = Annal.get(IxSerialize.class);

    static JsonObject serializePO(final JsonObject result, final IxModule config) {
        final JsonArray list = result.getJsonArray("list");
        final JsonObject queried = list.getJsonObject(Values.IDX);
        return serializeJ(queried, config);
    }

    static JsonArray serializePL(final JsonObject result, final IxModule config) {
        JsonArray list = result.getJsonArray("list");
        if (Objects.isNull(list)) {
            list = new JsonArray();
        }
        return serializeA(list, config);
    }

    static JsonArray serializeA(final JsonArray from, final JsonArray to, final IxModule config) {
        final io.vertx.tp.ke.atom.metadata.KField field = config.getField();
        final String keyField = field.getKey();
        return Uarr.create(to).zip(from, keyField, keyField).to();
    }

    /*
     * {
     *     "count": xx,
     *     "list": []
     * }
     */
    static JsonObject serializeP(final JsonObject data, final IxModule config) {
        final JsonArray ref = Ut.sureJArray(data.getJsonArray("list"));
        data.put("list", serializeA(ref, config));
        return data;
    }

    static JsonObject serializeJ(final JsonObject data, final IxModule config) {
        /*
         * Deserialize First
         */
        Ke.mount(data, KName.METADATA);
        final KField field = config.getField();
        field.fieldObject().forEach(each -> Ke.mount(data, each));
        field.fieldArray().forEach(each -> Ke.mountArray(data, each));
        return data;
    }

    static JsonArray serializeA(final JsonArray data, final IxModule config) {
        if (Ut.isNil(data)) {
            return new JsonArray();
        } else {
            Ut.itJArray(data).forEach(refJson -> serializeJ(refJson, config));
            return data;
        }
    }

    @SuppressWarnings("all")
    static <T> T deserializeT(final JsonObject data, final IxModule config) {
        IxLog.infoDao(LOGGER, "Normalized: \n{0}", data.encodePrettily());
        {
            /*
             * JsonObject / JsonArray must be converted to string
             * New feature for serialization on
             * `array, object` fields for future usage
             */
            Ke.mountString(data, KName.METADATA);
            final KField field = config.getField();
            field.fieldArray().forEach(each -> Ke.mountString(data, each));         // JsonArray defined
            field.fieldObject().forEach(each -> Ke.mountString(data, each));        // JsonObject defined
        }
        final String pojo = config.getPojo();
        final T reference = Ut.isNil(pojo) ?
                Ux.fromJson(data, (Class<T>) config.getPojoCls()) :
                Ux.fromJson(data, (Class<T>) config.getPojoCls(), config.getPojo());
        IxLog.infoDao(LOGGER, "Deserialized: {0}", reference);
        return reference;
    }

    @SuppressWarnings("all")
    static <T> List<T> deserializeT(final JsonArray data, final IxModule config) {
        final List<T> list = new ArrayList<>();
        data.stream()
                .filter(Objects::nonNull)
                .map(item -> (JsonObject) item)
                .map(entity -> (T) deserializeT(entity, config))
                .forEach(reference -> list.add(reference));
        return list;
    }
}
