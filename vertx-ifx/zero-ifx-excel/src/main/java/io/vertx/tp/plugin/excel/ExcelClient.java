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
import io.vertx.up.commune.element.TypeAtom;
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
    String TENANT = "tenant";
    String BOOT = "boot";

    static ExcelClient createShared(final Vertx vertx, final JsonObject config) {
        return new ExcelClientImpl(vertx, config);
    }

    // --------------------- ExTable Ingesting -----------------------

    @Fluent
    @Override
    ExcelClient init(JsonObject params);

    Future<Set<ExTable>> ingestAsync(String filename);

    Future<Set<ExTable>> ingestAsync(String filename, TypeAtom TypeAtom);

    Future<Set<ExTable>> ingestAsync(InputStream in, boolean isXlsx);

    Future<Set<ExTable>> ingestAsync(InputStream in, boolean isXlsx, TypeAtom TypeAtom);

    Set<ExTable> ingest(String filename);

    Set<ExTable> ingest(String filename, TypeAtom TypeAtom);

    Set<ExTable> ingest(InputStream in, boolean isXlsx);

    Set<ExTable> ingest(InputStream in, boolean isXlsx, TypeAtom TypeAtom);

    @Fluent
    ExcelClient ingest(String filename, Handler<AsyncResult<Set<ExTable>>> handler);


    // --------------------- ExTable Exporting -----------------------

    @Fluent
    ExcelClient ingest(String filename, TypeAtom TypeAtom, Handler<AsyncResult<Set<ExTable>>> handler);

    @Fluent
    ExcelClient ingest(InputStream in, boolean isXlsx, Handler<AsyncResult<Set<ExTable>>> handler);

    @Fluent
    ExcelClient ingest(InputStream in, boolean isXlsx, TypeAtom TypeAtom, Handler<AsyncResult<Set<ExTable>>> handler);

    Future<Buffer> exportAsync(String identifier, JsonArray data);

    // --------------------- ExTable Loading / Importing -----------------------

    Future<Buffer> exportAsync(String identifier, JsonArray data, TypeAtom TypeAtom);

    @Fluent
    ExcelClient exportAsync(String identifier, JsonArray data, Handler<AsyncResult<Buffer>> handler);

    @Fluent
    ExcelClient exportAsync(String identifier, JsonArray data, TypeAtom TypeAtom, Handler<AsyncResult<Buffer>> handler);

    @Fluent
    <T> ExcelClient importAsync(String filename, Handler<AsyncResult<Set<T>>> handler);

    @Fluent
    <T> ExcelClient importAsync(String filename, TypeAtom TypeAtom, Handler<AsyncResult<Set<T>>> handler);

    @Fluent
    <T> ExcelClient importAsync(InputStream in, boolean isXlsx, Handler<AsyncResult<Set<T>>> handler);

    @Fluent
    <T> ExcelClient importAsync(InputStream in, boolean isXlsx, TypeAtom TypeAtom, Handler<AsyncResult<Set<T>>> handler);

    <T> Future<Set<T>> importAsync(String filename);

    <T> Future<Set<T>> importAsync(String filename, TypeAtom TypeAtom);

    <T> Future<Set<T>> importAsync(InputStream in, boolean isXlsx);

    <T> Future<Set<T>> importAsync(InputStream in, boolean isXlsx, TypeAtom TypeAtom);

    /*
     * Filtered by `includes`
     */
    @Fluent
    <T> ExcelClient importAsync(String filename, Handler<AsyncResult<Set<T>>> handler, String... includes);

    @Fluent
    <T> ExcelClient importAsync(String filename, TypeAtom TypeAtom, Handler<AsyncResult<Set<T>>> handler, String... includes);

    @Fluent
    <T> ExcelClient importAsync(InputStream in, boolean isXlsx, Handler<AsyncResult<Set<T>>> handler, String... includes);

    @Fluent
    <T> ExcelClient importAsync(InputStream in, boolean isXlsx, TypeAtom TypeAtom, Handler<AsyncResult<Set<T>>> handler, String... includes);

    <T> Future<Set<T>> importAsync(String filename, String... includes);

    <T> Future<Set<T>> importAsync(String filename, TypeAtom TypeAtom, String... includes);

    <T> Future<Set<T>> importAsync(InputStream in, boolean isXlsx, String... includes);

    <T> Future<Set<T>> importAsync(InputStream in, boolean isXlsx, TypeAtom TypeAtom, String... includes);

    Future<JsonArray> extractAsync(final ExTable table);

    Future<JsonArray> extractAsync(final Set<ExTable> tables);
}
