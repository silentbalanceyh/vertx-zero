package io.vertx.tp.plugin.redis.cache;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.l1.AbstractL1;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class L1 extends AbstractL1 {
    private transient final L1Channel channel;

    public L1() {
        this.channel = new L1Channel();
    }

    @Override
    public Future<JsonObject> readAsync(final String key) {
        return this.channel.readAsync(key);
    }

    @Override
    public JsonObject read(final String key) {
        return this.channel.read(key);
    }
}
