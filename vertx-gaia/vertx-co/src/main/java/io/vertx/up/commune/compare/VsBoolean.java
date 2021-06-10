package io.vertx.up.commune.compare;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsBoolean extends AbstractSame {
    public VsBoolean() {
        super(Boolean.class);
    }

    @Override
    public boolean isAnd(final Object valueOld, final Object valueNew) {
        return false;
    }
}
