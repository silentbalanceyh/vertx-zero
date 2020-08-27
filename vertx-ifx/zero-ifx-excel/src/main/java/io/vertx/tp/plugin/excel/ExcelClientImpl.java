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
import io.vertx.up.log.Annal;

import java.io.InputStream;
import java.util.Set;

public class ExcelClientImpl implements ExcelClient {

    private static final Annal LOGGER = Annal.get(ExcelClientImpl.class);

    private transient final Vertx vertx;
    private transient final ExcelHelper helper = ExcelHelper.helper(this.getClass());
    // Excel Exporter
    private transient final SheetExport exporter = SheetExport.create(this.helper);
    private transient final SheetIngest ingest = SheetIngest.create(this.helper);
    private transient final SheetImport importer = SheetImport.create(this.helper);

    ExcelClientImpl(final Vertx vertx, final JsonObject config) {
        this.vertx = vertx;
        this.init(config);
    }

    @Override
    public ExcelClient init(final JsonObject config) {
        final JsonArray mapping = config.getJsonArray(ExcelClient.MAPPING);
        this.helper.initConnect(mapping);
        LOGGER.debug("[ Έξοδος ] Configuration finished: {0}", Pool.CONNECTS.size());
        if (config.containsKey(ExcelClient.ENVIRONMENT)) {
            final JsonArray environments = config.getJsonArray(ExcelClient.ENVIRONMENT);
            this.helper.initEnvironment(environments);
            LOGGER.debug("[ Έξοδος ] Configuration environments: {0}", environments.encode());
        }
        if (config.containsKey(ExcelClient.PEN)) {
            final String componentStr = config.getString(ExcelClient.PEN);
            this.helper.initPen(componentStr);
            LOGGER.debug("[ Έξοδος ] Configuration pen for Exporting: {0}", componentStr);
        }
        return this;
    }

    // --------------------- ExTable Ingesting -----------------------
    @Override
    public Set<ExTable> ingest(final String filename) {
        return this.ingest.ingest(filename);
    }

    @Override
    public Set<ExTable> ingest(final InputStream in, final boolean isXlsx) {
        return this.ingest.ingest(in, isXlsx);
    }

    @Override
    @Fluent
    public ExcelClient ingest(final String filename, final Handler<AsyncResult<Set<ExTable>>> handler) {
        this.ingest.ingest(filename, handler);
        return this;
    }

    @Override
    @Fluent
    public ExcelClient ingest(final InputStream in, final boolean isXlsx, final Handler<AsyncResult<Set<ExTable>>> handler) {
        this.ingest.ingest(in, isXlsx, handler);
        return this;
    }

    // --------------------- ExTable Loading / Importing -----------------------
    @Override
    @Fluent
    public <T> ExcelClient importAsync(final String filename, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(filename,
                res -> handler.handle(this.importer.importAsync(res)));
    }


    @Override
    @Fluent
    public <T> ExcelClient importAsync(final InputStream in, final boolean isXlsx, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(in, isXlsx,
                res -> handler.handle(this.importer.importAsync(res)));
    }

    @Override
    @Fluent
    public <T> ExcelClient importAsync(final String tableOnly, final String filename, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(filename, this.ingest.ingestAsync(tableOnly,
                res -> handler.handle(this.importer.importAsync(res))));
    }

    @Override
    @Fluent
    public <T> ExcelClient importAsync(final String tableOnly, final InputStream in, final boolean isXlsx, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(in, isXlsx, this.ingest.ingestAsync(tableOnly,
                res -> handler.handle(this.importer.importAsync(res))));
    }

    @Override
    public <T> T saveEntity(final JsonObject data, final ExTable table) {
        return this.importer.saveEntity(data, table);
    }

    // --------------------- ExTable Exporting -----------------------
    @Override
    public ExcelClient exportAsync(final String identifier, final JsonArray data, final Handler<AsyncResult<Buffer>> handler) {
        this.exporter.exportData(identifier, data, Shape.create(), handler);
        return this;
    }

    @Override
    public ExcelClient exportAsync(final String identifier, final JsonArray data,
                                   final Shape shape, final Handler<AsyncResult<Buffer>> handler) {
        this.exporter.exportData(identifier, data, shape, handler);
        return this;
    }

    @Override
    public Future<Buffer> exportAsync(final String identifier, final JsonArray data,
                                      final Shape shape) {
        return this.exporter.exportData(identifier, data, shape);
    }

    @Override
    public Future<Buffer> exportAsync(final String identifier, final JsonArray data) {
        return this.exporter.exportData(identifier, data, Shape.create());
    }
}
