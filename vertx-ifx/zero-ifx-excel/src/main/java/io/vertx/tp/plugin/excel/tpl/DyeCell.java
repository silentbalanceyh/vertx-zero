package io.vertx.tp.plugin.excel.tpl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class DyeCell implements Serializable {
    private final transient XSSFFont font;
    private final transient XSSFCellStyle style;

    private DyeCell(final Workbook workbook) {
        this.style = (XSSFCellStyle) workbook.createCellStyle();
        this.font = (XSSFFont) workbook.createFont();
    }

    public static DyeCell create(final Workbook workbook) {
        return new DyeCell(workbook);
    }

    /*
     * Align for text
     */
    public DyeCell align(final HorizontalAlignment align, final VerticalAlignment valign) {
        if (Objects.nonNull(align)) {
            this.style.setAlignment(align);
        }
        this.style.setVerticalAlignment(valign);
        return this;
    }

    public DyeCell align(final HorizontalAlignment align) {
        this.align(align, VerticalAlignment.TOP);
        return this;
    }

    /*
     * Color
     */

    public DyeCell color(final String fore, final String bg) {
        final XSSFColor foreColor = this.build(fore);
        final XSSFColor bgColor = this.build(bg);
        return this.color(foreColor, bgColor);
    }

    private DyeCell color(final XSSFColor fore, final XSSFColor bg) {
        /*
         * Here are some difference between old version and new version
         * The fore color should be font color
         * The bg color is the font background color
         */
        this.font.setColor(fore);
        // this.style.setFillForegroundColor(fore);
        this.style.setFillForegroundColor(bg);
        this.style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return this;
    }

    private XSSFColor build(final String color) {
        final int r = Integer.parseInt((color.substring(0, 2)), 16);
        final int g = Integer.parseInt((color.substring(2, 4)), 16);
        final int b = Integer.parseInt((color.substring(4, 6)), 16);
        // awt
        final java.awt.Color awtColor = new java.awt.Color(r, g, b);
        return new XSSFColor(awtColor, null);
    }

    /*
     * Border
     */
    public DyeCell border(final BorderStyle... borders) {
        return this.border(null, borders);
    }

    public DyeCell border(final Short color, final BorderStyle... borders) {
        // CSS Calculation
        if (0 < borders.length) {
            if (1 == borders.length) {
                // length = 1, all border
                this.style.setBorderTop(borders[0]);
                this.style.setBorderRight(borders[0]);
                this.style.setBorderBottom(borders[0]);
                this.style.setBorderLeft(borders[0]);
            } else if (2 == borders.length) {
                // length = 2, top + bottom, left + right
                this.style.setBorderTop(borders[0]);
                this.style.setBorderRight(borders[1]);
                this.style.setBorderBottom(borders[0]);
                this.style.setBorderLeft(borders[1]);
            } else if (3 == borders.length) {
                // length = 3, top, left + right, bottom
                this.style.setBorderTop(borders[0]);
                this.style.setBorderRight(borders[1]);
                this.style.setBorderBottom(borders[2]);
                this.style.setBorderLeft(borders[1]);
            } else if (4 == borders.length) {
                // length = 4, top, right, bottom, left
                this.style.setBorderTop(borders[0]);
                this.style.setBorderRight(borders[1]);
                this.style.setBorderBottom(borders[2]);
                this.style.setBorderLeft(borders[3]);
            }
        }
        // Color
        if (Objects.nonNull(color)) {
            this.style.setTopBorderColor(color);
            this.style.setRightBorderColor(color);
            this.style.setBottomBorderColor(color);
            this.style.setLeftBorderColor(color);
        }
        return this;
    }

    /*
     * Wrap Text
     */
    public DyeCell wrap() {
        this.style.setWrapText(true);
        return this;
    }

    public DyeCell unwrap() {
        this.style.setWrapText(false);
        return this;
    }

    /*
     * Font
     */
    public DyeCell font(final int fontSize, final boolean bold) {
        return this.font(fontSize, bold, null, null);
    }

    public DyeCell font(final int fontSize, final boolean bold,
                        final short color) {
        return this.font(fontSize, bold, color, null);
    }

    public DyeCell font(final int fontSize, final boolean bold,
                        final Short color, final String name) {
        this.font.setFontHeightInPoints((short) fontSize);
        if (Objects.nonNull(color)) {
            this.font.setColor(color);
        }
        this.font.setBold(bold);
        if (Objects.nonNull(name)) {
            this.font.setFontName(name);
        }
        return this;
    }

    public CellStyle build() {
        this.style.setFont(this.font);
        /*
         * Process Cache Pool here for max style applying
         */
        return this.style;
    }
}
