package io.vertx.tp.plugin.excel.tool;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.plugin.excel.ranger.ExBound;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/*
 * Function for iterator
 */
public class ExFn {
    /*
     * Iterator of rows with RowBound
     */
    public static void itSheet(final Sheet sheet,
                               final ExBound bound,
                               final BiConsumer<Row, Integer> consumer,
                               final Predicate<Row> predicate) {
        /* Start / End */
        final int start = bound.getStart();
        final int end = bound.getEnd();
        for (int idx = start; idx <= end; idx++) {
            final Row row = sheet.getRow(idx);
            if (null != row) {
                if (null == predicate) {
                    consumer.accept(row, idx);
                } else {
                    if (predicate.test(row)) {
                        // Predicate existing
                        consumer.accept(row, idx);
                    }
                }
            }
        }
    }

    public static void itSheet(final Sheet sheet,
                               final ExBound bound,
                               final BiConsumer<Row, Integer> consumer) {
        itSheet(sheet, bound, consumer, null);
    }

    public static void itRow(final Row row,
                             final ExBound bound,
                             final BiConsumer<Cell, Integer> consumer,
                             final Predicate<Cell> predicate) {
        final int start = bound.getStart();
        final int end = bound.getEnd();
        for (int idx = start; idx < end; idx++) {
            final Cell cell = row.getCell(idx);
            if (null != cell) {
                if (null == predicate) {
                    consumer.accept(cell, idx);
                } else {
                    final boolean tested = predicate.test(cell);
                    if (tested) {
                        // Predicate existing
                        consumer.accept(cell, idx);
                    }
                }
            }
        }
    }

    public static void itRow(final Row row,
                             final ExBound bound,
                             final BiConsumer<Cell, Integer> consumer) {
        itRow(row, bound, consumer, null);
    }


    public static void onRow(final Sheet sheet,
                             final int rowIndex,
                             final Consumer<Row> consumer,
                             final Predicate<Row> predicate) {
        final Row row = sheet.getRow(rowIndex);
        if (null != row) {
            if (null == predicate) {
                consumer.accept(row);
            } else {
                if (predicate.test(row)) {
                    // Predicate existing
                    consumer.accept(row);
                }
            }
        }
    }

    public static void onRow(final Sheet sheet,
                             final int rowIndex,
                             final Consumer<Row> consumer) {
        onRow(sheet, rowIndex, consumer, null);
    }

    public static void onCell(final Row row,
                              final int columnIndex,
                              final Consumer<Cell> consumer,
                              final Predicate<Cell> predicate) {
        final Cell cell = row.getCell(columnIndex);
        if (null != cell) {
            if (null == predicate) {
                consumer.accept(cell);
            } else {
                if (predicate.test(cell)) {
                    // Predicate existing
                    consumer.accept(cell);
                }
            }
        }
    }

    public static void onCell(final Row row,
                              final int columnIndex,
                              final Consumer<Cell> consumer) {
        onCell(row, columnIndex, consumer, null);
    }

    public static void generateStyle(final Sheet sheet, final JsonArray tableData, final List<Integer> sizeList) {

    }

    public static boolean generateHeader(final Sheet sheet, final String identifier, final JsonArray tableData) {
        return ExData.generateHeader(sheet, identifier, tableData);
    }

    public static void generateData(final Sheet sheet, final Integer index, final JsonArray rowData) {
        ExData.generateData(sheet, index, rowData);
    }
}
