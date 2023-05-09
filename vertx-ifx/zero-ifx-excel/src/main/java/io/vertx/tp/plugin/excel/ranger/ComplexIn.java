package io.vertx.tp.plugin.excel.ranger;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.modello.atom.normalize.MetaAtom;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.excel.atom.ExRecord;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.tp.plugin.excel.tool.ExFn;
import io.vertx.up.atom.Refer;
import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
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
            /* Parent map for extraction */
            final Refer foundParent = new Refer();
            ExFn.itRowZip(found, foundRow, bound, (first, second) -> {
                /* Parent / Child */
                final String parent = first.getStringCellValue();
                final String child = second.getStringCellValue();
                if (Ut.isNotNil(parent) || Ut.isNotNil(child)) {
                    /*
                     * field calculation
                     */
                    if (Ut.isNil(child) && Ut.isNotNil(parent)) {
                        /*
                         * Single field here
                         */
                        table.add(parent);
                    } else if (Ut.isNotNil(child) && Ut.isNotNil(parent)) {
                        /*
                         * Parent found and set the first child
                         */
                        foundParent.add(parent);
                        table.add(parent, child);
                    } else if (Ut.isNotNil(child) && Ut.isNil(parent)) {
                        /*
                         * Add left all child
                         */
                        table.add(foundParent.get(), child);
                    }
                }
            });
            hod.add(new RowBound(foundRow.getRowNum() + 1, limitation));
        });
        return hod.get();
    }

    @Override
    public ExTable applyData(final ExTable table, final ExBound dataRange, final Cell cell, final MetaAtom metaAtom) {
        /*
         * Build data column range based on current cell and table
         * 1) table means ExTable for range
         * 2) cell is the first row
         */
        final ExBound bound = new ColBound(cell.getColumnIndex(), cell.getColumnIndex() + table.size());
        /*
         * Get index that to checked be `null` for different record here
         */
        final Set<Integer> diffSet = table.indexDiff();
        /* Data Range */
        ExFn.itSheet(this.sheet, dataRange, diffSet, (rowList) -> {
            /*
             *  Build data part instead of each row here
             *  Each row should be record
             * */
            final ExRecord record = new ExRecord();
            final ConcurrentMap<String, JsonArray> complexMap = new ConcurrentHashMap<>();
            final ConcurrentMap<String, JsonObject> rowMap = new ConcurrentHashMap<>();
            // --------------- First row only ---------------
            /*
             * The first line is major record
             */
            final Row row = rowList.get(VValue.IDX);
            /*
             * In first iterator for first row, the system should build `complexMap`
             */
            ExFn.itRow(row, bound, this.cellConsumer(record, rowMap, table, metaAtom));
            this.extractComplex(complexMap, rowMap);

            // ----------------- Other row -------------------
            /*
             * From index = 1 to iterate the left row
             */
            final int size = rowList.size();
            if (1 < size) {
                for (int idx = 1; idx < size; idx++) {
                    final Row dataRow = rowList.get(idx);
                    ExFn.itRow(dataRow, bound, this.cellConsumer(rowMap, table, metaAtom));
                    this.extractComplex(complexMap, rowMap);
                }
            }
            // ------------------ Copy JsonArray --------------
            if (!complexMap.isEmpty()) {
                complexMap.forEach(record::put);
            }
            /* Not Empty to add, check whether record is valid */
            if (!record.isEmpty()) {
                table.add(record);
            }
        });
        return table;
    }

    private BiConsumer<Cell, Integer> cellConsumer(final ExRecord record, final ConcurrentMap<String, JsonObject> rowMap,
                                                   final ExTable table, final MetaAtom metaAtom) {
        return (dataCell, cellIndex) -> {
            /* Field / Value / field should not be null */
            final String field = table.field(cellIndex);
            if (Objects.nonNull(field)) {
                /* Different value processing */
                if (field.contains(VString.DOT)) {
                    /*
                     * Do Processing
                     */
                    this.cellConsumer(rowMap, field).accept(dataCell, metaAtom);
                } else {
                    /* Pure Workflow */
                    final Class<?> type = metaAtom.type(field);
                    final Object value = this.extractValue(dataCell, type);
                    record.put(field, value);
                }
            } else {
                this.logger().warn("Field (index = {0}) could not be found", cellIndex);
            }
        };
    }

    private BiConsumer<Cell, Integer> cellConsumer(final ConcurrentMap<String, JsonObject> rowMap,
                                                   final ExTable table, final MetaAtom metaAtom) {
        return (dataCell, cellIndex) -> {
            /* Field / Value / field should not be null */
            final String field = table.field(cellIndex);
            if (Objects.nonNull(field)) {
                if (field.contains(VString.DOT)) {
                    /*
                     * Data Structure for complex data
                     */
                    this.cellConsumer(rowMap, field).accept(dataCell, metaAtom);
                }
            } else {
                this.logger().warn("Field (index = {0}) could not be found", cellIndex);
            }
        };
    }
}
