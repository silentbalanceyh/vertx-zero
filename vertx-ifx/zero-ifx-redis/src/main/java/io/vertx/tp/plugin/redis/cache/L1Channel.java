package io.vertx.tp.plugin.redis.cache;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.Request;
import io.vertx.redis.client.Response;
import io.vertx.tp.plugin.redis.RedisInfix;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class L1Channel {
    private final static Annal LOGGER = Annal.get(L1Channel.class);

    private final transient Redis redis;
    private final transient Jedis jedis;

    L1Channel() {
        this.redis = RedisInfix.getClient();
        this.jedis = RedisInfix.getJClient();
    }

    void refresh(final ConcurrentMap<String, JsonObject> dataMap, final ChangeFlag flag) {
        final List<Request> requests = new ArrayList<>();
        dataMap.forEach((k, data) -> {
            final Request request = this.requestData(k, data, flag);
            if (Objects.nonNull(request)) {
                requests.add(request);
            }
        });
        if (!requests.isEmpty() && Objects.nonNull(this.redis)) {
            this.redis.batch(requests, res -> {
                if (res.succeeded()) {
                    LOGGER.info("( Cache ) The key `{0}` has been refreshed.", Ut.toJArray(dataMap.keySet()));
                } else {
                    if (Objects.nonNull(res.cause())) {
                        res.cause().printStackTrace();
                    }
                }
            });
        }
    }

    void refresh(final String key, final JsonObject data, final ChangeFlag flag) {
        final Request request = this.requestData(key, data, flag);
        if (Objects.nonNull(request) && Objects.nonNull(this.redis)) {
            this.redis.send(request, res -> {
                if (res.succeeded()) {
                    LOGGER.info("( Cache ) The key `{0}` has been refreshed.", key);
                } else {
                    if (Objects.nonNull(res.cause())) {
                        res.cause().printStackTrace();
                    }
                }
            });
        }
    }

    JsonObject hit(final String key) {
        /*
         * Async convert to sync
         */
        if (Objects.nonNull(this.jedis)) {
            final String literal = this.jedis.get(key);
            if (Ut.isNil(literal)) {
                LOGGER.info("( Cache ) The key `{0}` has not been Hit !!!.", key);
                return null;
            } else {
                return Ut.toJObject(literal);
            }
        } else {
            return null;
        }
    }

    Future<JsonObject> hitAsync(final String key) {
        final Request request = Request.cmd(Command.GET);
        request.arg(key);
        return this.requestAsync(request, response -> {
            final Buffer buffer = response.toBuffer();
            return Objects.isNull(buffer) ? null : buffer.toJsonObject();
        });
    }

    private Request requestData(final String key, final JsonObject data, final ChangeFlag flag) {
        final Request request;
        switch (flag) {
            case ADD:
            case UPDATE: {
                request = Request.cmd(Command.SET);
                request.arg(key);
                request.arg(data.encode());
            }
            break;
            case DELETE: {
                request = Request.cmd(Command.DEL);
                request.arg(key);
            }
            break;
            default:
                request = null;
                break;
        }
        return request;
    }

    private <T> Future<T> requestAsync(final Request request, final Function<Response, T> consumer) {
        final Promise<T> promise = Promise.promise();
        this.redis.send(request, res -> {
            if (res.succeeded()) {
                final T data = consumer.apply(res.result());
                promise.complete(data);
            } else {
                promise.fail(res.cause());
            }
        });
        return promise.future();
    }
}
