package io.vertx.tp.plugin.excel.ranger;

import io.horizon.atom.modeler.TypeAtom;
import io.vertx.tp.plugin.excel.atom.ExRecord;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.tp.plugin.excel.tool.ExFn;
import io.vertx.up.atom.Refer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
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
    public ExTable applyData(final ExTable table, final ExBound dataRange, final Cell cell, final TypeAtom MetaAtom) {
        /* Data Range */
        ExFn.itSheet(this.sheet, dataRange, (dataRow, rowIndex) -> {
            /* Build Data Col Range */
            final ExBound bound = new ColBound(cell.getColumnIndex(),
                cell.getColumnIndex() + table.size());

            /* New record build */
            final ExRecord record = new ExRecord();
            ExFn.itRow(dataRow, bound, (dataCell, cellIndex) -> {
                /* Field / Value */
                final String field = table.field(cellIndex);
                if (Objects.nonNull(field)) {
                    final Class<?> type = MetaAtom.type(field);
                    final Object value = this.extractValue(dataCell, type);

                    /* Stored into record */
                    record.put(field, value);
                } else {
                    this.logger().warn("Field (index = {0}) could not be found", cellIndex);
                }
            });
            /* Not Empty to add, check whether record is valid */
            if (!record.isEmpty()) {
                table.add(record);
            }
        });
        return table;
    }
}
