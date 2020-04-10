package io.vertx.up.exception.heart;

import io.vertx.up.exception.ZeroRunException;

public class PoolKeyNullException extends ZeroRunException {
    public PoolKeyNullException() {
        super(Info.POOL_KEY_MSG);
    }
}
