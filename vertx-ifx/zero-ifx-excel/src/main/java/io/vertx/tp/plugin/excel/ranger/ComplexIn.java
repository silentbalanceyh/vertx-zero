package io.vertx.tp.plugin.excel.ranger;

import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.tp.plugin.excel.tool.ExFn;
import io.vertx.up.atom.Refer;
import io.vertx.up.commune.element.Shape;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class ComplexIn extends AbstractExIn {
    public ComplexIn(final Sheet sheet) {
        super(sheet);
    }

    @Override
    public ExBound applyTable(final ExTable table, final Row row, final Cell cell, final Integer limitation) {
        /* Scan Field, Once scanning */
        final Refer hod = new Refer();
        ExFn.onRow(this.sheet, row.getRowNum() + 3, row.getRowNum() + 4, (found, foundRow) -> {
            /* Build Field Col */
            final ExBound bound = new ColBound(cell.getColumnIndex(), found.getLastCellNum());
            ExFn.itRowZip(found, foundRow, bound, (first, second) -> {
                /* Parent / Child */
                final String parent = first.getStringCellValue();
                final String child = second.getStringCellValue();
                System.err.println(parent + "," + child);
            });
            hod.add(new RowBound(foundRow.getRowNum() + 1, limitation));
        });
        return hod.get();
    }

    @Override
    public ExTable applyData(final ExTable table, final ExBound dataRange, final Cell cell, final Shape shape) {
        return null;
    }
}
