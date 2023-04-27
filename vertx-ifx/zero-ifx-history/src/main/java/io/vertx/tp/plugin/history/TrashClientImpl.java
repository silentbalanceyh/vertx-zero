package io.vertx.tp.plugin.history;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

public class TrashClientImpl implements TrashClient {
    private final transient Vertx vertxRef;
    private final transient TrashBuilder builder;

    public TrashClientImpl(final Vertx vertxRef, final String identifier) {
        this.vertxRef = vertxRef;
        this.builder = new TrashBuilder(identifier).init();
    }

    @Override
    public Future<JsonObject> backupAsync(final JsonObject record, final MultiMap params) {
        final JsonObject content = Ut.valueJObject(record);
        if (Ut.isNotNil(content)) {
            this.builder.createHistory(record, params);
        }
        return Future.succeededFuture(record);
    }

    @Override
    public Future<JsonArray> backupAsync(final JsonArray records, final MultiMap params) {
        final JsonArray content = Ut.valueJArray(records);
        if (!content.isEmpty()) {
            this.builder.createHistory(content, params);
        }
        return Future.succeededFuture(records);
    }

    @Override
    public Future<JsonObject> backupAsync(final JsonObject record) {
        return this.backupAsync(record, null);
    }

    @Override
    public Future<JsonArray> backupAsync(final JsonArray records) {
        return this.backupAsync(records, null);
    }

    @Override
    public Future<JsonObject> restoreAsync(final JsonObject record, final MultiMap params) {
        /*
         * Wait for future
         */
        return null;
    }
}
