package io.vertx.up.uca.jooq;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.pojo.Mojo;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.unity.Ux;

import java.util.Set;

/*
 * Join Operation Complex JqTool Component
 */
@SuppressWarnings("all")
class JoinEngine {
    private final transient JoinStore store;
    private final transient JoinSearch search;
    private final transient JoinUnique unique;
    private final transient JoinWriter writer;

    JoinEngine() {
        this.store = new JoinStore();
        this.search = new JoinSearch(store);
        this.unique = new JoinUnique(store);
        this.writer = new JoinWriter(store);
    }

    <T> JoinEngine add(final Class<T> daoCls, final String field) {
        this.store.add(daoCls, field);
        return this;
    }

    <T> JoinEngine alias(final Class<?> daoCls, final String field, final String alias) {
        this.store.alias(daoCls, field, alias);
        return this;
    }

    <T> JoinEngine join(final Class<?> daoCls, final String field) {
        this.store.join(daoCls, field);
        return this;
    }

    <T> JoinEngine pojo(final Class<?> daoCls, final String pojo) {
        this.store.pojo(daoCls, pojo);
        return this;
    }

    // -------------------- Meta Data Processing
    Set<String> fieldFirst() {
        return this.store.metaFirstField();
    }

    // -------------------- Single Processing -----------
    Future<JsonObject> fetchById(final String key, final boolean isASub) {
        return this.unique.fetchById(key, isASub);
    }

    Future<Boolean> deleteById(final String key) {
        return Ux.futureT();
    }

    Future<JsonObject> insert(final JsonObject data, final JsonObject record) {
        return Ux.futureJ();
    }

    Future<JsonObject> insert(final JsonObject data, final JsonArray record) {
        return Ux.futureJ();
    }

    Future<JsonObject> update(final String key, final JsonObject data, final JsonObject record) {
        return Ux.futureJ();
    }

    Future<JsonObject> update(final String key, final JsonObject data, final JsonArray records) {
        return Ux.futureJ();
    }

    // -------------------- Search Operation -----------
    /*
     * Pagination Searching
     */
    Future<JsonObject> searchAsync(final Qr qr, final Mojo mojo) {
        final JsonObject response = new JsonObject();
        final JsonArray data = this.search.searchA(qr, mojo);

        response.put("list", data);
        final Long counter = this.search.count(qr);
        response.put("count", counter);
        return Ux.future(response);
    }

    JsonArray searchArray(final Qr qr, final Mojo mojo) {
        return this.search.searchA(qr, mojo);
    }

    Future<Long> countAsync(final Qr qr) {
        return Future.succeededFuture(this.search.count(qr));
    }
}
