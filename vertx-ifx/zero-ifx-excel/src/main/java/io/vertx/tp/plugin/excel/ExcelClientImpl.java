package io.vertx.tp.plugin.excel;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._500ExportingErrorException;
import io.vertx.tp.plugin.excel.atom.ExRecord;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.tp.plugin.excel.tool.ExFn;
import io.vertx.up.commune.element.Shape;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.jq.UxJooq;
import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelClientImpl implements ExcelClient {

    private static final Annal LOGGER = Annal.get(ExcelClientImpl.class);

    private transient final Vertx vertx;
    private transient final ExcelHelper helper = ExcelHelper.helper(this.getClass());

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

    @Override
    public ExcelClient ingest(final String filename, final Handler<AsyncResult<Set<ExTable>>> handler) {
        handler.handle(Future.succeededFuture(this.ingest(filename)));
        return this;
    }

    @Override
    public Set<ExTable> ingest(final String filename) {
        /* 1. Get Workbook reference */
        final Workbook workbook = this.helper.getWorkbook(filename);
        /* 2. Iterator for Sheet */
        return this.helper.getExTables(workbook);
    }

    @Override
    public ExcelClient ingest(final InputStream in, final boolean isXlsx, final Handler<AsyncResult<Set<ExTable>>> handler) {
        handler.handle(Future.succeededFuture(this.ingest(in, isXlsx)));
        return this;
    }

    @Override
    public Set<ExTable> ingest(final InputStream in, final boolean isXlsx) {
        /* 1. Get Workbook reference */
        final Workbook workbook = this.helper.getWorkbook(in, isXlsx);
        /* 2. Iterator for Sheet */
        return this.helper.getExTables(workbook);
    }

    @Override
    public <T> ExcelClient loading(final String filename, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(filename, process -> handler.handle(this.handleIngested(process)));
    }

    @Override
    @Fluent
    public <T> ExcelClient importTable(final String tableOnly, final String filename, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(filename, processed -> {
            if (processed.succeeded()) {
                /* Filtered valid table here */
                final Set<ExTable> execution = this.getFiltered(processed.result(), tableOnly);
                handler.handle(this.handleIngested(Ux.future(execution)));
            }
        });
    }

    @Override
    @Fluent
    public <T> ExcelClient importTable(final String tableOnly, final InputStream in, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(in, true, processed -> {
            if (processed.succeeded()) {
                /* Filtered valid table here */
                final Set<ExTable> execution = this.getFiltered(processed.result(), tableOnly);
                handler.handle(this.handleIngested(Ux.future(execution)));
            }
        });
    }

    @Override
    public <T> ExcelClient loading(final InputStream in, final boolean isXlsx, final Handler<AsyncResult<Set<T>>> handler) {
        return this.ingest(in, isXlsx, process -> handler.handle(this.handleIngested(process)));
    }

    private <T> Future<Set<T>> handleIngested(final AsyncResult<Set<ExTable>> async) {
        if (async.succeeded()) {
            final Set<ExTable> tables = async.result();
            /* 3. Loading data into the system */
            final Set<T> entitySet = new HashSet<>();
            tables.forEach(table -> this.extract(table).forEach(json -> {
                /* 4. Filters building */
                final T entity = this.saveEntity(json, table);
                if (Objects.nonNull(entity)) {
                    entitySet.add(entity);
                }
            }));
            /* 4. Set<T> handler */
            return Future.succeededFuture(entitySet);
        } else {
            return Future.succeededFuture();
        }
    }

    @Override
    public <T> T saveEntity(final JsonObject data, final ExTable table) {
        T reference = null;
        if (Objects.nonNull(table.getPojo()) && Objects.nonNull(table.getDao())) {
            /*
             * First, find the record by unique filters that defined in business here.
             */
            final JsonObject filters = table.whereUnique(data);
            LOGGER.debug("[ Έξοδος ] Filters: {0}, Table: {1}", filters.encode(), table.getName());
            final T entity = Ux.fromJson(data, table.getPojo(), table.getPojoFile());
            final UxJooq jooq = Ux.Jooq.on(table.getDao());
            if (null != jooq) {
                final String pojoFile = table.getPojoFile();
                if (Ut.notNil(pojoFile)) {
                    jooq.on(pojoFile);
                }
                /*
                 * Unique filter to fetch single record database here.
                 * Such as code + sigma
                 */
                final T queried = jooq.fetchOne(filters);
                if (null == queried) {
                    /*
                     * Here are two situations that we could be careful
                     * 1. Unique Condition in source does not change, do insert here.
                     * 2. Key Condition existing in database, do update here.
                     */
                    final String key = table.whereKey(data);
                    if (Ut.isNil(key)) {
                        /*
                         * No definition of key here, insert directly.
                         */
                        reference = jooq.insert(entity);
                    } else {
                        /*
                         * Double check to avoid issue:
                         * java.sql.SQLIntegrityConstraintViolationException: Duplicate entry 'xxx' for key 'PRIMARY'
                         */
                        final T fetched = jooq.findById(key);
                        if (null == fetched) {
                            /*
                             * In this situation, it common workflow to do data loading.
                             */
                            reference = jooq.insert(entity);
                        } else {
                            /*
                             * In this situation, it means the old unique filters have been changed.
                             * Such as:
                             * From
                             * id,      code,      sigma
                             * 001,     AB.CODE,   5sLyA90qSo7
                             *
                             * To
                             * id,      code,      sigma
                             * 001,     AB.CODE1,  5sLyA90qSo7
                             *
                             * Above example could show that primary key has not been modified
                             */
                            reference = jooq.update(entity);
                        }
                    }
                } else {
                    /*
                     * code, sigma did not change and we could identify this record
                     * do update directly to modify old information.
                     */
                    reference = jooq.update(entity);
                }
            }
        }
        return reference;
    }

    @Override
    public ExcelClient exportTable(final String identifier, final JsonArray data, final Handler<AsyncResult<Buffer>> handler) {
        /* 1. Workbook created */
        final XSSFWorkbook workbook = new XSSFWorkbook();
        /* 2. Sheet created */
        final XSSFSheet sheet = workbook.createSheet(identifier);
        /*
         * 3. Row created
         * 3.1. First row created
         * 3.2. Other data rows created
         *
         * Basic Operation:
         * 1) Get the first row size ( labelRow )
         * 2) Get the second row size ( labelCell )
         * 3) Build header data on template
         * {TABLE} / identifier / xxxxxx
         * Generate the header row here
         * */
        final boolean headed = ExFn.generateHeader(sheet, identifier, data);
        /*
         * Data Part
         */
        final List<Integer> sizeList = new ArrayList<>();
        Ut.itJArray(data, JsonArray.class, (rowData, index) -> {
            /*
             * Adjust 1 for generateHeader
             */
            final Integer actualIdx = headed ? (index + 1) : index;
            ExFn.generateData(sheet, actualIdx, rowData);
            sizeList.add(rowData.size());
        });

        /*
         * Tpl extraction for exporting
         */
        this.helper.brush(workbook, sheet);

        /*
         * 4. Adjust column width
         *  Here are some situation that the font-size may be changed in Tpl
         * */
        ExFn.generateAdjust(sheet, sizeList);

        /* 5. OutputStream */
        Fn.safeJvm(() -> {
            /*
             * This file object refer to created temp file and output to buffer
             */
            final String filename = identifier + "." + UUID.randomUUID() + ".xlsx";
            final OutputStream out = new FileOutputStream(filename);
            workbook.write(out);
            // InputStream converted
            handler.handle(Ux.future(Ut.ioBuffer(filename)).compose(buffer -> {
                /*
                 * ioDelete should happened after data got, in this kind of situation
                 * there is no additional data generated here.
                 */
                // Ut.ioRm(filename);
                return Future.succeededFuture(buffer);
            }));
        });
        return this;
    }

    @Override
    public Future<Buffer> exportTable(final String identifier, final JsonArray data, final Shape type) {
        return null;
    }

    @Override
    public Future<Buffer> exportTable(final String identifier, final JsonArray data) {
        final Promise<Buffer> future = Promise.promise();
        this.exportTable(identifier, data, handler -> {
            if (handler.succeeded()) {
                future.complete(handler.result());
            } else {
                final Throwable error = handler.cause();
                if (Objects.nonNull(error)) {
                    final WebException failure = new _500ExportingErrorException(this.getClass(), error.getMessage());
                    future.fail(failure);
                } else {
                    future.fail(new _500InternalServerException(this.getClass(), "Unexpected Error"));
                }
            }
        });
        return future.future();
    }

    private Set<ExTable> getFiltered(final Set<ExTable> processed, final String tableOnly) {
        return processed.stream()
                .filter(table -> tableOnly.equals(table.getName()))
                .collect(Collectors.toSet());
    }

    private List<JsonObject> extract(final ExTable table) {
        /* Records extracting */
        final List<ExRecord> records = table.get();
        LOGGER.info("[ Έξοδος ] Table: {0}, Data Size: {1}", table.getName(), records.size());
        /* Pojo Processing */
        return records.stream().filter(Objects::nonNull)
                .map(ExRecord::toJson)
                .collect(Collectors.toList());
    }
}
