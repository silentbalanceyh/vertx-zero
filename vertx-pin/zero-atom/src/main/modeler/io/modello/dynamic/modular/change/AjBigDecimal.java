package io.modello.dynamic.modular.change;

import java.math.BigDecimal;
import java.util.Objects;

final class AjBigDecimal extends AbstractAdjuster {

    public AjBigDecimal() {
        super(BigDecimal.class);
    }

    @Override
    public Object inValue(final Object ucmdbInput) {
        final String literal = this.literal(ucmdbInput);
        if (Objects.isNull(literal)) {
            return null;
        } else {
            return Double.parseDouble(literal);
        }
    }

    @Override
    public Object outValue(final Object input) {
        if (Objects.isNull(input)) {
            return null;
        } else {
            if (input instanceof Double) {
                /*
                 * Fix: java.lang.ClassCastException: java.lang.Double cannot be cast to java.math.BigDecimal
                 */
                final BigDecimal decimal = BigDecimal.valueOf((Double) input);
                return decimal.toPlainString();
            } else if (input instanceof Integer) {
                /*
                 * Fix: java.lang.ClassCastException: java.lang.Integer cannot be cast to java.math.BigDecimal
                 */
                final int result = (Integer) input;
                final BigDecimal decimal = BigDecimal.valueOf(result);
                return decimal.toPlainString();
            } else if (input instanceof Long) {
                /*
                 * Fix: java.lang.ClassCastException: java.lang.Long cannot be cast to java.math.BigDecimal
                 */
                final long result = (Long) input;
                final BigDecimal decimal = BigDecimal.valueOf(result);
                return decimal.toPlainString();
            } else if (input instanceof String) {
                /*
                 * Fix: java.lang.ClassCastException: java.lang.Long cannot be cast to java.math.BigDecimal
                 */
                final double result = Double.parseDouble(input.toString());
                final BigDecimal decimal = BigDecimal.valueOf(result);
                return decimal.toPlainString();
            } else {
                final BigDecimal decimal = (BigDecimal) input;
                return decimal.toPlainString();
            }
        }
    }
}
