package io.vertx.tp.plugin;

import io.reactivex.Observable;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.tp.plugin.mongo.MongoInfix;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.Objects;
import java.util.function.BinaryOperator;

public class UxMongo {

    private static final MongoClient CLIENT = MongoInfix.getClient();

    private static final Annal LOGGER = Annal.get(UxMongo.class);

    public Future<Boolean> missing(final String collection, final JsonObject filter) {
        return Fn.thenUnbox(future -> CLIENT.findOne(collection, filter, null, res -> {
            LOGGER.debug(Info.MONGO_FILTER, collection, filter, res.result());
            future.complete(null == res.result());
        }));
    }

    public Future<Boolean> existing(final String collection, final JsonObject filter) {
        return Fn.thenUnbox(future -> CLIENT.findOne(collection, filter, null, res -> {
            LOGGER.debug(Info.MONGO_FILTER, collection, filter, res.result());
            future.complete(null != res.result());
        }));
    }

    public JsonObject termIn(final JsonObject filter, final String field, final JsonArray values) {
        final JsonObject terms = new JsonObject();
        if (null != filter) {
            terms.mergeIn(filter);
        }
        return terms.put(field, new JsonObject().put("$in", values));
    }

    public JsonObject termLike(final JsonObject filter, final String field, final String value) {
        final JsonObject terms = new JsonObject();
        if (null != filter) {
            terms.mergeIn(filter);
        }
        return terms.put(field, new JsonObject().put("$regex", ".*" + value + ".*"));
    }

    public Future<JsonObject> insert(final String collection, final JsonObject data) {
        return Fn.thenUnbox(future -> CLIENT.insert(collection, data, res -> {
            if (res.succeeded()) {
                LOGGER.debug(Info.MONGO_INSERT, collection, data);
                future.complete(data);
            } else {
                LOGGER.debug(Info.MONGO_INSERT, collection, null);
                future.complete();
            }
        }));
    }

    public Future<JsonObject> findOne(final String collection, final JsonObject filter) {
        return Fn.thenUnbox(future -> CLIENT.findOne(collection, filter, null, res -> {
            LOGGER.debug(Info.MONGO_FILTER, collection, filter, res.result());
            future.complete(res.result());
        }));
    }

    public Future<JsonObject> findOne(final String collection, final JsonObject filter,
                                      final String joinedCollection, final String joinedKey, final JsonObject additional,
                                      final BinaryOperator<JsonObject> operatorFun) {
        final JsonObject data = new JsonObject();
        return this.findOne(collection, filter)
            .compose(result -> {
                data.mergeIn(result);
                final JsonObject joinedFilter = (null == additional) ? new JsonObject() : additional.copy();
                // MongoDB only
                joinedFilter.put(joinedKey, result.getValue("_id"));
                return this.findOne(joinedCollection, joinedFilter);
            })
            .compose(second -> Future.succeededFuture(operatorFun.apply(data, second)));
    }

    public Future<JsonObject> findOneAndReplace(final String collection, final JsonObject filter,
                                                final JsonObject updated) {
        // Find first for field update
        return Fn.thenUnbox(future -> CLIENT.findOne(collection, filter, null, handler -> {
            if (handler.succeeded()) {
                final JsonObject data = handler.result().mergeIn(updated);
                CLIENT.findOneAndReplace(collection, filter, data, result -> {
                    LOGGER.debug(Info.MONGO_UPDATE, collection, filter, data);
                    future.complete(data);
                });
            } else {
                future.complete(updated);
            }
        }));
    }

    public Future<Long> removeDocument(final String collection, final JsonObject filter) {
        return Fn.thenUnbox(future -> CLIENT.removeDocument(collection, filter, res -> {
            final Long removed = res.result().getRemovedCount();
            LOGGER.debug(Info.MONGO_DELETE, collection, filter, removed);
            future.complete(removed);
        }));
    }

    public Future<JsonArray> findWithOptions(final String collection, final JsonObject filter,
                                             final FindOptions options) {
        return Fn.thenUnbox(future -> CLIENT.findWithOptions(collection, filter, options, res -> {
            final JsonArray result = new JsonArray();
            Observable.fromIterable(res.result())
                .filter(Objects::nonNull)
                .subscribe(result::add)
                .dispose();
            LOGGER.debug(Info.MONGO_FIND, collection, filter, options.toJson(), result);
            future.complete(result);
        }));
    }

    public Future<JsonArray> findWithOptions(final String collection, final JsonObject filter, final FindOptions options,
                                             // Secondary JqTool
                                             final String joinedCollection, final String joinedKey, final JsonObject additional,
                                             final BinaryOperator<JsonObject> operatorFun) {
        return Fn.arrangeA(this.findWithOptions(collection, filter, options),
            item -> {
                final JsonObject joinedFilter = (null == additional) ? new JsonObject() : additional.copy();
                // MongoDB only
                joinedFilter.put(joinedKey, item.getValue("_id"));
                return this.findOne(joinedCollection, joinedFilter);
            }, operatorFun);
    }
}
