package io.vertx.tp.plugin.excel;

import io.horizon.eon.VValue;
import io.horizon.specification.modeler.TypeAtom;
import io.horizon.uca.log.Annal;
import io.vertx.tp.plugin.booting.KConnect;
import io.vertx.tp.plugin.excel.atom.ExKey;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.tp.plugin.excel.ranger.*;
import io.vertx.tp.plugin.excel.tool.ExFn;
import io.vertx.up.uca.log.DevEnv;
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
    public Set<ExTable> analyzed(final ExBound bound, final TypeAtom typeAtom) {
        if (DevEnv.devExcelRange()) {
            LOGGER.info("[ Έξοδος ] Scan Range: {0}", bound);
        }
        try {
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
                        && Ut.isNotNil(cell.getStringCellValue())
                        /* Fix issue of {TABLE} here for BLANK CELL */
                        && cell.getStringCellValue().equals(ExKey.EXPR_TABLE));
            });
            /* analyzedBounds */
            if (!tableCell.isEmpty()) {
                if (DevEnv.devExcelRange()) {
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
                        return this.analyzed(row, cell, limit, typeAtom);
                    }
                }).filter(Objects::nonNull).forEach(tables::add);
            }
            return tables;
        } catch (final Throwable ex) {
            LOGGER.fatal(ex);
            return new HashSet<>();
        }
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
        indexes.remove(VValue.IDX);
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
    private ExTable analyzed(final Row row, final Cell cell, final Integer limitation, final TypeAtom typeAtom) {
        /* Build ExTable */
        final ExTable table = this.create(row, cell);

        /* ExIn build */
        final ExIn in;
        if (Objects.nonNull(typeAtom) && typeAtom.isComplex()) {
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
        return in.applyData(table, dataRange, cell, typeAtom);
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
            final KConnect connect = Pool.CONNECTS.get(table.getName());
            if (Objects.nonNull(connect)) {
                table.setConnect(connect);
            }
        }
        return table;
    }
}
