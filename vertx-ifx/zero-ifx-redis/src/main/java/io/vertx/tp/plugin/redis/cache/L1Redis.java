package io.vertx.tp.plugin.redis.cache;

import io.horizon.eon.em.ChangeFlag;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.Request;
import io.vertx.redis.client.Response;
import io.vertx.tp.plugin.redis.RedisInfix;
import io.vertx.up.log.Annal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class L1Redis {
    private static final Annal LOGGER = Annal.get(L1Redis.class);
    private final transient Redis redis;

    L1Redis() {
        this.redis = RedisInfix.getClient();
        if (Objects.isNull(this.redis)) {
            LOGGER.warn("[ L1 ] Be careful that the reference of Redis is null");
        }
    }

    boolean enabled() {
        return Objects.nonNull(this.redis);
    }

    List<Request> requestData(final Set<String> eraseKeys) {
        final List<Request> requests = new ArrayList<>();
        eraseKeys.forEach(key -> {
            final Request request = Request.cmd(Command.DEL);
            request.arg(key);
            /*
             * ERR wrong number of arguments for 'del' command
             */
            requests.add(request);
        });
        return requests;
    }

    List<Request> requestData(final ConcurrentMap<String, Object> dataMap, final ChangeFlag flag) {
        final List<Request> requests = new ArrayList<>();
        dataMap.forEach((k, data) -> {
            final Request request = this.requestData(k, data, flag);
            if (Objects.nonNull(request)) {
                requests.add(request);
            }
        });
        return requests;
    }

    List<Request> requestDataAppend(final ConcurrentMap<String, Object> dataMap) {
        final List<Request> requests = new ArrayList<>();
        dataMap.forEach((k, data) -> {
            final Request request = Request.cmd(Command.SADD);
            if (Objects.nonNull(data)) {
                request.arg(k);
                /*
                 * Here we convert to Set, instead, no "," needed
                 */
                request.arg(data.toString());
                // Old: request.arg(data.toString() + ",");
                requests.add(request);
            }
        });
        return requests;
    }

    Request requestData(final String key, final Object input, final ChangeFlag flag) {
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

    <T> Future<T> requestAsync(final Request request, final Function<Response, T> consumer) {
        if (Objects.nonNull(this.redis)) {
            final Promise<T> promise = Promise.promise();
            this.redis.send(request, res -> this.handle(promise, consumer, res));
            return promise.future();
        } else {
            return Future.succeededFuture();
        }
    }

    <T> Future<T> requestAsync(final List<Request> requests, final Function<List<Response>, T> consumer) {
        if (requests.isEmpty() || Objects.isNull(this.redis)) {
            return Future.succeededFuture();
        } else {
            final Promise<T> promise = Promise.promise();
            this.redis.batch(requests, res -> this.handle(promise, consumer, res));
            return promise.future();
        }
    }

    private <T, R> void handle(final Promise<T> promise, final Function<R, T> consumer,
                               final AsyncResult<R> res) {
        if (res.succeeded()) {
            final T data = consumer.apply(res.result());
            promise.complete(data);
        } else {
            if (Objects.nonNull(res.cause())) {
                res.cause().printStackTrace();
                promise.fail(res.cause());
            } else {
                promise.complete(null);
            }
        }
    }
}
