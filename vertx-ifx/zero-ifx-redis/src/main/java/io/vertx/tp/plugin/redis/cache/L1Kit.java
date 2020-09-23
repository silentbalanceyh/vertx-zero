package io.vertx.tp.plugin.redis.cache;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.cache.l1.L1Config;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class L1Kit {

    static <T> Buffer dataDelivery(final T entity, final ChangeFlag flag,
                                   final Class<?> entityCls) {
        final JsonObject data = new JsonObject();
        data.put("data", (JsonObject) Ut.serializeJson(entity));
        data.put("type", entityCls.getName());
        data.put("flag", flag);
        /*
         * HKey generate
         */
        return data.toBuffer();
    }

    static ConcurrentMap<String, JsonObject> dataConsumer(final JsonObject data, final L1Config config) {
        final ConcurrentMap<String, JsonObject> resultMap = new ConcurrentHashMap<>();

        return resultMap;
    }
}
