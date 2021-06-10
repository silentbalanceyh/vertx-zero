package io.vertx.up.commune.compare;

import java.math.BigDecimal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsBigDecimal extends AbstractSame {
    public VsBigDecimal() {
        super(BigDecimal.class);
    }

    @Override
    public boolean isAnd(final Object valueOld, final Object valueNew) {
        return false;
    }
}
