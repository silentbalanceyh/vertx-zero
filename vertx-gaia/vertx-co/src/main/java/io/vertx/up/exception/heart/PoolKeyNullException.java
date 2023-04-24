package io.vertx.up.exception.heart;

import io.horizon.exception.ZeroRunException;

public class PoolKeyNullException extends ZeroRunException {
    public PoolKeyNullException() {
        super(Info.CACHE_KEY_MSG);
    }
}
