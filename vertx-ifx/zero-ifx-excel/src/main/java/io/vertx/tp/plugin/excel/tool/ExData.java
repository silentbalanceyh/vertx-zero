package io.vertx.tp.plugin.excel.tool;

import io.vertx.core.json.JsonArray;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Objects;

class ExData {

    static void generateData(final Sheet sheet, final Integer index, final JsonArray rowData) {
        /* Row creation */
        final Row row = sheet.createRow(index);
        /* Data Filling */
        final int size = rowData.size();
        for (int idx = Values.IDX; idx < size; idx++) {
            createCell(row, idx, rowData.getValue(idx));
        }
    }

    private static void createCell(final Row row, final Integer index, final Object value) {
        /* Cell create */
        final Cell cell = row.createCell(index);
        /* Data Object */
        if (Objects.isNull(value)) {
            /* null filling */
            cell.setCellValue(Strings.EMPTY);
        } else {
            if (String.class == value.getClass()) {
                /* String Type */
                cell.setCellValue(value.toString());
            }
        }
    }
}
