package io.vertx.up.commune.compare;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsInteger extends AbstractSame {
    public VsInteger() {
        super(Integer.class);
    }

    @Override
    public boolean isAnd(final Object valueOld, final Object valueNew) {
        return false;
    }
}
