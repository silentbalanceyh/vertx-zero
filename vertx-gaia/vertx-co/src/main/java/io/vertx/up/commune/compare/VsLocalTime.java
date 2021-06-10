package io.vertx.up.commune.compare;

import java.time.LocalTime;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsLocalTime extends AbstractSame {
    public VsLocalTime() {
        super(LocalTime.class);
    }

    @Override
    public boolean isAnd(final Object valueOld, final Object valueNew) {
        return false;
    }
}
