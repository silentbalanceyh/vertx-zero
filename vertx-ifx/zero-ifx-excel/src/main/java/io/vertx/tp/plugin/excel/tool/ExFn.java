package io.vertx.tp.plugin.excel.tool;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.plugin.excel.ranger.ExBound;
import io.vertx.up.commune.element.TypeAtom;
import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/*
 * Function for iterator
 */
public class ExFn {
    /*
     * Iterator of rows with RowBound
     * Each row as `Unit`
     */
    public static void itSheet(final Sheet sheet,
                               final ExBound bound,
                               final BiConsumer<Row, Integer> consumer) {
        /* Start / End */
        final int start = bound.getStart();
        final int end = bound.getEnd();
        for (int idx = start; idx <= end; idx++) {
            final Row row = sheet.getRow(idx);
            if (null != row) {
                // Predicate existing
                consumer.accept(row, idx);
            }
        }
    }

    public static void itSheet(final Sheet sheet,
                               final ExBound bound,
                               final Set<Integer> indexSet,
                               final Consumer<List<Row>> consumer) {
        /* Start / End */
        final int start = bound.getStart();
        final int end = bound.getEnd();
        /* Matrix -> List */
        final List<List<Row>> matrix = new ArrayList<>();
        final List<Row> parameters = new ArrayList<>();
        for (int idx = start; idx <= end; idx++) {
            final Row row = sheet.getRow(idx);
            /*
             * Check each cell values, each cell must be BLANK that are in indexSet
             * If all `indexSet` is BLANK, it means that current row is not new row
             * For new row, at least there should be only one value existing
             */
            final boolean isPrevRow = indexSet.stream().map(row::getCell)
                    .allMatch(cell -> {
                        /*
                         * Here are STRING / BLANK etc.
                         */
                        if (CellType.BLANK == cell.getCellType()) {
                            /*
                             * BLANK
                             */
                            return true;
                        } else if (CellType.STRING == cell.getCellType()) {
                            /*
                             * STRING, the value should be ""
                             */
                            return Ut.isNil(cell.getStringCellValue());
                        } else return false;
                    });
            if (!isPrevRow) {
                /*
                 * Other times for processing.
                 */
                if (!parameters.isEmpty()) {
                    matrix.add(new ArrayList<>(parameters));
                    parameters.clear();
                }
            }
            parameters.add(row);
        }
        if (!parameters.isEmpty()) {
            /*
             * The left data matrix for last one
             */
            matrix.add(new ArrayList<>(parameters));
            parameters.clear();
        }
        matrix.forEach(consumer);
    }

    /*
     * Used out side
     */
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

    /*
     * No filter provided
     */
    public static void itRow(final Row row,
                             final ExBound bound,
                             final BiConsumer<Cell, Integer> consumer) {
        itRow(row, bound, consumer, null);
    }

    /*
     * For complex template, two row iteration
     */
    public static void itRowZip(final Row row, final Row row1,
                                final ExBound bound,
                                final BiConsumer<Cell, Cell> consumer) {
        final int start = bound.getStart();
        final int end = bound.getEnd();
        for (int idx = start; idx < end; idx++) {
            final Cell cell = row.getCell(idx);
            final Cell cell1 = row1.getCell(idx);
            if (null != cell) {
                // Predicate existing
                consumer.accept(cell, cell1);
            }
        }
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

    public static void onRow(final Sheet sheet,
                             final int rowIndex, final int rowIndex1,
                             final BiConsumer<Row, Row> consumer) {
        onRow(sheet, rowIndex, row ->
                onRow(sheet, rowIndex1,
                        row1 -> consumer.accept(row, row1)), null);
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

    public static void generateAdjust(final Sheet sheet, final List<Integer> sizeList) {
        ExData.generateAdjust(sheet, sizeList);
    }

    public static boolean generateHeader(final Sheet sheet, final String identifier,
                                         final JsonArray tableData, final TypeAtom TypeAtom) {
        return ExData.generateHeader(sheet, identifier, tableData, TypeAtom);
    }

    public static void generateHeader(final Sheet sheet, final Integer index,
                                      final JsonArray rowData) {
        ExData.generateData(sheet, index, rowData, new ArrayList<>());
    }

    public static void generateData(final Sheet sheet, final Integer index,
                                    final JsonArray rowData, final List<Class<?>> typeArray) {
        ExData.generateData(sheet, index, rowData, typeArray);
    }
}
