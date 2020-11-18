package io.vertx.tp.plugin.excel.tpl;

import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class BlueDye {
    private static final ConcurrentMap<Integer, BlueDye> PICKER_POOL =
            new ConcurrentHashMap<>();
    /*
     * Fix Issue:
     * The maximum number of Cell Styles was exceeded. You can define up to 64000 style in a .xlsx Workbook
     */
    private final ConcurrentMap<String, DyeCell> stylePool =
            new ConcurrentHashMap<>();

    private final transient Workbook workbook;

    private BlueDye(final Workbook workbook) {
        this.workbook = workbook;
    }
    /*
     * Style applying to fix issue:
     */

    static BlueDye get(final Workbook workbook) {
        return Fn.pool(PICKER_POOL, workbook.hashCode(), () -> new BlueDye(workbook));
    }

    /*
     * {TABLE} cell
     */
    void onTable(final Cell cell) {
        final DyeCell dyeCell = Fn.pool(this.stylePool, "TABLE",
                () -> DyeCell.create(this.workbook)
                        .color("FFFFFF", "3EB7FF")
                        .align(HorizontalAlignment.CENTER)
                        .border(BorderStyle.THIN)
                        .font(13, false));
        cell.setCellStyle(dyeCell.build());
    }

    void onModel(final Cell cell) {
        final DyeCell dyeCell = Fn.pool(this.stylePool, "MODEL",
                () -> DyeCell.create(this.workbook)
                        .color("FFFFFF", "696969")
                        .align(HorizontalAlignment.CENTER)
                        .border(BorderStyle.THIN)
                        .font(13, false));
        cell.setCellStyle(dyeCell.build());
    }

    void onEmpty(final Cell cell) {
        final DyeCell dyeCell = Fn.pool(this.stylePool, "EMPTY",
                () -> DyeCell.create(this.workbook)
                        .align(HorizontalAlignment.CENTER)
                        .border(BorderStyle.THIN));
        cell.setCellStyle(dyeCell.build());
    }

    void onCnHeader(final Cell cell) {
        this.onTable(cell);
    }

    void onEnHeader(final Cell cell) {
        final DyeCell dyeCell = Fn.pool(this.stylePool, "HEADER",
                () -> DyeCell.create(this.workbook)
                        .color("000000", "FFEC8B")
                        .align(HorizontalAlignment.CENTER)
                        .border(BorderStyle.THIN)
                        .font(13, false));
        cell.setCellStyle(dyeCell.build());
    }

    /*
     * Here are simple rule for excel exporting
     * 1) When the cell is NUMERIC, we should determine whether the value is Number or Date
     * -- NUMBER, the style pool is DATA-VALUE, the same as other date type
     * -- Date, when time equal the min ( 00:00 ), use format of `yyyy-MM-dd`, otherwise: `yyyy-MM-dd HH:mm`
     *    The pool name is DATA-DATE & DATA-DATETIME
     * 2) Other date type should be pool of DATA-VALUE also.
     */
    void onData(final Cell cell, final Class<?> type) {
        final DyeCell dye;
        if (CellType.NUMERIC == cell.getCellType()) {
            /*
             * Buf for date exporting here
             */
            final double cellValue = cell.getNumericCellValue();
            if (Objects.nonNull(type)                                   // not null type
                    && Ut.isDate(type)                                  // type is date time definition
                    && DateUtil.isValidExcelDate(cellValue)             // the value is valid excel date
            ) {
                final Date value = DateUtil.getJavaDate(cellValue, TimeZone.getDefault());
                final LocalDateTime datetime = Ut.toDateTime(value);

                final LocalTime time = datetime.toLocalTime();
                if (LocalTime.MIN == time) {
                    /*
                     * LocalDate
                     */
                    dye = this.onDataValue("DATA-DATE").dateFormat(false);
                } else {
                    /*
                     * LocalDateTime
                     */
                    dye = this.onDataValue("DATA-DATETIME").dateFormat(true);
                }
            } else {
                /*
                 * Number
                 */
                dye = this.onDataValue("DATA-VALUE");
            }
        } else {
            /*
             * Other
             */
            dye = this.onDataValue("DATA-VALUE");
        }
        cell.setCellStyle(dye.build());
    }

    private DyeCell onDataValue(final String key) {
        return Fn.pool(this.stylePool, key,
                () -> DyeCell.create(this.workbook)
                        .border(BorderStyle.THIN)
                        .align(null, VerticalAlignment.TOP)
                        .font(13, false));
    }
}
