package io.vertx.up.uca.jooq;

import io.horizon.uca.qr.Sorter;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.jooq.util.JqFlow;

import java.util.List;

/**
 * Jooq Splitted Reader
 * SELECT
 * COUNT
 * - Search, Check, Find
 */
@SuppressWarnings("all")
class JqReader {

    private transient ActionSearch search;
    private transient ActionFetch fetch;
    private transient ActionCheck check;

    private JqReader(final JqAnalyzer analyzer) {
        /*
         * New Structure for more details
         */
        this.search = new ActionSearch(analyzer);
        this.fetch = new ActionFetch(analyzer);
        this.check = new ActionCheck(analyzer);
    }

    static JqReader create(final JqAnalyzer analyzer) {
        return new JqReader(analyzer);
    }

    // ============ Search Processing =============
    <T> Future<JsonObject> searchAsync(final JsonObject params, final JqFlow workflow) {
        return this.search.searchAsync(params, workflow);
    }

    <T> JsonObject search(final JsonObject params, final JqFlow workflow) {
        return this.search.search(params, workflow);
    }

    // ============ Fetch Processing ( New for Sorter ) ================
    <T> Future<List<T>> fetchAllAsync() {
        return this.fetch.fetchAllAsync();
    }

    <T> List<T> fetchAll() {
        return this.fetch.fetchAll();
    }

    <T> Future<List<T>> fetchAsync(final String field, final Object value) {
        return this.fetch.fetchAsync(field, value);
    }

    <T> List<T> fetch(final String field, final Object value) {
        return this.fetch.fetch(field, value);
    }

    <T> Future<List<T>> fetchAsync(final JsonObject criteria) {
        return this.fetch.fetchAsync(criteria);
    }

    <T> Future<List<T>> fetchAsync(final JsonObject criteria, final Sorter sorter) {
        return this.fetch.fetchAsync(criteria, sorter);
    }

    <T> List<T> fetch(final JsonObject criteria) {
        return this.fetch.fetch(criteria);
    }

    <T> List<T> fetch(final JsonObject criteria, final Sorter sorter) {
        return this.fetch.fetch(criteria, sorter);
    }

    <T, ID> Future<T> fetchByIdAsync(final ID id) {
        return this.fetch.fetchByIdAsync(id);
    }

    <T, ID> T fetchById(final ID id) {
        return this.fetch.fetchById(id);
    }

    // ============ Fetch One Operation =============
    /* Async fetch one operation: SELECT */
    <T> Future<T> fetchOneAsync(final String field, final Object value) {
        return this.fetch.fetchOneAsync(field, value);
    }

    <T> T fetchOne(final String field, final Object value) {
        return this.fetch.fetchOne(field, value);
    }

    <T> Future<T> fetchOneAsync(final JsonObject criteria) {
        return this.fetch.fetchOneAsync(criteria);
    }

    <T> T fetchOne(final JsonObject criteria) {
        return this.fetch.fetchOne(criteria);
    }

    // ============ Exist Operation =============
    Future<Boolean> existByIdAsync(final Object id) {
        return this.check.existByIdAsync(id);
    }

    Boolean existById(final Object id) {
        return this.check.existById(id);
    }

    Future<Boolean> existAsync(final JsonObject criteria) {
        return this.check.existAsync(criteria);
    }

    Boolean exist(final JsonObject criteria) {
        return this.check.exist(criteria);
    }
}
