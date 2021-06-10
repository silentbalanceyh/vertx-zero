package io.vertx.up.commune.compare;

import java.time.Instant;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsInstant extends AbstractSame {
    public VsInstant() {
        super(Instant.class);
    }

    @Override
    public boolean isAnd(final Object valueOld, final Object valueNew) {
        return false;
    }
}
