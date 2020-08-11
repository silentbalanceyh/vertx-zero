package io.vertx.tp.plugin.excel.tool;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.plugin.excel.atom.ExKey;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class ExData {

    private static final ConcurrentMap<Class<?>, CellType> TYPE_MAP = new ConcurrentHashMap<Class<?>, CellType>() {
        {
            this.put(String.class, CellType.STRING);
            this.put(char.class, CellType.STRING);
            this.put(Instant.class, CellType.NUMERIC);
            this.put(LocalDate.class, CellType.NUMERIC);
            this.put(LocalDateTime.class, CellType.NUMERIC);
            this.put(BigDecimal.class, CellType.NUMERIC);
            this.put(Integer.class, CellType.NUMERIC);
            this.put(int.class, CellType.NUMERIC);
            this.put(Long.class, CellType.NUMERIC);
            this.put(long.class, CellType.NUMERIC);
            this.put(Short.class, CellType.NUMERIC);
            this.put(short.class, CellType.NUMERIC);
            this.put(boolean.class, CellType.BOOLEAN);
            this.put(Boolean.class, CellType.BOOLEAN);
        }
    };

    static void generateAdjust(final Sheet sheet, final List<Integer> sizeList) {
        // Adjust column width first
        final IntSummaryStatistics statistics =
                sizeList.stream().mapToInt(Integer::intValue).summaryStatistics();
        final int max = statistics.getMax();
        for (int idx = 0; idx < max; idx++) {
            sheet.autoSizeColumn(idx);
        }
        // Bordered for each cell
    }

    static void generateData(final Sheet sheet, final Integer index, final JsonArray rowData) {
        /* Row creation */
        final Row row = sheet.createRow(index);
        /* Data Filling */
        final int size = rowData.size();
        for (int idx = Values.IDX; idx < size; idx++) {
            createCell(row, idx, rowData.getValue(idx));
        }
    }

    static boolean generateHeader(final Sheet sheet, final String identifier, final JsonArray tableData) {
        if (Values.TWO <= tableData.size()) {
            final JsonArray labelHeader = Ut.sureJArray(tableData.getJsonArray(Values.IDX));
            final JsonArray fieldHeader = Ut.sureJArray(tableData.getJsonArray(Values.ONE));
            final int width = Math.max(labelHeader.size(), fieldHeader.size());
            /*
             * Row creation
             * The header must be the first row
             */
            final Row row = sheet.createRow(Values.IDX);
            /*
             * First cell
             */
            createCell(row, 0, ExKey.EXPR_TABLE);
            createCell(row, 1, identifier);
            createCell(row, 2, null);
            /*
             * Region for cell
             * The four parameters are all index part, it means that you should
             * 0,0: means first row
             * 2,width - 1: From the third column here
             */
            final CellRangeAddress region =
                    new CellRangeAddress(0, 0, 2, width - 1);
            sheet.addMergedRegion(region);
            return true;
        } else {
            /*
             * Header generation failure
             */
            return false;
        }
    }

    private static void createCell(final Row row, final Integer index, final Object value) {

        /* Data Object */
        if (Objects.isNull(value)) {
            /* Cell create */
            final Cell cell = row.createCell(index);
            /* null filling */
            cell.setCellValue(Strings.EMPTY);
        } else {
            /* Cell Type */
            final Class<?> type = value.getClass();
            final CellType cellType = TYPE_MAP.getOrDefault(type, CellType.STRING);
            /* Cell create */
            final Cell cell = row.createCell(index, cellType);
            /* All type should be OK */
            cell.setCellValue(value.toString());
        }
    }
}
