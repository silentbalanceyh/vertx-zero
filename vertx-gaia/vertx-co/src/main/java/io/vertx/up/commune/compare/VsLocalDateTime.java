package io.vertx.up.commune.compare;

import java.time.LocalDateTime;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsLocalDateTime extends AbstractSame {
    public VsLocalDateTime() {
        super(LocalDateTime.class);
    }

    @Override
    public boolean isAnd(final Object valueOld, final Object valueNew) {
        return false;
    }
}
