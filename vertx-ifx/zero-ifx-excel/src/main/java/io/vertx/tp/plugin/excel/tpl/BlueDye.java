package io.vertx.tp.plugin.excel.tpl;

import io.vertx.up.fn.Fn;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class BlueDye {
    private static final ConcurrentMap<Integer, BlueDye> PICKER_POOL =
            new ConcurrentHashMap<>();
    private final transient Workbook workbook;

    private BlueDye(final Workbook workbook) {
        this.workbook = workbook;
    }

    static BlueDye get(final Workbook workbook) {
        return Fn.pool(PICKER_POOL, workbook.hashCode(), () -> new BlueDye(workbook));
    }

    /*
     * {TABLE} cell
     */
    void onTable(final Cell cell) {
        cell.setCellStyle(DyeCell.create(this.workbook)
                .color("FFFFFF", "3EB7FF")
                .align(HorizontalAlignment.CENTER)
                .border(BorderStyle.THIN)
                .font(13, false)
                .build()
        );
    }

    void onModel(final Cell cell) {
        cell.setCellStyle(DyeCell.create(this.workbook)
                .color("FFFFFF", "696969")
                .align(HorizontalAlignment.CENTER)
                .border(BorderStyle.THIN)
                .font(13, false)
                .build()
        );
    }

    void onEmpty(final Cell cell) {
        cell.setCellStyle(DyeCell.create(this.workbook)
                .align(HorizontalAlignment.CENTER)
                .border(BorderStyle.THIN)
                .build());
    }

    void onCnHeader(final Cell cell) {
        this.onTable(cell);
    }

    void onEnHeader(final Cell cell) {
        cell.setCellStyle(DyeCell.create(this.workbook)
                .color("000000", "FFEC8B")
                .align(HorizontalAlignment.CENTER)
                .border(BorderStyle.THIN)
                .font(13, false)
                .build());
    }

    void onData(final Cell cell) {
        cell.setCellStyle(DyeCell.create(this.workbook)
                .border(BorderStyle.THIN)
                .font(13, false)
                .build());
    }
}
