package io.horizon.uca.compare;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsInstant extends AbstractSameDate {
    public VsInstant() {
        super(Instant.class);
    }

    @Override
    public boolean eqDate(final LocalDateTime datetimeOld, final LocalDateTime datetimeNew) {
        return this.eqMinute(datetimeOld, datetimeNew);
    }
}
