package io.vertx.tp.plugin.excel.ranger;

import io.vertx.tp.plugin.excel.atom.ExRecord;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.tp.plugin.excel.cell.ExValue;
import io.vertx.tp.plugin.excel.tool.ExFn;
import io.vertx.up.atom.Refer;
import io.vertx.up.commune.element.Shape;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class PureIn extends AbstractExIn {

    public PureIn(final Sheet sheet) {
        super(sheet);
    }

    @Override
    public ExBound applyTable(final ExTable table, final Row row, final Cell cell, final Integer limitation) {
        /* Scan Field, Once scanning */
        final Refer hod = new Refer();
        ExFn.onRow(this.sheet, row.getRowNum() + 2, foundRow -> {
            /* Build Field Col */
            final ExBound bound = new ColBound(cell.getColumnIndex(), foundRow.getLastCellNum());
            /* Table Data Calculated */
            ExFn.itRow(foundRow, bound, (foundCell, colIndex) -> table.add(foundCell.getStringCellValue()));
            /* Build Value Row Range */
            hod.add(new RowBound(foundRow.getRowNum() + 1, limitation));
        });
        return hod.get();
    }

    @Override
    public ExTable applyData(final ExTable table, final ExBound dataRange, final Cell cell, final Shape shape) {
        /* Data Range */
        ExFn.itSheet(this.sheet, dataRange, (dataRow, dataIndex) -> {
            /* Build Data Col Range */
            final ExBound bound = new ColBound(cell.getColumnIndex(),
                    cell.getColumnIndex() + table.size());
            /* Each row should be record */
            final ExRecord record = this.create(dataRow, bound, table);
            /* Not Empty to add */
            table.add(record);
        });
        return table;
    }

    private ExRecord create(final Row row, final ExBound bound, final ExTable table) {
        final ExRecord record = new ExRecord();
        ExFn.itRow(row, bound, (dataCell, dataIndex) -> {
            /* Field / Value */
            final String field = table.field(dataIndex);
            try {
                final Object value = ExValue.getValue(dataCell, this.evaluator);
                /* Stored into record */
                record.put(field, value);
            } catch (final Throwable ex) {
                this.logger().info("Error occurs: {0}, Table Name: {1}", ex.getMessage(), table.getName());
                ex.printStackTrace();
            }
        });
        return record;
    }
}
