package io.vertx.tp.plugin.excel.atom;

import io.vertx.up.log.Annal;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class ExPos {
    private static final Annal LOGGER = Annal.get(ExPos.class);
    private final transient int rowIndex;
    private final transient int colIndex;

    private ExPos(final int rowIndex, final int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public static ExPos index(final int rowIndex, final int colIndex) {
        return new ExPos(rowIndex, colIndex);
    }

    public static ExPos index(final int colIndex) {
        return new ExPos(0, colIndex);
    }

    public int rowIndex() {
        return this.rowIndex;
    }

    public int colIndex() {
        return this.colIndex;
    }

    public CellRangeAddress region(final int rows, final int cols) {
        /*
         * Build new region based on current position
         */
        final int rowStart = this.rowIndex;
        final int colStart = this.colIndex;
        /*
         * Adjust for end
         */
        int rowEnd = this.rowIndex + rows;
        int colEnd = this.colIndex + cols;
        if (rowStart < rowEnd) {
            rowEnd--;
        }
        if (colStart < colEnd) {
            colEnd--;
        }
        /*
         * valid for region / Merged region A302 must contain 2 or more cells
         *
         */
        final int rowAcc = (rowEnd - rowStart);
        final int colAcc = (colEnd - colStart);
        if (0 < rowAcc || 0 < colAcc) {
            LOGGER.debug("[ Έξοδος ] Region created: ( Row: {0} ~ {1}, Column: {2} ~ {3} )",
                    rowStart, rowEnd, colStart, colEnd);
            return new CellRangeAddress(rowStart, rowEnd, colStart, colEnd);
        } else return null;
    }
}
