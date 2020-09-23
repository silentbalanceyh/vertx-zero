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
import io.vertx.up.util.Ut;
import redis.clients.jedis.Jedis;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class L1Channel {

    private final transient Redis redis;
    private final transient Jedis jedis;

    L1Channel() {
        this.redis = RedisInfix.getClient();
        this.jedis = RedisInfix.getJClient();
    }

    JsonObject hit(final String key) {
        /*
         * Async convert to sync
         */
        final String literal = this.jedis.get(key);
        if (Ut.isNil(literal)) {
            return null;
        } else {
            return Ut.toJObject(literal);
        }
    }

    Future<JsonObject> hitAsync(final String key) {
        final Request request = Request.cmd(Command.GET);
        request.arg(key);
        return this.hitAsync(request, response -> {
            final Buffer buffer = response.toBuffer();
            return Objects.isNull(buffer) ? null : buffer.toJsonObject();
        });
    }

    private <T> Future<T> hitAsync(final Request request, final Function<Response, T> consumer) {
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
