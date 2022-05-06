package io.vertx.up.uca.compare;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsLocalDate extends AbstractSameDate {
    public VsLocalDate() {
        super(LocalDate.class);
    }

    @Override
    public boolean eqDate(final LocalDateTime valueOld, final LocalDateTime valueNew) {
        return this.eqDay(valueOld, valueNew);
    }
}
