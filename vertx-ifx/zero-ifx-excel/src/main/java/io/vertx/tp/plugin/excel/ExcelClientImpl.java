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
import io.vertx.tp.plugin.excel.atom.ExTenant;
import io.vertx.up.commune.element.TypeAtom;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

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
        final JsonArray mapping = config.getJsonArray(ExcelClient.MAPPING, new JsonArray());
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
        if (config.containsKey(ExcelClient.TENANT)) {
            final JsonObject tenantJson = Ut.ioJObject(config.getString(ExcelClient.TENANT));
            if (Ut.notNil(tenantJson)) {
                final ExTenant tenant = ExTenant.create(tenantJson);
                this.helper.initTenant(tenant);
                LOGGER.debug("[ Έξοδος ] Configuration tenant for Importing: {0}", tenantJson.encode());
            }
        }
        return this;
    }

    // --------------------- ExTable Ingesting -----------------------

    /**
     * 1) Api for ingest of parameters, the matrix is as following
     * |    filename/input-stream   |    2003/2007                       |     Shape     |      Async       |
     * |        filename            |    by extension ( .xls, .xlsx )    |       x       |      false       |
     * |        input-stream        |    isXlsx, true for 2007           |       x       |      false       |
     * |        filename            |    by extension ( .xls, .xlsx )    |       V       |      false       |
     * |        input-stream        |    isXlsx, true for 2007           |       V       |      false       |
     * |        filename            |    by extension ( .xls, .xlsx )    |       x       |      true        |
     * |        input-stream        |    isXlsx, true for 2007           |       x       |      true        |
     * |        filename            |    by extension ( .xls, .xlsx )    |       V       |      true        |
     * |        input-stream        |    isXlsx, true for 2007           |       V       |      true        |
     *
     * 2) 12 mode of `ingest` here
     * 2.1) The input contains two categories:
     * -- 1. InputStream for byte array input, in this mode, you must provide `isXlsx` parameter
     * -- 2. filename of input, the format should be distinguish by file extension `.xls` for 2003, `.xlsx` for 2007
     * 2.2) The Shape contains `Dynamic` importing in Ox channel or other build `Shape` object, it contains type definition
     * 2.2) async contains ( Sync, Callback, Future ) three mode
     */
    @Override
    public Set<ExTable> ingest(final String filename) {
        return this.ingest.ingest(filename);
    }

    @Override
    public Set<ExTable> ingest(final String filename, final TypeAtom typeAtom) {
        return this.ingest.ingest(filename, typeAtom);
    }

    @Override
    public Set<ExTable> ingest(final InputStream in, final boolean isXlsx) {
        return this.ingest.ingest(in, isXlsx);
    }

    @Override
    public Set<ExTable> ingest(final InputStream in, final boolean isXlsx, final TypeAtom typeAtom) {
        return this.ingest.ingest(in, isXlsx, typeAtom);
    }

    @Override
    public Future<Set<ExTable>> ingestAsync(final String filename) {
        return Future.succeededFuture(this.ingest(filename));
    }

    @Override
    public Future<Set<ExTable>> ingestAsync(final String filename, final TypeAtom typeAtom) {
        return Future.succeededFuture(this.ingest(filename, typeAtom));
    }

    @Override
    public Future<Set<ExTable>> ingestAsync(final InputStream in, final boolean isXlsx) {
        return Future.succeededFuture(this.ingest(in, isXlsx));
    }

    @Override
    public Future<Set<ExTable>> ingestAsync(final InputStream in, final boolean isXlsx, final TypeAtom typeAtom) {
        return Future.succeededFuture(this.ingest(in, isXlsx, typeAtom));
    }

    @Override
    @Fluent
    public ExcelClient ingest(final String filename, final Handler<AsyncResult<Set<ExTable>>> handler) {
        handler.handle(this.ingestAsync(filename));
        return this;
    }

    @Override
    @Fluent
    public ExcelClient ingest(final String filename, final TypeAtom typeAtom, final Handler<AsyncResult<Set<ExTable>>> handler) {
        handler.handle(this.ingestAsync(filename, typeAtom));
        return this;
    }

    @Override
    @Fluent
    public ExcelClient ingest(final InputStream in, final boolean isXlsx, final Handler<AsyncResult<Set<ExTable>>> handler) {
        handler.handle(this.ingestAsync(in, isXlsx));
        return this;
    }

    @Override
    @Fluent
    public ExcelClient ingest(final InputStream in, final boolean isXlsx, final TypeAtom typeAtom, final Handler<AsyncResult<Set<ExTable>>> handler) {
        handler.handle(this.ingestAsync(in, isXlsx, typeAtom));
        return this;
    }

    // --------------------- ExTable Loading / Importing -----------------------

    /**
     * 1) Api for ingest of parameters, the matrix is as following
     * |    filename/input-stream   |    2003/2007                       |     Shape     |      Async       |
     * |        filename            |    by extension ( .xls, .xlsx )    |       x       |      true        |
     * |        input-stream        |    isXlsx, true for 2007           |       x       |      true        |
     * |        filename            |    by extension ( .xls, .xlsx )    |       V       |      true        |
     * |        input-stream        |    isXlsx, true for 2007           |       V       |      true        |
     *
     * 2) 12 mode of `ingest` here
     * 2.1) The input contains two categories:
     * -- 1. InputStream for byte array input, in this mode, you must provide `isXlsx` parameter
     * -- 2. filename of input, the format should be distinguish by file extension `.xls` for 2003, `.xlsx` for 2007
     * 2.2) The Shape contains `Dynamic` importing in Ox channel or other build `Shape` object, it contains type definition
     * 2.2) async contains ( Sync, Callback, Future ) three mode
     */
    @Override
    @Fluent
    public <T> ExcelClient importAsync(final String filename, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(filename, res -> handler.handle(this.importer.importAsync(res)));
    }

    @Override
    @Fluent
    public <T> ExcelClient importAsync(final InputStream in, final boolean isXlsx, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(in, isXlsx, res -> handler.handle(this.importer.importAsync(res)));
    }

    @Override
    public <T> ExcelClient importAsync(final String filename, final TypeAtom typeAtom, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(filename, typeAtom, res -> handler.handle(this.importer.importAsync(res)));
    }

    @Override
    public <T> ExcelClient importAsync(final InputStream in, final boolean isXlsx, final TypeAtom typeAtom, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(in, isXlsx, typeAtom, res -> handler.handle(this.importer.importAsync(res)));
    }

    @Override
    public <T> Future<Set<T>> importAsync(final String filename) {
        return this.ingestAsync(filename).compose(this.importer::importAsync);
    }

    @Override
    public <T> Future<Set<T>> importAsync(final String filename, final TypeAtom typeAtom) {
        return this.ingestAsync(filename, typeAtom).compose(this.importer::importAsync);
    }

    @Override
    public <T> Future<Set<T>> importAsync(final InputStream in, final boolean isXlsx) {
        return this.ingestAsync(in, isXlsx).compose(this.importer::importAsync);
    }

    @Override
    public <T> Future<Set<T>> importAsync(final InputStream in, final boolean isXlsx, final TypeAtom typeAtom) {
        return this.ingestAsync(in, isXlsx, typeAtom).compose(this.importer::importAsync);
    }

    @Override
    public <T> ExcelClient importAsync(final String filename, final Handler<AsyncResult<Set<T>>> handler, final String... includes) {
        return this.ingest(filename, res -> handler.handle(this.importer.importAsync(this.ingest.compressAsync(res.result(), includes))));
    }

    @Override
    public <T> ExcelClient importAsync(final String filename, final TypeAtom typeAtom, final Handler<AsyncResult<Set<T>>> handler, final String... includes) {
        return this.ingest(filename, typeAtom, res -> handler.handle(this.importer.importAsync(this.ingest.compressAsync(res.result(), includes))));
    }

    @Override
    public <T> ExcelClient importAsync(final InputStream in, final boolean isXlsx, final Handler<AsyncResult<Set<T>>> handler, final String... includes) {
        return this.ingest(in, isXlsx, res -> handler.handle(this.importer.importAsync(this.ingest.compressAsync(res.result(), includes))));
    }

    @Override
    public <T> ExcelClient importAsync(final InputStream in, final boolean isXlsx, final TypeAtom typeAtom, final Handler<AsyncResult<Set<T>>> handler, final String... includes) {
        return this.ingest(in, isXlsx, typeAtom, res -> handler.handle(this.importer.importAsync(this.ingest.compressAsync(res.result(), includes))));
    }

    @Override
    public <T> Future<Set<T>> importAsync(final String filename, final String... includes) {
        return this.ingestAsync(filename).compose(tables -> this.ingest.compressAsync(tables, includes)).compose(this.importer::importAsync);
    }

    @Override
    public <T> Future<Set<T>> importAsync(final String filename, final TypeAtom typeAtom, final String... includes) {
        return this.ingestAsync(filename, typeAtom).compose(tables -> this.ingest.compressAsync(tables, includes)).compose(this.importer::importAsync);
    }

    @Override
    public <T> Future<Set<T>> importAsync(final InputStream in, final boolean isXlsx, final String... includes) {
        return this.ingestAsync(in, isXlsx).compose(tables -> this.ingest.compressAsync(tables, includes)).compose(this.importer::importAsync);
    }

    @Override
    public <T> Future<Set<T>> importAsync(final InputStream in, final boolean isXlsx, final TypeAtom typeAtom, final String... includes) {
        return this.ingestAsync(in, isXlsx, typeAtom).compose(tables -> this.ingest.compressAsync(tables, includes)).compose(this.importer::importAsync);
    }

    // --------------------- ExTable Exporting -----------------------
    @Override
    public ExcelClient exportAsync(final String identifier, final JsonArray data, final Handler<AsyncResult<Buffer>> handler) {
        this.exporter.exportData(identifier, data, TypeAtom.create(), handler);
        return this;
    }

    @Override
    public ExcelClient exportAsync(final String identifier, final JsonArray data,
                                   final TypeAtom typeAtom, final Handler<AsyncResult<Buffer>> handler) {
        this.exporter.exportData(identifier, data, typeAtom, handler);
        return this;
    }

    @Override
    public Future<Buffer> exportAsync(final String identifier, final JsonArray data,
                                      final TypeAtom typeAtom) {
        return this.exporter.exportData(identifier, data, typeAtom);
    }

    @Override
    public Future<Buffer> exportAsync(final String identifier, final JsonArray data) {
        return this.exporter.exportData(identifier, data, TypeAtom.create());
    }

    // --------------------- Spec Workflow -----------------------
    @Override
    public Future<JsonArray> extractAsync(final ExTable table) {
        return this.helper.extract(table);
    }

    @Override
    public Future<JsonArray> extractAsync(final Set<ExTable> tables) {
        return this.helper.extract(tables);
    }
}
