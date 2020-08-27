package io.vertx.tp.plugin.excel;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.up.commune.element.Shape;
import io.vertx.up.plugin.TpClient;

import java.io.InputStream;
import java.util.Set;

/**
 * ExcelClient for office excel data loading
 * Apache Poi
 */
public interface ExcelClient extends TpClient<ExcelClient> {

    String MAPPING = "mapping";
    String ENVIRONMENT = "environment";
    String PEN = "pen";

    static ExcelClient createShared(final Vertx vertx, final JsonObject config) {
        return new ExcelClientImpl(vertx, config);
    }

    @Fluent
    @Override
    ExcelClient init(JsonObject params);


    // --------------------- ExTable Ingesting -----------------------
    Set<ExTable> ingest(String filename);

    @Fluent
    ExcelClient ingest(String filename, Handler<AsyncResult<Set<ExTable>>> handler);

    Set<ExTable> ingest(InputStream in, boolean isXlsx);

    @Fluent
    ExcelClient ingest(InputStream in, boolean isXlsx, Handler<AsyncResult<Set<ExTable>>> handler);

    // --------------------- ExTable Exporting -----------------------
    @Fluent
    ExcelClient exportAsync(String identifier, JsonArray data, Handler<AsyncResult<Buffer>> handler);

    @Fluent
    ExcelClient exportAsync(String identifier, JsonArray data, Shape shape, Handler<AsyncResult<Buffer>> handler);

    Future<Buffer> exportAsync(String identifier, JsonArray data);

    Future<Buffer> exportAsync(String identifier, JsonArray data, Shape type);

    // --------------------- ExTable Loading / Importing -----------------------
    @Fluent
    <T> ExcelClient importAsync(String filename, Handler<AsyncResult<Set<T>>> handler);

    @Fluent
    <T> ExcelClient importAsync(InputStream in, boolean isXlsx, Handler<AsyncResult<Set<T>>> handler);

    @Fluent
    <T> ExcelClient importAsync(String tableOnly, String filename, Handler<AsyncResult<Set<T>>> handler);

    @Fluent
    <T> ExcelClient importAsync(String tableOnly, InputStream in, boolean isXlsx, Handler<AsyncResult<Set<T>>> handler);

    /**
     * Save entity ( table -> data )
     */
    <T> T saveEntity(final JsonObject data, final ExTable table);
}
