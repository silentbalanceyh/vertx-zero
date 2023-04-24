package io.vertx.up.exception.heart;

import io.zero.exception.ZeroRunException;

public class PoolKeyNullException extends ZeroRunException {
    public PoolKeyNullException() {
        super(Info.CACHE_KEY_MSG);
    }
}
