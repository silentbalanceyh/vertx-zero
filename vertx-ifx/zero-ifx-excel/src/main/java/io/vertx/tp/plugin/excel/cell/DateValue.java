package io.vertx.tp.plugin.excel.cell;

import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

class DateValue {

    static Object toNumeric(final Cell cell) {
        if (CellType.NUMERIC == cell.getCellType()) {
            if (DateUtil.isCellDateFormatted(cell)) {
                final Date date = DateUtil.getJavaDate(cell.getNumericCellValue(), TimeZone.getDefault());
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
                return cell.getNumericCellValue();
            }
        } else return null;
    }
}
