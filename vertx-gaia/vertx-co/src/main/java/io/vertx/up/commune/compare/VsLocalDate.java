package io.vertx.up.commune.compare;

import java.time.LocalDate;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsLocalDate extends AbstractSame {
    public VsLocalDate() {
        super(LocalDate.class);
    }

    @Override
    public boolean isAnd(final Object valueOld, final Object valueNew) {
        return false;
    }
}
