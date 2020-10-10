package io.vertx.tp.plugin.redis.cache;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Request;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class L1ChannelAsync {
    private final static Annal LOGGER = Annal.get(L1ChannelAsync.class);
    private final transient L1Redis redis = new L1Redis();

    L1ChannelAsync() {

    }

    void write(final ConcurrentMap<String, Object> dataMap, final ChangeFlag flag) {
        /*
         * Write cache
         */
        final List<Request> requests = this.redis.requestData(dataMap, flag);
        /*
         * Batch processing
         */
        this.redis.requestAsync(requests, response -> response).onSuccess(response -> {
            if (Objects.nonNull(response)) {
                LOGGER.info(CacheMsg.DATA_REFRESHED, Ut.toJArray(dataMap.keySet()));
            }
        });
    }

    void append(final ConcurrentMap<String, Object> dataMap) {
        /*
         * Write cache ( APPEND )
         */
        final List<Request> requests = this.redis.requestDataAppend(dataMap);
        this.redis.requestAsync(requests, response -> response).onSuccess(response -> {
            if (Objects.nonNull(response)) {
                LOGGER.info(CacheMsg.DATA_TREE, Ut.toJArray(dataMap.keySet()));
            }
        });
    }

    Future<JsonObject> readAsync(final String key) {
        final Request request = Request.cmd(Command.GET);
        request.arg(key);
        return this.redis.requestAsync(request, response -> {
            if (Objects.nonNull(response)) {
                final Buffer buffer = response.toBuffer();
                /*
                 * Whether it's refer
                 */
                if (Objects.isNull(buffer)) {
                    return null;
                } else {
                    final String literal = buffer.toString();
                    if (Ut.isJObject(literal)) {
                        /*
                         * Data Found
                         */
                        return new JsonObject(literal);
                    } else {
                        /*
                         * Call self
                         */
                        return Future.succeededFuture(literal);
                    }
                }
            } else return null;
        }).compose(item -> {
            if (item instanceof String) {
                LOGGER.info(CacheMsg.HIT_SECONDARY, item, key);
                return this.readAsync(item.toString());
            } else {
                return Future.succeededFuture((JsonObject) item);
            }
        });
    }
}
