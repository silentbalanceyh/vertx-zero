package io.vertx.up.plugin.redis.cache;

import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Request;
import io.vertx.redis.client.Response;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class L1ChannelAsync {
    private final static Annal LOGGER = Annal.get(L1ChannelAsync.class);
    private final transient L1Redis redis = new L1Redis();

    L1ChannelAsync() {
    }

    void write(final ConcurrentMap<String, Object> dataMap, final ChangeFlag flag) {
        if (this.redis.enabled()) {
            /*
             * Write cache
             */
            final List<Request> requests = this.redis.requestData(dataMap, flag);
            this.redis.requestAsync(requests, response -> response).onComplete(response -> {
                if (Objects.nonNull(response) && !dataMap.keySet().isEmpty()) {
                    LOGGER.info(CacheMsg.DATA_REFRESHED, Ut.toJArray(dataMap.keySet()));
                }
            });
        }
    }

    void append(final ConcurrentMap<String, Object> dataMap) {
        if (this.redis.enabled()) {
            /*
             * Write cache ( APPEND )
             */
            final List<Request> requests = this.redis.requestDataAppend(dataMap);
            this.redis.requestAsync(requests, response -> response).onComplete(response -> {
                if (Objects.nonNull(response) && !dataMap.keySet().isEmpty()) {
                    LOGGER.info(CacheMsg.DATA_TREE, Ut.toJArray(dataMap.keySet()));
                }
            });
        }
    }

    void eraseTree(final String treeKey) {
        if (this.redis.enabled()) {
            final Request request = Request.cmd(Command.SMEMBERS);
            request.arg(treeKey);
            this.redis.requestAsync(request, response -> {
                final Set<String> eraseKeys = new HashSet<>();
                /*
                 * Tree Major Key
                 */
                if (0 < response.size()) {
                    eraseKeys.add(treeKey);
                    response.stream().filter(Objects::nonNull).map(Response::toString).forEach(eraseKeys::add);
                }
                return eraseKeys;
            }).onComplete(completed -> {
                /*
                 * Collect all keys and remove all
                 * Command, DEL
                 */
                if (completed.succeeded()) {
                    final Set<String> eraseKeys = completed.result();
                    if (Objects.nonNull(eraseKeys) && !eraseKeys.isEmpty()) {
                        /*
                         * Set<String>
                         * Key is null, when it's not null here should execute
                         */
                        final List<Request> requests = this.redis.requestData(eraseKeys);
                        this.redis.requestAsync(requests, deletion -> deletion).onComplete(deletion -> {
                            if (Objects.nonNull(deletion) && deletion.succeeded()) {
                                LOGGER.info(CacheMsg.HIT_REMOVE, String.valueOf(eraseKeys.size()), Ut.toJArray(eraseKeys));
                            }
                        });
                    }
                }
            });
        }
    }

    @SuppressWarnings("all")
    <T> Future<T> readAsync(final String key) {
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
                         * Single Record
                         */
                        return new JsonObject(literal);
                    } else if (Ut.isJArray(literal)) {
                        /*
                         * Collection
                         */
                        return new JsonArray(literal);
                    } else {
                        /*
                         * Call self
                         */
                        return literal;
                    }
                }
            } else return null;
        }).compose(item -> {
            if (item instanceof String) {
                LOGGER.info(CacheMsg.HIT_SECONDARY, item, key);
                return this.readAsync(item.toString());
            } else {
                if (Objects.nonNull(item)) {
                    LOGGER.info(CacheMsg.HIT_DATA, key);
                    return Future.succeededFuture((T) item);
                } else {
                    LOGGER.info(CacheMsg.HIT_FAILURE, key);
                    return Future.succeededFuture();
                }
            }
        });
    }
}
