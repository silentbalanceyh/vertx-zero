package io.vertx.tp.crud.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxField;
import io.vertx.tp.crud.atom.IxModule;
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

    static JsonObject unique(final JsonObject result) {
        final JsonArray list = result.getJsonArray("list");
        return list.getJsonObject(Values.IDX);
    }

    static JsonArray list(final JsonObject result) {
        JsonArray list = result.getJsonArray("list");
        if (Objects.isNull(list)) {
            list = new JsonArray();
        }
        return list;
    }

    static JsonArray zipper(final JsonArray from, final JsonArray to, final IxModule config) {
        final IxField field = config.getField();
        final String keyField = field.getKey();
        return Uarr.create(to)
                .zip(from, keyField, keyField)
                .to();
    }

    @SuppressWarnings("all")
    static <T> T entity(final JsonObject data, final IxModule config) {
        IxLog.infoDao(LOGGER, "Normalized: \n{0}", data.encodePrettily());
        final String pojo = config.getPojo();
        final T reference = Ut.isNil(pojo) ?
                Ux.fromJson(data, (Class<T>) config.getPojoCls()) :
                Ux.fromJson(data, (Class<T>) config.getPojoCls(), config.getPojo());
        IxLog.infoDao(LOGGER, "Deserialized: {0}", reference);
        return reference;
    }

    @SuppressWarnings("all")
    static <T> List<T> entity(final JsonArray data, final IxModule config) {
        final List<T> list = new ArrayList<>();
        data.stream()
                .filter(Objects::nonNull)
                .map(item -> (JsonObject) item)
                .map(entity -> (T) entity(entity, config))
                .forEach(reference -> list.add(reference));
        return list;
    }
}
