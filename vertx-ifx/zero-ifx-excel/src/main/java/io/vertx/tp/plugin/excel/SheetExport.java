package io.vertx.tp.plugin.excel;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._500ExportingErrorException;
import io.vertx.tp.plugin.excel.tool.ExFn;
import io.vertx.up.commune.element.Shape;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * The package scope for exporting data based on Excel Client
 * This component is for data exporting only
 */
class SheetExport {
    private transient final ExcelHelper helper;

    private SheetExport(final ExcelHelper helper) {
        this.helper = helper;
    }

    static SheetExport create(final ExcelHelper helper) {
        return new SheetExport(helper);
    }

    void exportData(final String identifier, final JsonArray data,
                    final Shape shape, final Handler<AsyncResult<Buffer>> handler) {
        /*
         * 1. Workbook created first
         * Each time if you want to export the data to excel file, here you must create
         * new workbook
         */
        final XSSFWorkbook workbook = new XSSFWorkbook();

        /*
         * 2. Create the sheet of workbook
         * In current version, we only select 2007 format instead of other version such as 2003
         * The sheet name is `identifier` ( Table Name )
         */
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
         *
         * 3.3. Different workflow to generate header based on Shape
         * 1) When shape is null, the data type of each cell is detected by data literal
         * 2) When shape contains value, the data type of each cell is defined by Shape
         * */
        final boolean headed = ExFn.generateHeader(sheet, identifier, data, shape);

        /*
         * 4. Data Part of current excel file
         */
        final List<Integer> sizeList = new ArrayList<>();
        /*
         * Type processing
         */
        final List<Class<?>> types = this.types(data, shape);
        Ut.itJArray(data, JsonArray.class, (rowData, index) -> {
            /*
             * Adjust 1 for generatedHeader
             * 1) Header adjust to pickup data region
             * 2) index map calculation
             */
            final Integer actualIdx = headed ? (index + 1) : index;
            /*
             * Generate banner of title for usage
             */
            if (shape.isComplex()) {
                /*
                 * 1,2,3,4
                 */
                if (actualIdx <= 4) {
                    /*
                     * 1,2, Cn Header
                     * 3,4, En Header
                     */
                    ExFn.generateHeader(sheet, actualIdx, rowData);
                } else {
                    /*
                     * Data Part
                     */
                    ExFn.generateData(sheet, actualIdx, rowData, types);
                }
            } else {
                if (actualIdx <= 2) {
                    /*
                     * 1, Cn Header
                     * 2, En Header
                     */
                    ExFn.generateHeader(sheet, actualIdx, rowData);
                } else {
                    /*
                     * Data Part
                     */
                    ExFn.generateData(sheet, actualIdx, rowData, types);
                }
            }

            sizeList.add(rowData.size());
        });

        /*
         * 5. Apply for style based on Tpl extraction
         */
        this.helper.brush(workbook, sheet, shape);

        /*
         * 6. Adjust column width
         * - Here are some situation that the font-size may be changed in Tpl
         * This workflow must be after
         *
         * ```java
         * this.helper.brush(workbook, sheet);
         * ```
         */
        ExFn.generateAdjust(sheet, sizeList);

        /*
         * 7. OutputStream
         */
        Fn.safeJvm(() -> {
            /*
             * This file object refer to created temp file and output to buffer
             */
            final String filename = identifier + Strings.DOT + UUID.randomUUID() +
                    Strings.DOT + FileSuffix.EXCEL_2007;
            final OutputStream out = new FileOutputStream(filename);
            workbook.write(out);
            /*
             * Result handle
             */
            handler.handle(Future.succeededFuture(Ut.ioBuffer(filename)));
        });
    }

    Future<Buffer> exportData(final String identifier, final JsonArray data,
                              final Shape shape) {
        final Promise<Buffer> promise = Promise.promise();
        this.exportData(identifier, data, shape, this.callback(promise));
        return promise.future();
    }

    /*
     * Build data type here
     * 1) Simple
     * (index = 2) = 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15...
     * 2) Complex
     * (index = 3) = 0,1,2,3,4,5 = JsonArray,n,n,8,9,10 = JsonArray,n,n,n,14,15,...
     * (index = 4) = n,n,n,n,n,5,6,7,n,n,10,11,12,13,14,15,...
     *
     * Flatted for complex type here
     */
    private List<Class<?>> types(final JsonArray data, final Shape shape) {
        final List<Class<?>> typeArray = new ArrayList<>();
        if (Objects.nonNull(shape) && shape.isComplex()) {
            // index = 2
            final JsonArray fields = data.getJsonArray(Values.TWO);
            // index = 3
            final JsonArray secondary = data.getJsonArray(Values.THREE);

            if (Ut.notNil(fields) && Ut.notNil(secondary)) {
                // parent processing
                final ConcurrentMap<Integer, String> parentMap = new ConcurrentHashMap<>();

                /*
                 * Iterator on first
                 * Calculate region for `null`
                 */
                String found = null;
                final int length = fields.size();
                for (int idx = 0; idx < length; idx++) {
                    final Object item = fields.getValue(idx);
                    final String field = this.typeField(item);
                    if (Ut.notNil(field)) {
                        final Class<?> type = shape.type(field);
                        if (JsonArray.class != type) {
                            typeArray.add(type);
                        } else {
                            // Start index
                            found = field;
                            parentMap.put(idx, found);
                        }
                    } else {
                        // End index
                        parentMap.put(idx, found);
                    }
                }
                /*
                 * Secondary must be children to extract
                 */
                final int lengthChild = secondary.size();
                for (int idx = 0; idx < lengthChild; idx++) {
                    final Object item = secondary.getValue(idx);
                    final String field = this.typeField(item);
                    final String parent = parentMap.getOrDefault(idx, null);
                    if (Ut.notNil(field) && Ut.notNil(parent)) {
                        /*
                         * Find type based on parent/child both
                         */
                        typeArray.add(shape.type(parent, field));
                    }
                }
            }
        } else {
            // index = 2
            final JsonArray fields = data.getJsonArray(Values.ONE);
            if (Ut.notNil(fields)) {
                fields.forEach(item -> {
                    final String field = this.typeField(item);
                    typeArray.add(shape.type(field));
                });
            }
        }
        return typeArray;
    }

    private String typeField(final Object input) {
        if (Objects.isNull(input)) {
            return null;
        } else {
            if (input instanceof JsonObject) {
                return ((JsonObject) input).getString("value");
            } else {
                return (String) input;
            }
        }
    }

    private Handler<AsyncResult<Buffer>> callback(final Promise<Buffer> promise) {
        return handler -> {
            if (handler.succeeded()) {
                promise.complete(handler.result());
            } else {
                final Throwable error = handler.cause();
                if (Objects.nonNull(error)) {
                    final WebException failure = new _500ExportingErrorException(this.getClass(), error.getMessage());
                    promise.fail(failure);
                } else {
                    promise.fail(new _500InternalServerException(this.getClass(),
                            "Unexpected Error when Exporting"));
                }
            }
        };
    }
}
