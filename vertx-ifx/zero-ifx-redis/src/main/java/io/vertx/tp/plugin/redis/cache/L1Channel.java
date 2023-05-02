package io.vertx.tp.plugin.redis.cache;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class L1Channel {

    private final transient L1ChannelSync channelSync = new L1ChannelSync();
    private final transient L1ChannelAsync channelAsync = new L1ChannelAsync();

    L1Channel() {
    }

    void eraseTree(final String cacheKey) {
        /*
         * Returned void
         */
        this.channelAsync.eraseTree(cacheKey);
    }

    void write(final ConcurrentMap<String, Object> dataMap, final ChangeFlag flag) {
        /*
         * Returned void
         */
        this.channelAsync.write(dataMap, flag);
    }

    void append(final ConcurrentMap<String, Object> dataMap) {
        /*
         * Returned void
         */
        this.channelAsync.append(dataMap);
    }

    <T> T read(final String key) {
        return this.channelSync.read(key);
    }

    <T> Future<T> readAsync(final String key) {
        return this.channelAsync.readAsync(key);
    }
}
