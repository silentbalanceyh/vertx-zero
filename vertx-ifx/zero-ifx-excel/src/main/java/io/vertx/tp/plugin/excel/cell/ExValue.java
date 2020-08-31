package io.vertx.tp.plugin.excel.cell;

import io.vertx.tp.plugin.excel.atom.ExKey;
import io.vertx.up.commune.element.Shape;
import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

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
            if (literal.startsWith("JSON")) {
                reference = Pool.PREFIX_MAP.get("JSON");
            }
            if (Objects.isNull(reference)) {
                /*
                 * Null to default
                 */
                return Ut.singleton(PureValue.class);
            } else {
                return reference;
            }
        } else {
            return reference;
        }
    }

    static Object getValue(final Cell cell, final FormulaEvaluator evaluator, final Shape shape) {
        return null;
    }

    static Object getValue(final Cell cell, final FormulaEvaluator evaluator) {
        final Function<Cell, Object> fun = Pool.FUNS.get(cell.getCellType());
        if (null == fun) {
            if (CellType.FORMULA == cell.getCellType()) {
                final CellValue value = evaluator.evaluate(cell);
                final String literal = value.getStringValue();
                if (Objects.isNull(literal)) {
                    return null;
                } else {
                    /* Literal `NULL` value here */
                    return ExKey.VALUE_NULL.endsWith(literal.trim()) ? null : literal;
                }
            } else {
                return null;
            }
        } else {
            return fun.apply(cell);
        }
    }

    <T> T to(Object value);
}
