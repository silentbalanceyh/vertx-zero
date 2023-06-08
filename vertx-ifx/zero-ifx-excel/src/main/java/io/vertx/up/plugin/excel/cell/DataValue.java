package io.vertx.up.plugin.excel.cell;

import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

class DataValue {

    static Object toBoolean(final Cell cell) {
        if (CellType.BOOLEAN == cell.getCellType()) {
            return cell.getBooleanCellValue();
        } else {
            final String literal = cell.getStringCellValue();
            if (Ut.isNil(literal)) {
                return Boolean.FALSE;
            } else {
                return Boolean.valueOf(literal);
            }
        }
    }

    static Object toString(final Cell cell) {
        if (CellType.NUMERIC == cell.getCellType()) {
            /*
             * Fix issue of user operation:
             * Cannot get a STRING value from a NUMERIC cell
             */
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return cell.getStringCellValue();
        }
    }

    static Object toNumeric(final Cell cell) {
        if (CellType.NUMERIC == cell.getCellType()) {
            if (DateUtil.isCellDateFormatted(cell)) {
                final double cellValue = cell.getNumericCellValue();
                if (DateUtil.isValidExcelDate(cellValue)) {
                    final Date date = DateUtil.getJavaDate(cellValue, TimeZone.getDefault());
                    /*
                     * For 1899-12-30
                     */
                    final LocalDateTime dateTime = Ut.toDateTime(date);
                    if (dateTime.getYear() < 1900) {
                        /*
                         * Calculation has been put in `toTime`
                         */
                        final LocalTime time = Ut.toTime(date);
                        return time.format(DateTimeFormatter.ISO_LOCAL_TIME);
                    } else {
                        return date.toInstant();
                    }
                } else {
                    return null;
                }
            } else {
                return cell.getNumericCellValue();
            }
        } else {
            return null;
        }
    }
}
