package io.vertx.tp.plugin.redis.cache;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.redis.RedisInfix;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import redis.clients.jedis.Jedis;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class L1ChannelSync {
    private final static Annal LOGGER = Annal.get(L1ChannelSync.class);
    private final transient Jedis jedis;

    L1ChannelSync() {
        this.jedis = RedisInfix.getJClient();
    }

    JsonObject read(final String key) {
        /*
         * Async convert to type
         */
        if (Objects.isNull(this.jedis)) {
            // Nothing returned
            return null;
        } else {
            final String literal = this.jedis.get(key);
            if (Ut.isNil(literal)) {
                LOGGER.info(CacheMsg.HIT_FAILURE, key);
                return null;
            } else {
                if (Ut.isJObject(literal)) {
                    /*
                     * Data Found
                     */
                    return Ut.toJObject(literal);
                } else {
                    /*
                     * Secondary read
                     */
                    LOGGER.info(CacheMsg.HIT_SECONDARY, literal, key);
                    return this.read(literal);
                }
            }
        }
    }
}
