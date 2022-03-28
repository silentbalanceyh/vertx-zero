package io.vertx.tp.plugin.excel.cell;

import io.vertx.tp.plugin.excel.atom.ExKey;
import io.vertx.tp.plugin.excel.tool.ExOut;
import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

/*
 * Cell Processing for value
 */
@FunctionalInterface
public interface ExValue {
    /*
     * Get value reference to process value
     */
    @SuppressWarnings("all")
    static ExValue get(final Object value) {
        if (Objects.isNull(value)) {
            return Ut.singleton(PureValue.class);
        }
        /*
         * Match completely:
         * {UUID}
         */
        ExValue reference = Pool.VALUE_MAP.get(value);
        if (Objects.isNull(reference)) {
            /*
             * Prefix match
             */
            final String literal = value.toString();

            final String found = Arrays.stream(Literal.Prefix.SET)
                .filter(prefix -> literal.startsWith(prefix))
                .findFirst().orElse(null);
            if (Objects.isNull(found)) {
                /*
                 * Null to default
                 */
                return Ut.singleton(PureValue.class);
            } else {
                /*
                 * Prefix reference
                 */
                return Pool.PREFIX_MAP.get(found);
            }
        } else {
            return reference;
        }
    }

    static Object getValue(final Cell cell, final Class<?> type, final FormulaEvaluator evaluator) {
        /*
         * BLANK / ERROR Processed first to processed
         */
        final CellType cellType = cell.getCellType();
        if (CellType.BLANK == cellType || CellType.ERROR == cellType) {
            /*
             * These two are both returned 'null' value
             */
            return null;
        }
        final String exprOr;
        if (CellType.FORMULA == cellType && Objects.nonNull(evaluator)) {
            /*
             * Formula process, should do processing first
             */
            final CellValue value = evaluator.evaluate(cell);
            final String exprValue = value.getStringValue();
            if (Objects.nonNull(exprValue) && ExKey.VALUE_NULL.equalsIgnoreCase(exprValue.trim())) {
                exprOr = null;
            } else {
                exprOr = exprValue;
            }
            /*
             * Two situations: EMPTY / NULL
             */
            if (Ut.isNil(exprOr)) {
                /*
                 * Terminal for exprOr
                 */
                return null;
            }
        }

        /*
         * Re-calculate function based on
         * 1) Cell
         * 2) Shape
         *
         * If shape = null, keep the original workflow
         * otherwise, use new workflow instead
         */
        final Function<Cell, Object> fun;
        if (CellType.FORMULA == cellType) {
            fun = Pool.FUNS.get(CellType.STRING);
        } else {
            if (Objects.nonNull(type)) {
                final CellType switchedType = ExOut.type(type);
                if (Objects.isNull(switchedType)) {
                    fun = Pool.FUNS.get(cellType);
                } else {
                    fun = Pool.FUNS.get(switchedType);
                }
            } else {
                fun = Pool.FUNS.get(cellType);
            }
        }

        /*
         * Workflow
         *
         * 1) Processed expr value should be mapped to STRING directly
         * 2) Other type should call fun
         */
        if (Objects.isNull(fun)) {
            return null;
        } else {
            return fun.apply(cell);
        }
    }

    <T> T to(Object value);
}
