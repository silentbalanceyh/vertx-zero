package io.vertx.up.commune.compare;

import io.vertx.core.json.JsonArray;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsJsonArray extends AbstractSame {
    public VsJsonArray() {
        super(JsonArray.class);
    }

    @Override
    public boolean isAnd(final Object valueOld, final Object valueNew) {
        return false;
    }
}
