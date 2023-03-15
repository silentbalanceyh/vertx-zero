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
import io.vertx.up.experiment.mixture.HTAtom;
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

    Future<Set<ExTable>> ingestAsync(String filename, HTAtom HTAtom);

    Future<Set<ExTable>> ingestAsync(InputStream in, boolean isXlsx);

    Future<Set<ExTable>> ingestAsync(InputStream in, boolean isXlsx, HTAtom HTAtom);

    Set<ExTable> ingest(String filename);

    Set<ExTable> ingest(String filename, HTAtom HTAtom);

    Set<ExTable> ingest(InputStream in, boolean isXlsx);

    Set<ExTable> ingest(InputStream in, boolean isXlsx, HTAtom HTAtom);

    @Fluent
    ExcelClient ingest(String filename, Handler<AsyncResult<Set<ExTable>>> handler);


    // --------------------- ExTable Exporting -----------------------

    @Fluent
    ExcelClient ingest(String filename, HTAtom HTAtom, Handler<AsyncResult<Set<ExTable>>> handler);

    @Fluent
    ExcelClient ingest(InputStream in, boolean isXlsx, Handler<AsyncResult<Set<ExTable>>> handler);

    @Fluent
    ExcelClient ingest(InputStream in, boolean isXlsx, HTAtom HTAtom, Handler<AsyncResult<Set<ExTable>>> handler);

    Future<Buffer> exportAsync(String identifier, JsonArray data);

    // --------------------- ExTable Loading / Importing -----------------------

    Future<Buffer> exportAsync(String identifier, JsonArray data, HTAtom HTAtom);

    @Fluent
    ExcelClient exportAsync(String identifier, JsonArray data, Handler<AsyncResult<Buffer>> handler);

    @Fluent
    ExcelClient exportAsync(String identifier, JsonArray data, HTAtom HTAtom, Handler<AsyncResult<Buffer>> handler);

    @Fluent
    <T> ExcelClient importAsync(String filename, Handler<AsyncResult<Set<T>>> handler);

    @Fluent
    <T> ExcelClient importAsync(String filename, HTAtom HTAtom, Handler<AsyncResult<Set<T>>> handler);

    @Fluent
    <T> ExcelClient importAsync(InputStream in, boolean isXlsx, Handler<AsyncResult<Set<T>>> handler);

    @Fluent
    <T> ExcelClient importAsync(InputStream in, boolean isXlsx, HTAtom HTAtom, Handler<AsyncResult<Set<T>>> handler);

    <T> Future<Set<T>> importAsync(String filename);

    <T> Future<Set<T>> importAsync(String filename, HTAtom HTAtom);

    <T> Future<Set<T>> importAsync(InputStream in, boolean isXlsx);

    <T> Future<Set<T>> importAsync(InputStream in, boolean isXlsx, HTAtom HTAtom);

    /*
     * Filtered by `includes`
     */
    @Fluent
    <T> ExcelClient importAsync(String filename, Handler<AsyncResult<Set<T>>> handler, String... includes);

    @Fluent
    <T> ExcelClient importAsync(String filename, HTAtom HTAtom, Handler<AsyncResult<Set<T>>> handler, String... includes);

    @Fluent
    <T> ExcelClient importAsync(InputStream in, boolean isXlsx, Handler<AsyncResult<Set<T>>> handler, String... includes);

    @Fluent
    <T> ExcelClient importAsync(InputStream in, boolean isXlsx, HTAtom HTAtom, Handler<AsyncResult<Set<T>>> handler, String... includes);

    <T> Future<Set<T>> importAsync(String filename, String... includes);

    <T> Future<Set<T>> importAsync(String filename, HTAtom HTAtom, String... includes);

    <T> Future<Set<T>> importAsync(InputStream in, boolean isXlsx, String... includes);

    <T> Future<Set<T>> importAsync(InputStream in, boolean isXlsx, HTAtom HTAtom, String... includes);

    Future<JsonArray> extractAsync(final ExTable table);

    Future<JsonArray> extractAsync(final Set<ExTable> tables);
}
