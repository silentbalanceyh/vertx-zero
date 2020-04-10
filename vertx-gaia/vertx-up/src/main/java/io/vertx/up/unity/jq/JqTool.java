package io.vertx.up.unity.jq;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.pojo.Mirror;
import io.vertx.up.atom.pojo.Mojo;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;

public class JqTool {

    private static final Annal LOGGER = Annal.get(JqTool.class);

    static <T> Future<T> future(
            final CompletableFuture<T> completableFuture
    ) {
        final Promise<T> future = Promise.promise();
        completableFuture.thenAcceptAsync(future::complete)
                .exceptionally((ex) -> {
                    LOGGER.jvm(ex);
                    future.fail(ex);
                    return null;
                });
        return future.future();
    }

    static <T> ConcurrentMap<ChangeFlag, List<T>> compared(final List<T> current, final List<T> original,
                                                           final BiPredicate<T, T> finder, final BinaryOperator<T> combiner) {
        /*
         * Combine original / and last list
         */
        final List<T> addQueue = new ArrayList<>();
        final List<T> updateQueue = new ArrayList<>();
        /*
         * Only get `ADD` & `UPDATE`
         * Iterate original list
         * 1) If the entity is missing in original, ADD
         * 2) If the entity is existing in original, UPDATE
         */
        current.forEach(newRecord -> {
            /*
             * New record found in `original`
             */
            final T found = Ut.elementFind(original, oldRecord -> finder.test(oldRecord, newRecord));
            if (Objects.isNull(found)) {
                addQueue.add(newRecord);
            } else {
                final T combine = combiner.apply(found, newRecord);
                updateQueue.add(combine);
            }
        });
        return new ConcurrentHashMap<ChangeFlag, List<T>>() {
            {
                this.put(ChangeFlag.ADD, addQueue);
                this.put(ChangeFlag.UPDATE, updateQueue);
            }
        };
    }

    static Inquiry getInquiry(final JsonObject envelop, final String pojo) {
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

    static Inquiry getInquiry(final JsonObject data, final Mojo mojo) {
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
