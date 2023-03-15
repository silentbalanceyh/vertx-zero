package io.vertx.up.uca.compare;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsLocalTime extends AbstractSameDate {
    public VsLocalTime() {
        super(LocalTime.class);
    }

    @Override
    public boolean eqDate(final LocalDateTime datetimeOld, final LocalDateTime datetimeNew) {
        return this.eqMinute(datetimeOld, datetimeNew);
    }
}
