package io.vertx.tp.plugin.excel.tool;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.excel.atom.ExKey;
import io.vertx.tp.plugin.excel.atom.ExPos;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.aeon.experiment.mixture.HTAtom;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

class ExData {

    static void generateAdjust(final Sheet sheet, final List<Integer> sizeList) {
        // Adjust column width first
        final IntSummaryStatistics statistics =
            sizeList.stream().mapToInt(Integer::intValue).summaryStatistics();
        final int max = statistics.getMax();
        for (int idx = 0; idx < max; idx++) {
            sheet.autoSizeColumn(idx, true);
        }
        // Bordered for each cell
    }

    static void generateData(final Sheet sheet, final Integer index,
                             final JsonArray rowData, final List<Class<?>> types) {
        /* Row creation */
        final Row row = sheet.createRow(index);
        /* Data Filling */
        final int size = rowData.size();
        for (int colIdx = Values.IDX; colIdx < size; colIdx++) {
            /* Processing for ExPos */
            final ExPos pos = ExPos.index(index, colIdx);
            /*
             * Type processing
             */
            final Class<?> type;
            if (colIdx < types.size()) {
                type = types.get(colIdx);
            } else {
                type = null;
            }
            /* createCell processed */
            createCell(sheet, row, pos, rowData.getValue(colIdx), type);
        }
    }

    static boolean generateHeader(final Sheet sheet, final String identifier,
                                  final JsonArray tableData, final HTAtom HTAtom) {
        final Consumer<Integer> consumer = width -> {
            /*
             * Row creation
             * The header must be the first row
             */
            final Row row = sheet.createRow(Values.IDX);
            /*
             * First cell
             */
            final ExPos pos = ExPos.index(2);
            createCell(sheet, row, ExPos.index(0), ExKey.EXPR_TABLE);
            createCell(sheet, row, ExPos.index(1), identifier);
            createCell(sheet, row, pos, null);
            /*
             * Region for cell
             * The four parameters are all index part, it means that you should
             * 0,0: means first row
             * 2,width - 1: From the third column here
             */
            final CellRangeAddress region = pos.region(0, width - 1);
            if (Objects.nonNull(region)) {
                /*
                 * Call,  valid for region / Merged region A302 must contain 2 or more cells
                 */
                Fn.safeJvm(() -> sheet.addMergedRegion(region));
            }
        };
        if (HTAtom.isComplex()) {
            /*
             * Complex workflow processing
             */
            if (Values.FOUR <= tableData.size()) {
                /*
                 * 1, 3 processing
                 */
                final JsonArray labelHeader = Ut.valueJArray(tableData.getJsonArray(Values.ONE));
                final JsonArray fieldHeader = Ut.valueJArray(tableData.getJsonArray(Values.THREE));
                consumer.accept(Math.max(labelHeader.size(), fieldHeader.size()));
                return true;
            } else {
                return false; // Header generation failure
            }
        } else {
            if (Values.TWO <= tableData.size()) {
                /*
                 * 0, 1 processing
                 */
                final JsonArray labelHeader = Ut.valueJArray(tableData.getJsonArray(Values.IDX));
                final JsonArray fieldHeader = Ut.valueJArray(tableData.getJsonArray(Values.ONE));
                consumer.accept(Math.max(labelHeader.size(), fieldHeader.size()));
                return true;
            } else {
                return false; // Header generation failure
            }
        }
    }

    private static void createCell(final Sheet sheet, final Row row,
                                   final ExPos pos, final Object value) {
        createCell(sheet, row, pos, value, null);
    }

    private static void createCell(final Sheet sheet, final Row row,
                                   final ExPos pos, final Object value, final Class<?> type) {
        if (value instanceof JsonObject) {
            /*
             * Json Format
             * {
             *      "cols": "xx",
             *      "rows": "xxx",
             *      "value": ""
             * }
             */
            final JsonObject define = (JsonObject) value;
            final Object input = define.getValue("value");
            /*
             * Processing `cols`, `rows`
             */
            createCell(row, pos.colIndex(), input, type);
            /*
             * CellRangeAddress
             */
            final int cols = define.getInteger("cols");
            final int rows = define.getInteger("rows");

            final CellRangeAddress region = pos.region(rows, cols);
            if (Objects.nonNull(region)) {
                /*
                 * Call,  valid for region / Merged region A302 must contain 2 or more cells
                 */
                Fn.safeJvm(() -> sheet.addMergedRegion(region));
            }
        } else {
            /*
             * null consumer processing
             */
            createCell(row, pos.colIndex(), value, type);
        }
    }

    private static void createCell(final Row row, final Integer index, final Object value, final Class<?> typeDef) {
        if (Objects.isNull(value)) {
            /* Cell create */
            final Cell cell = row.createCell(index);
            /* null filling */
            cell.setCellValue(Strings.EMPTY);
        } else {
            /* Type analyzed */
            final Class<?> type;
            if (Objects.isNull(typeDef)) {
                type = value.getClass();
            } else {
                type = typeDef;
            }

            /* Cell type */
            final CellType cellType = ExOut.type(type);
            /* Cell create */
            final Cell cell = row.createCell(index, cellType);
            /* All type should be OK for set String */
            ExOut.value(cell, type, value);
            /* Datetime cell format */
        }
    }
}
