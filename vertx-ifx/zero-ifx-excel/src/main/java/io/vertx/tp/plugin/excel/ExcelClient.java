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

    /**
     * Excel file data loading
     *
     * @param filename data input file
     * @param handler  callback handler to process Set<ExTable>
     * @return self reference
     */
    @Fluent
    <T> ExcelClient loading(String filename, Handler<AsyncResult<Set<T>>> handler);


    @Fluent
    ExcelClient ingest(String filename, Handler<AsyncResult<Set<ExTable>>> handler);

    Set<ExTable> ingest(String filename);


    @Fluent
    ExcelClient exportTable(String identifier, JsonArray data, Handler<AsyncResult<Buffer>> handler);

    Future<Buffer> exportTable(String identifier, JsonArray data);

    @Fluent
    <T> ExcelClient importTable(String tableOnly, String filename, Handler<AsyncResult<Set<T>>> handler);

    @Fluent
    <T> ExcelClient importTable(String tableOnly, InputStream in, Handler<AsyncResult<Set<T>>> handler);

    /**
     * Two format supported here: 2013 / 2017
     */
    @Fluent
    <T> ExcelClient loading(InputStream in, boolean isXlsx, Handler<AsyncResult<Set<T>>> handler);

    @Fluent
    ExcelClient ingest(InputStream in, boolean isXlsx, Handler<AsyncResult<Set<ExTable>>> handler);

    Set<ExTable> ingest(InputStream in, boolean isXlsx);

    /**
     * Save entity ( table -> data )
     */
    <T> T saveEntity(final JsonObject data, final ExTable table);
}
