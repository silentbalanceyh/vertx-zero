package io.vertx.tp.plugin.excel;

import io.vertx.tp.plugin.excel.atom.ExConnect;
import io.vertx.tp.plugin.excel.atom.ExKey;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.tp.plugin.excel.ranger.*;
import io.vertx.tp.plugin.excel.tool.ExFn;
import io.vertx.up.commune.element.TypeAtom;
import io.vertx.up.eon.Values;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Debugger;
import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.*;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Wrapper Sheet object to store data, this object could help to
 * build ExTable here.
 */
public class SheetAnalyzer implements Serializable {
    private static final Annal LOGGER = Annal.get(SheetAnalyzer.class);
    private final transient Sheet sheet;
    private transient FormulaEvaluator evaluator;
    private transient ExIn scanner;

    public SheetAnalyzer(final Sheet sheet) {
        this.sheet = sheet;
    }

    public SheetAnalyzer on(final FormulaEvaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    /*
     * Scan sheet to find all the data and definition part
     */
    public Set<ExTable> analyzed(final ExBound bound, final TypeAtom TypeAtom) {
        if (Debugger.onExcelRanging()) {
            LOGGER.info("[ Έξοδος ] Scan Range: {0}", bound);
        }
        /* Sheet scanning */
        final Set<ExTable> tables = new HashSet<>();

        /* Row scanning for {TABLE} */
        final List<Cell> tableCell = new ArrayList<>();
        ExFn.itSheet(this.sheet, bound, (row, index) -> {
            final ExBound colBound = new ColBound(row);
            ExFn.itRow(row, colBound,
                /* {Table} Cell */
                (cell, colIndex) -> tableCell.add(cell),
                /* Predicate Here */
                cell -> CellType.STRING == cell.getCellType()
                    /* Do not check blank / empty cell here */
                    && Ut.notNil(cell.getStringCellValue())
                    /* Fix issue of {TABLE} here for BLANK CELL */
                    && cell.getStringCellValue().equals(ExKey.EXPR_TABLE));
        });
        /* analyzedBounds */
        if (!tableCell.isEmpty()) {
            if (Debugger.onExcelRanging()) {
                LOGGER.info("[ Έξοδος ] Scanned sheet: {0}, tableCell = {1}",
                    this.sheet.getSheetName(), String.valueOf(tableCell.size()));
            }
            /* Range scaned */
            final ConcurrentMap<Integer, Integer> range = this.getRange(tableCell);
            tableCell.stream().map(cell -> {
                final Row row = this.sheet.getRow(cell.getRowIndex());
                if (null == row) {
                    return null;
                } else {
                    final Integer limit = range.get(cell.hashCode());
                    return this.analyzed(row, cell, limit, TypeAtom);
                }
            }).filter(Objects::nonNull).forEach(tables::add);
        }
        return tables;
    }

    private ConcurrentMap<Integer, Integer> getRange(final List<Cell> tableCell) {
        /* Range scaned */
        final List<Integer> hashCodes = new ArrayList<>();
        final List<Integer> indexes = new ArrayList<>();
        tableCell.forEach(cell -> {
            /* Adjust calculation index and decrease */
            indexes.add(cell.getRowIndex() - 1);
            hashCodes.add(cell.hashCode());
        });
        indexes.add(this.sheet.getLastRowNum());
        indexes.remove(Values.IDX);
        final ConcurrentMap<Integer, Integer> indexMap = new ConcurrentHashMap<>();
        for (int idx = 0; idx < hashCodes.size(); idx++) {
            final Integer key = hashCodes.get(idx);
            final Integer index = indexes.get(idx);
            indexMap.put(key, index);
        }
        return indexMap;
    }

    /*
     * Scan sheet from row to cell to build each table.
     */
    private ExTable analyzed(final Row row, final Cell cell, final Integer limitation, final TypeAtom TypeAtom) {
        /* Build ExTable */
        final ExTable table = this.create(row, cell);

        /* ExIn build */
        final ExIn in;
        if (Objects.nonNull(TypeAtom) && TypeAtom.isComplex()) {
            in = new ComplexIn(this.sheet).bind(this.evaluator);
        } else {
            in = new PureIn(this.sheet).bind(this.evaluator);
        }
        /*
         * Table processing
         */
        final ExBound dataRange = in.applyTable(table, row, cell, limitation);
        /*
         * Data processing
         */
        return in.applyData(table, dataRange, cell, TypeAtom);
    }

    private ExTable create(final Row row, final Cell cell) {
        /* Sheet Name */
        final ExTable table = new ExTable(this.sheet.getSheetName());
        /* Name, Connect, Description - Cell */
        ExFn.onCell(row, cell.getColumnIndex() + 1,
            found -> table.setName(found.getStringCellValue()));
        ExFn.onCell(row, cell.getColumnIndex() + 2,
            found -> table.setDescription(found.getStringCellValue()));
        /* Calculation */
        if (Objects.nonNull(table.getName()) && Pool.CONNECTS.containsKey(table.getName())) {
            final ExConnect connect = Pool.CONNECTS.get(table.getName());
            if (Objects.nonNull(connect)) {
                table.setConnect(connect);
            }
        }
        return table;
    }
}
