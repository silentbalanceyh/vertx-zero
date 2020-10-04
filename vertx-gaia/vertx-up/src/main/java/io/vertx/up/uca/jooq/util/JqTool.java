package io.vertx.up.uca.jooq.util;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.pojo.Mirror;
import io.vertx.up.atom.pojo.Mojo;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class JqTool {

    private static final Annal LOGGER = Annal.get(JqTool.class);

    public static <T> CompositeFuture joinAsync(final JsonObject criteria, final JsonObject data, final JqFlow flow) {
        final Future<JsonObject> criteriaFuture = flow.inputQrJAsync(criteria);
        final Future<T> dataFuture = flow.inputAsync(data);
        return CompositeFuture.join(criteriaFuture, dataFuture);
    }

    public static <T> CompositeFuture joinAsync(final JsonObject criteria, final JsonArray data, final JqFlow flow) {
        final Future<JsonObject> criteriaFuture = flow.inputQrJAsync(criteria);
        final Future<List<T>> dataFuture = flow.inputAsync(data);
        return CompositeFuture.join(criteriaFuture, dataFuture);
    }

    public static Inquiry getInquiry(final JsonObject envelop, final String pojo) {
        return Fn.getNull(Inquiry.create(new JsonObject()), () -> {
            final JsonObject data = envelop.copy();
            if (Ut.isNil(pojo)) {
                return Inquiry.create(data);
            } else {
                // Projection Process
                final Mojo mojo = Mirror.create(JqTool.class).mount(pojo).mojo();
                return getInquiry(data, mojo);
            }
        }, envelop);
    }

    public static Inquiry getInquiry(final JsonObject data, final Mojo mojo) {
        if (data.containsKey("projection")) {
            data.put("projection", projection(data.getJsonArray("projection"), mojo));
        }
        if (data.containsKey("sorter")) {
            data.put("sorter", sorter(data.getJsonArray("sorter"), mojo));
        }
        if (data.containsKey("criteria")) {
            data.put("criteria", criteria(data.getJsonObject("criteria"), mojo));
        }
        LOGGER.info(Info.INQUIRY_MESSAGE, data.encode());
        return Inquiry.create(data);
    }

    public static JsonObject criteria(final JsonObject criteria, final Mojo mojo) {
        final JsonObject criterias = new JsonObject();
        final ConcurrentMap<String, String> mapping = mojo.getIn();
        for (final String field : criteria.fieldNames()) {
            // Filter processed
            final String key = field.contains(Strings.COMMA) ? field.split(Strings.COMMA)[0] : field;
            final String targetField;
            if (mapping.containsKey(key)) {
                if (field.contains(Strings.COMMA)) {
                    targetField = mapping.get(key) + Strings.COMMA + field.split(Strings.COMMA)[1];
                } else {
                    targetField = mapping.get(key);
                }
                // Ignore non-existing field in mapping here to avoid SQL errors.
                criterias.put(targetField, criteria.getValue(field));
            } else {
                // JqTool Engine Needed, Support Tree
                if (Ut.isJObject(criteria.getValue(field)) || field.equals(Strings.EMPTY)) {
                    if (Ut.isJObject(criteria.getValue(field))) {
                        final JsonObject valueJson = criteria.getJsonObject(field);
                        criterias.put(field, criteria(valueJson, mojo));
                    } else {
                        criterias.put(field, criteria.getValue(field));
                    }
                } else {
                    criterias.put(field, criteria.getValue(field));
                }
            }

        }
        return criterias;
    }

    private static JsonArray projection(final JsonArray projections, final Mojo mojo) {
        final JsonArray result = new JsonArray();
        final ConcurrentMap<String, String> mapping = mojo.getIn();
        Ut.itJArray(projections, String.class, (item, index) ->
                result.add(null == mapping.get(item) ? item : mapping.get(item)));
        return result;
    }


    private static JsonArray sorter(final JsonArray sorter, final Mojo mojo) {
        final JsonArray sorters = new JsonArray();
        final ConcurrentMap<String, String> mapping = mojo.getIn();
        Ut.itJArray(sorter, String.class, (item, index) -> {
            final String key = item.contains(Strings.COMMA) ? item.split(Strings.COMMA)[0] : item;
            if (mapping.containsKey(key)) {
                final String targetField = mapping.get(key);
                if (item.contains(Strings.COMMA)) {
                    sorters.add(targetField + Strings.COMMA + item.split(Strings.COMMA)[1]);
                } else {
                    sorters.add(targetField + Strings.COMMA + "ASC");
                }
            } else {
                sorters.add(item);
            }
        });
        return sorters;
    }
}
