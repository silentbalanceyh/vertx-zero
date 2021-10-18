package io.vertx.up.commune.compare;

import java.time.LocalDateTime;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsLocalDateTime extends AbstractSameDate {
    public VsLocalDateTime() {
        super(LocalDateTime.class);
    }

    @Override
    public boolean eqDate(final LocalDateTime datetimeOld, final LocalDateTime datetimeNew) {
        return this.eqMinute(datetimeOld, datetimeNew);
    }
}
