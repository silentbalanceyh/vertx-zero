package io.modello.dynamic.modular.change;

import io.vertx.up.util.Ut;

final class AjInteger extends AbstractAdjuster {

    public AjInteger() {
        super(Integer.class);
    }

    @Override
    public Object inValue(final Object ucmdbInput) {
        final String liberal = this.literal(ucmdbInput);
        if (Ut.isInteger(liberal)) {
            return Integer.parseInt(liberal);
        } else {
            return this.parseUnit(liberal);
        }
    }

    @Override
    public boolean isSame(final Object oldValue, final Object newValue) {
        final String oldLiteral = this.literal(oldValue);
        final String newLiteral = this.literal(newValue);
        return this.parseUnit(oldLiteral).equals(this.parseUnit(newLiteral));
    }

    private Integer parseUnit(final String literal) {
        /*
         * 去掉 GB, TB, MB, G, T, M 等单位
         */
        if (Ut.isNotNil(literal)) {
            final String normalized = literal.replaceAll(
                "(GB|TB|MB|G|T|M)", ""
            ).trim();
            return Ut.isNotNil(normalized) ? (int) Double.parseDouble(normalized) : 0;
        } else {
            return 0;
        }
    }
}
