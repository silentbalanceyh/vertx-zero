package io.vertx.tp.plugin.redis.cache;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
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

    void write(final ConcurrentMap<String, Object> dataMap, final ChangeFlag flag) {
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

    void combine(final ConcurrentMap<String, Object> dataMap) {
        /*
         * Read and Write ( Merged )
         */
    }

    JsonObject read(final String key) {
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

    Future<JsonObject> readAsync(final String key) {
        final Request request = Request.cmd(Command.GET);
        request.arg(key);
        return this.requestAsync(request, response -> {
            if (Objects.nonNull(response)) {
                final Buffer buffer = response.toBuffer();
                return Objects.isNull(buffer) ? null : buffer.toJsonObject();
            } else return null;
        });
    }

    private Request requestData(final String key, final Object input, final ChangeFlag flag) {
        final String dataString;
        if (input instanceof String) {
            dataString = input.toString();
        } else if (input instanceof JsonObject) {
            dataString = ((JsonObject) input).encode();
        } else if (input instanceof JsonArray) {
            dataString = ((JsonArray) input).encode();
        } else {
            dataString = null;
        }
        if (Objects.isNull(dataString)) {
            return null;
        }
        final Request request;
        switch (flag) {
            case ADD:
            case UPDATE: {
                request = Request.cmd(Command.SET);
                request.arg(key);
                request.arg(dataString);
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
        if (Objects.nonNull(this.redis)) {
            this.redis.send(request, res -> {
                if (res.succeeded()) {
                    final T data = consumer.apply(res.result());
                    promise.complete(data);
                } else {
                    promise.fail(res.cause());
                }
            });
            return promise.future();
        } else return Future.succeededFuture();
    }
}
