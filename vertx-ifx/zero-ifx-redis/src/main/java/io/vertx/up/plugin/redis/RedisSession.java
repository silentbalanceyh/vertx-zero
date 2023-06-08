package io.vertx.up.plugin.redis;

import io.vertx.ext.auth.PRNG;
import io.vertx.ext.web.sstore.impl.SharedDataSessionImpl;

public class RedisSession extends SharedDataSessionImpl {
    public RedisSession() {
        super();
    }

    public RedisSession(final PRNG random) {
        super(random);
    }

    public RedisSession(final PRNG random, final long timeout, final int length) {
        super(random, timeout, length);
    }
}
