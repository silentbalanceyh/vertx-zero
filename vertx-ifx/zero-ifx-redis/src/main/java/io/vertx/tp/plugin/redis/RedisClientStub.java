package io.vertx.tp.plugin.redis;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import io.vertx.redis.RedisTransaction;
import io.vertx.redis.Script;
import io.vertx.redis.client.*;
import io.vertx.redis.impl.RedisEncoding;
import io.vertx.redis.op.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static io.vertx.redis.client.Command.*;

@SuppressWarnings("all")
public class RedisClientStub implements RedisClient {

    private final Vertx vertx;
    private final RedisOptions options;
    private final AtomicReference<CompletableFuture<Redis>> redis = new AtomicReference<>();

    public RedisClientStub(final Vertx vertx, final RedisOptions options) {
        this.vertx = vertx;
        this.options = options;
    }

    /**
     * A helper method to package method parameters into JsonArray payload.
     * <p>
     * Null parameters are ignored.
     *
     * @param parameters Call parameters
     * @return JsonArray that can be passed to send()
     */
    @SuppressWarnings("unchecked")
    private static List<?> toPayload(final Object... parameters) {
        final List<Object> result = new ArrayList<>(parameters.length);

        for (Object param : parameters) {
            // unwrap
            if (param instanceof JsonArray) {
                param = ((JsonArray) param).getList();
            }
            // unwrap
            if (param instanceof JsonObject) {
                param = ((JsonObject) param).getMap();
            }

            if (param instanceof Collection) {
                ((Collection) param).stream().filter(Objects::nonNull).forEach(result::add);
            } else if (param instanceof Map) {
                for (final Map.Entry<?, ?> pair : ((Map<?, ?>) param).entrySet()) {
                    result.add(pair.getKey());
                    result.add(pair.getValue());
                }
            } else if (param instanceof Stream) {
                ((Stream) param).forEach(e -> {
                    if (e instanceof Object[]) {
                        Collections.addAll(result, (Object[]) e);
                    } else {
                        result.add(e);
                    }
                });
            } else if (param != null) {
                result.add(param);
            }
        }
        return result;
    }

    private static JsonArray toJsonArray(final Response response) {
        final JsonArray json = new JsonArray();

        if (response.type() != ResponseType.MULTI) {
            switch (response.type()) {
                case INTEGER:
                    json.add(response.toLong());
                    break;
                case SIMPLE:
                case BULK:
                    json.add(response.toString());
                    break;
            }
            return json;
        }

        for (final Response r : response) {
            if (r == null) {
                json.addNull();
            } else {
                switch (r.type()) {
                    case INTEGER:
                        json.add(r.toLong());
                        break;
                    case SIMPLE:
                    case BULK:
                        json.add(r.toString());
                        break;
                    case MULTI:
                        json.add(toJsonArray(r));
                        break;
                }
            }
        }

        return json;
    }

    private static JsonObject toJsonObject(final Response response) {
        final JsonObject json = new JsonObject();
        for (final String key : response.getKeys()) {
            final Response value = response.get(key);
            switch (value.type()) {
                case INTEGER:
                    json.put(key, value.toLong());
                    break;
                case SIMPLE:
                case BULK:
                    json.put(key, value.toString());
                    break;
                case MULTI:
                    json.put(key, toJsonArray(value));
                    break;
            }
        }

        return json;
    }

    private void send(final Command command, final List arguments, final Handler<AsyncResult<Response>> handler) {
        final Request req = Request.cmd(command);

        if (arguments != null) {
            for (final Object o : arguments) {
                if (o == null) {
                    req.nullArg();
                } else if (o instanceof Buffer) {
                    req.arg((Buffer) o);
                } else {
                    req.arg(o.toString());
                }
            }
        }

        CompletableFuture<Redis> fut = redis.get();
        if (fut == null) {
            final CompletableFuture f = new CompletableFuture<>();
            if (redis.compareAndSet(null, f)) {
                fut = f;
                Redis.createClient(vertx, new io.vertx.redis.client.RedisOptions()
                        .setNetClientOptions(options)
                        .setEndpoint(options.isDomainSocket() ?
                                SocketAddress.domainSocketAddress(options.getDomainSocketAddress()).toString() :
                                SocketAddress.inetSocketAddress(options.getPort(), options.getHost()).toString()));
                // TODO: Process for new Redis Client;
            } else {
                fut = redis.get();
            }
        }

        fut.whenComplete((client, err) -> {
            if (err == null) {
                client.send(req, handler);
            } else {
                handler.handle(Future.failedFuture(err));
            }
        });
    }

    private void sendLong(final Command command, final List arguments, final Handler<AsyncResult<Long>> handler) {
        send(command, arguments, ar -> {
            if (ar.failed()) {
                handler.handle(Future.failedFuture(ar.cause()));
            } else {
                handler.handle(Future.succeededFuture(ar.result().toLong()));
            }
        });
    }

    private void sendString(final Command command, final List arguments, final Handler<AsyncResult<String>> handler) {
        send(command, arguments, ar -> {
            if (ar.failed()) {
                handler.handle(Future.failedFuture(ar.cause()));
            } else {
                handler.handle(Future.succeededFuture(ar.result() != null ? ar.result().toString() : null));
            }
        });
    }

    private void sendJsonArray(final Command command, final List arguments, final Handler<AsyncResult<JsonArray>> handler) {
        send(command, arguments, ar -> {
            if (ar.failed()) {
                handler.handle(Future.failedFuture(ar.cause()));
            } else {
                handler.handle(Future.succeededFuture(ar.result() != null ? toJsonArray(ar.result()) : null));
            }
        });
    }

    private void sendVoid(final Command command, final List arguments, final Handler<AsyncResult<Void>> handler) {
        send(command, arguments, ar -> {
            if (ar.failed()) {
                handler.handle(Future.failedFuture(ar.cause()));
            } else {
                handler.handle(Future.succeededFuture());
            }
        });
    }

    private void sendBuffer(final Command command, final List arguments, final Handler<AsyncResult<Buffer>> handler) {
        send(command, arguments, ar -> {
            if (ar.failed()) {
                handler.handle(Future.failedFuture(ar.cause()));
            } else {
                final Response response = ar.result();
                /*
                 * Fixed: NullPointerException
                 */
                if (Objects.isNull(response)) {
                    handler.handle(Future.succeededFuture(null));
                } else {
                    handler.handle(Future.succeededFuture(ar.result().toBuffer()));
                }
            }
        });
    }

    private void sendJsonObject(final Command command, final List arguments, final Handler<AsyncResult<JsonObject>> handler) {
        send(command, arguments, ar -> {
            if (ar.failed()) {
                handler.handle(Future.failedFuture(ar.cause()));
            } else {
                handler.handle(Future.succeededFuture(toJsonObject(ar.result())));
            }
        });
    }

    @Override
    public void close(final Handler<AsyncResult<Void>> handler) {
        final CompletableFuture<Redis> prev = redis.getAndSet(null);
        if (prev != null) {
            prev.whenComplete((client, err) -> {
                if (err == null) {
                    client.close();
                    if (handler != null) {
                        handler.handle(Future.succeededFuture());
                    }
                } else {
                    if (handler != null) {
                        handler.handle(Future.failedFuture(err.getCause()));
                    }
                }
            });
        }
    }

    @Override
    public RedisClient append(final String key, final String value, final Handler<AsyncResult<Long>> handler) {
        sendLong(APPEND, toPayload(key, value), handler);
        return this;
    }

    @Override
    public RedisClient auth(final String password, final Handler<AsyncResult<String>> handler) {
        sendString(AUTH, toPayload(password), handler);
        return this;
    }

    @Override
    public RedisClient bgrewriteaof(final Handler<AsyncResult<String>> handler) {
        sendString(BGREWRITEAOF, null, handler);
        return this;
    }

    @Override
    public RedisClient bgsave(final Handler<AsyncResult<String>> handler) {
        sendString(BGSAVE, null, handler);
        return this;
    }

    @Override
    public RedisClient bitcount(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(BITCOUNT, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient bitcountRange(final String key, final long start, final long end, final Handler<AsyncResult<Long>> handler) {
        sendLong(BITCOUNT, toPayload(key, start, end), handler);
        return this;
    }

    @Override
    public RedisClient bitop(final BitOperation operation, final String destkey, final List<String> keys, final Handler<AsyncResult<Long>> handler) {
        sendLong(BITOP, toPayload(operation.name(), destkey, keys), handler);
        return this;
    }

    @Override
    public RedisClient bitpos(final String key, final int bit, final Handler<AsyncResult<Long>> handler) {
        sendLong(BITPOS, toPayload(key, bit), handler);
        return this;
    }

    @Override
    public RedisClient bitposFrom(final String key, final int bit, final int start, final Handler<AsyncResult<Long>> handler) {
        sendLong(BITPOS, toPayload(key, bit, start), handler);
        return this;
    }

    @Override
    public RedisClient bitposRange(final String key, final int bit, final int start, final int stop, final Handler<AsyncResult<Long>> handler) {
        sendLong(BITPOS, toPayload(key, bit, start, stop), handler);
        return this;
    }

    @Override
    public RedisClient blpop(final String key, final int seconds, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(BLPOP, toPayload(key, seconds), handler);
        return this;
    }

    @Override
    public RedisClient blpopMany(final List<String> keys, final int seconds, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(BLPOP, toPayload(keys, seconds), handler);
        return this;
    }

    @Override
    public RedisClient brpop(final String key, final int seconds, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(BRPOP, toPayload(key, seconds), handler);
        return this;
    }

    @Override
    public RedisClient brpopMany(final List<String> keys, final int seconds, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(BRPOP, toPayload(keys, seconds), handler);
        return this;
    }

    @Override
    public RedisClient brpoplpush(final String key, final String destkey, final int seconds, final Handler<AsyncResult<String>> handler) {
        sendString(BRPOPLPUSH, toPayload(key, destkey, seconds), handler);
        return this;
    }

    @Override
    public RedisClient clientKill(final KillFilter filter, final Handler<AsyncResult<Long>> handler) {
        sendLong(CLIENT, toPayload("kill", filter.toJsonArray().getList()), handler);
        return this;
    }

    @Override
    public RedisClient clientList(final Handler<AsyncResult<String>> handler) {
        sendString(CLIENT, toPayload("list"), handler);
        return this;
    }

    @Override
    public RedisClient clientGetname(final Handler<AsyncResult<String>> handler) {
        sendString(CLIENT, toPayload("GETNAME"), handler);
        return this;
    }

    @Override
    public RedisClient clientPause(final long millis, final Handler<AsyncResult<String>> handler) {
        sendString(CLIENT, toPayload("PAUSE", millis), handler);
        return this;
    }

    @Override
    public RedisClient clientSetname(final String name, final Handler<AsyncResult<String>> handler) {
        sendString(CLIENT, toPayload("SETNAME", name), handler);
        return this;
    }

    @Override
    public RedisClient clusterAddslots(final List<Long> slots, final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("ADDSLOTS"), handler);
        return this;
    }

    @Override
    public RedisClient clusterCountFailureReports(final String nodeId, final Handler<AsyncResult<Long>> handler) {
        sendLong(CLUSTER, toPayload("COUNT-FAILURE-REPORTS", nodeId), handler);
        return this;
    }

    @Override
    public RedisClient clusterCountkeysinslot(final long slot, final Handler<AsyncResult<Long>> handler) {
        sendLong(CLUSTER, toPayload("COUNTKEYSINSLOT", slot), handler);
        return this;
    }

    @Override
    public RedisClient clusterDelslots(final long slot, final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("DELSLOTS", slot), handler);
        return this;
    }

    @Override
    public RedisClient clusterDelslotsMany(final List<Long> slots, final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("DELSLOTS", slots), handler);
        return this;
    }

    @Override
    public RedisClient clusterFailover(final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("FAILOVER"), handler);
        return this;
    }

    @Override
    public RedisClient clusterFailOverWithOptions(final FailoverOptions options, final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("FAILOVER", options), handler);
        return this;
    }

    @Override
    public RedisClient clusterForget(final String nodeId, final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("FORGET", nodeId), handler);
        return this;
    }

    @Override
    public RedisClient clusterGetkeysinslot(final long slot, final long count, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(CLUSTER, toPayload("GETKEYSINSLOT", slot, count), handler);
        return this;
    }

    @Override
    public RedisClient clusterInfo(final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(CLUSTER, toPayload("INFO"), handler);
        return this;
    }

    @Override
    public RedisClient clusterKeyslot(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(CLUSTER, toPayload("KEYSLOT", key), handler);
        return this;
    }

    @Override
    public RedisClient clusterMeet(final String ip, final long port, final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("MEET", ip, port), handler);
        return this;
    }

    @Override
    public RedisClient clusterNodes(final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(CLUSTER, toPayload("NODES"), handler);
        return this;
    }

    @Override
    public RedisClient clusterReplicate(final String nodeId, final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("REPLICATE", nodeId), handler);
        return this;
    }

    @Override
    public RedisClient clusterReset(final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("RESET"), handler);
        return this;
    }

    @Override
    public RedisClient clusterResetWithOptions(final ResetOptions options, final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("RESET", options), handler);
        return this;
    }

    @Override
    public RedisClient clusterSaveconfig(final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("SAVECONFIG"), handler);
        return this;
    }

    @Override
    public RedisClient clusterSetConfigEpoch(final long epoch, final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("SET-CONFIG-EPOCH", epoch), handler);
        return this;
    }

    @Override
    public RedisClient clusterSetslot(final long slot, final SlotCmd subcommand, final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("SETSLOT", slot, subcommand), handler);
        return this;
    }

    @Override
    public RedisClient clusterSetslotWithNode(final long slot, final SlotCmd subcommand, final String nodeId, final Handler<AsyncResult<Void>> handler) {
        sendVoid(CLUSTER, toPayload("SETSLOT", slot, subcommand, nodeId), handler);
        return this;
    }

    @Override
    public RedisClient clusterSlaves(final String nodeId, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(CLUSTER, toPayload("SLAVES", nodeId), handler);
        return this;
    }

    @Override
    public RedisClient clusterSlots(final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(CLUSTER, toPayload("SLOTS"), handler);
        return this;
    }

    @Override
    public RedisClient command(final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(COMMAND, null, handler);
        return this;
    }

    @Override
    public RedisClient commandCount(final Handler<AsyncResult<Long>> handler) {
        sendLong(COMMAND, toPayload("COUNT"), handler);
        return this;
    }

    @Override
    public RedisClient commandGetkeys(final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(COMMAND, toPayload("GETKEYS"), handler);
        return this;
    }

    @Override
    public RedisClient commandInfo(final List<String> commands, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(COMMAND, toPayload("INFO", commands), handler);
        return this;
    }

    @Override
    public RedisClient configGet(final String parameter, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(CONFIG, toPayload("GET", parameter), handler);
        return this;
    }

    @Override
    public RedisClient configRewrite(final Handler<AsyncResult<String>> handler) {
        sendString(CONFIG, toPayload("REWRITE"), handler);
        return this;
    }

    @Override
    public RedisClient configSet(final String parameter, final String value, final Handler<AsyncResult<String>> handler) {
        sendString(CONFIG, toPayload("SET", parameter, value), handler);
        return this;
    }

    @Override
    public RedisClient configResetstat(final Handler<AsyncResult<String>> handler) {
        sendString(CONFIG, toPayload("RESETSTAT"), handler);
        return this;
    }

    @Override
    public RedisClient dbsize(final Handler<AsyncResult<Long>> handler) {
        sendLong(DBSIZE, null, handler);
        return this;
    }

    @Override
    public RedisClient debugObject(final String key, final Handler<AsyncResult<String>> handler) {
        sendString(DEBUG, toPayload("OBJECT", key), handler);
        return this;
    }

    @Override
    public RedisClient debugSegfault(final Handler<AsyncResult<String>> handler) {
        sendString(DEBUG, toPayload("SEGFAULT"), handler);
        return this;
    }

    @Override
    public RedisClient decr(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(DECR, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient decrby(final String key, final long decrement, final Handler<AsyncResult<Long>> handler) {
        sendLong(DECRBY, toPayload(key, decrement), handler);
        return this;
    }

    @Override
    public RedisClient del(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(DEL, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient delMany(final List<String> keys, final Handler<AsyncResult<Long>> handler) {
        sendLong(DEL, toPayload(keys), handler);
        return this;
    }

    @Override
    public RedisClient dump(final String key, final Handler<AsyncResult<String>> handler) {
        sendString(DUMP, toPayload(key), dump -> {
            if (dump.failed()) {
                handler.handle(dump);
            } else {
                handler.handle(Future.succeededFuture(RedisEncoding.encode(dump.result())));
            }
        });
        return this;
    }

    @Override
    public RedisClient echo(final String message, final Handler<AsyncResult<String>> handler) {
        sendString(ECHO, toPayload(message), handler);
        return this;
    }

    @Override
    public RedisClient eval(final String script, List<String> keys, List<String> args, final Handler<AsyncResult<JsonArray>> handler) {
        keys = (keys != null) ? keys : Collections.emptyList();
        args = (args != null) ? args : Collections.emptyList();
        sendJsonArray(EVAL, toPayload(script, keys.size(), keys, args), handler);
        return this;
    }

    @Override
    public RedisClient evalsha(final String sha1, List<String> keys, List<String> args, final Handler<AsyncResult<JsonArray>> handler) {
        keys = (keys != null) ? keys : Collections.emptyList();
        args = (args != null) ? args : Collections.emptyList();
        sendJsonArray(EVALSHA, toPayload(sha1, keys.size(), keys, args), handler);
        return this;
    }

    @Override
    public RedisClient evalScript(final Script script, final List<String> keys, final List<String> args, final Handler<AsyncResult<JsonArray>> handler) {
        evalsha(script.getSha1(), keys, args, res -> {
            if (res.failed() && res.cause().getMessage().startsWith("NOSCRIPT")) {
                eval(script.getScript(), keys, args, handler);
            } else {
                handler.handle(res);
            }
        });
        return this;
    }

    @Override
    public RedisClient exists(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(EXISTS, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient existsMany(final List<String> keys, final Handler<AsyncResult<Long>> handler) {
        sendLong(EXISTS, toPayload(keys), handler);
        return this;
    }

    @Override
    public RedisClient expire(final String key, final long seconds, final Handler<AsyncResult<Long>> handler) {
        sendLong(EXPIRE, toPayload(key, seconds), handler);
        return this;
    }

    @Override
    public RedisClient expireat(final String key, final long seconds, final Handler<AsyncResult<Long>> handler) {
        sendLong(EXPIREAT, toPayload(key, seconds), handler);
        return this;
    }

    @Override
    public RedisClient flushall(final Handler<AsyncResult<String>> handler) {
        sendString(FLUSHALL, null, handler);
        return this;
    }

    @Override
    public RedisClient flushdb(final Handler<AsyncResult<String>> handler) {
        sendString(FLUSHDB, null, handler);
        return this;
    }

    @Override
    public RedisClient get(final String key, final Handler<AsyncResult<String>> handler) {
        sendString(GET, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient getBinary(final String key, final Handler<AsyncResult<Buffer>> handler) {
        sendBuffer(GET, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient getbit(final String key, final long offset, final Handler<AsyncResult<Long>> handler) {
        sendLong(GETBIT, toPayload(key, offset), handler);
        return this;
    }

    @Override
    public RedisClient getrange(final String key, final long start, final long end, final Handler<AsyncResult<String>> handler) {
        sendString(GETRANGE, toPayload(key, start, end), handler);
        return this;
    }

    @Override
    public RedisClient getset(final String key, final String value, final Handler<AsyncResult<String>> handler) {
        sendString(GETSET, toPayload(key, value), handler);
        return this;
    }

    @Override
    public RedisClient hdel(final String key, final String field, final Handler<AsyncResult<Long>> handler) {
        sendLong(HDEL, toPayload(key, field), handler);
        return this;
    }

    @Override
    public RedisClient hdelMany(final String key, final List<String> fields, final Handler<AsyncResult<Long>> handler) {
        sendLong(HDEL, toPayload(key, fields), handler);
        return this;
    }

    @Override
    public RedisClient hexists(final String key, final String field, final Handler<AsyncResult<Long>> handler) {
        sendLong(HEXISTS, toPayload(key, field), handler);
        return this;
    }

    @Override
    public RedisClient hget(final String key, final String field, final Handler<AsyncResult<String>> handler) {
        sendString(HGET, toPayload(key, field), handler);
        return this;
    }

    @Override
    public RedisClient hgetall(final String key, final Handler<AsyncResult<JsonObject>> handler) {
        sendJsonObject(HGETALL, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient hincrby(final String key, final String field, final long increment, final Handler<AsyncResult<Long>> handler) {
        sendLong(HINCRBY, toPayload(key, field, increment), handler);
        return this;
    }

    @Override
    public RedisClient hincrbyfloat(final String key, final String field, final double increment, final Handler<AsyncResult<String>> handler) {
        sendString(HINCRBYFLOAT, toPayload(key, field, increment), handler);
        return this;
    }

    @Override
    public RedisClient hkeys(final String key, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(HKEYS, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient hlen(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(HLEN, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient hmget(final String key, final List<String> fields, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(HMGET, toPayload(key, fields), handler);
        return this;
    }

    @Override
    public RedisClient hmset(final String key, final JsonObject values, final Handler<AsyncResult<String>> handler) {
        sendString(HMSET, toPayload(key, values), handler);
        return this;
    }

    @Override
    public RedisClient hset(final String key, final String field, final String value, final Handler<AsyncResult<Long>> handler) {
        sendLong(HSET, toPayload(key, field, value), handler);
        return this;
    }

    @Override
    public RedisClient hsetnx(final String key, final String field, final String value, final Handler<AsyncResult<Long>> handler) {
        sendLong(HSETNX, toPayload(key, field, value), handler);
        return this;
    }

    @Override
    public RedisClient hvals(final String key, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(HVALS, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient incr(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(INCR, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient incrby(final String key, final long increment, final Handler<AsyncResult<Long>> handler) {
        sendLong(INCRBY, toPayload(key, increment), handler);
        return this;
    }

    @Override
    public RedisClient incrbyfloat(final String key, final double increment, final Handler<AsyncResult<String>> handler) {
        sendString(INCRBYFLOAT, toPayload(key, increment), handler);
        return this;
    }

    @Override
    public RedisClient info(final Handler<AsyncResult<JsonObject>> handler) {
        sendString(INFO, Collections.emptyList(), info -> {
            if (info.failed()) {
                handler.handle(Future.failedFuture(info.cause()));
            } else {
                final JsonObject result = new JsonObject();
                JsonObject section = result;
                for (final String line : info.result().split("\r?\n")) {
                    if (line.length() > 0) {
                        if (line.charAt(0) == '#') {
                            section = new JsonObject();
                            result.put(line.substring(2).toLowerCase(), section);
                        } else {
                            final int sep = line.indexOf(':');
                            section.put(line.substring(0, sep), line.substring(sep + 1));
                        }
                    }
                }
                handler.handle(Future.succeededFuture(result));
            }
        });
        return this;
    }

    @Override
    public RedisClient infoSection(final String section, final Handler<AsyncResult<JsonObject>> handler) {
        sendString(INFO, toPayload(section), info -> {
            if (info.failed()) {
                handler.handle(Future.failedFuture(info.cause()));
            } else {
                final JsonObject result = new JsonObject();
                JsonObject sectionJson = result;
                for (final String line : info.result().split("\r?\n")) {
                    if (line.length() > 0) {
                        if (line.charAt(0) == '#') {
                            sectionJson = new JsonObject();
                            result.put(line.substring(2).toLowerCase(), sectionJson);
                        } else {
                            final int sep = line.indexOf(':');
                            sectionJson.put(line.substring(0, sep), line.substring(sep + 1));
                        }
                    }
                }
                handler.handle(Future.succeededFuture(result));
            }
        });
        return this;
    }

    @Override
    public RedisClient keys(final String pattern, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(KEYS, toPayload(pattern), handler);
        return this;
    }

    @Override
    public RedisClient lastsave(final Handler<AsyncResult<Long>> handler) {
        sendLong(LASTSAVE, null, handler);
        return this;
    }

    @Override
    public RedisClient lindex(final String key, final int index, final Handler<AsyncResult<String>> handler) {
        sendString(LINDEX, toPayload(key, index), handler);
        return this;
    }

    @Override
    public RedisClient linsert(final String key, final InsertOptions option, final String pivot, final String value, final Handler<AsyncResult<Long>> handler) {
        sendLong(LINSERT, toPayload(key, option.name(), pivot, value), handler);
        return this;
    }

    @Override
    public RedisClient llen(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(LLEN, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient lpop(final String key, final Handler<AsyncResult<String>> handler) {
        sendString(LPOP, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient lpushMany(final String key, final List<String> values, final Handler<AsyncResult<Long>> handler) {
        sendLong(LPUSH, toPayload(key, values), handler);
        return this;
    }

    @Override
    public RedisClient lpush(final String key, final String value, final Handler<AsyncResult<Long>> handler) {
        sendLong(LPUSH, toPayload(key, value), handler);
        return this;
    }

    @Override
    public RedisClient lpushx(final String key, final String value, final Handler<AsyncResult<Long>> handler) {
        sendLong(LPUSHX, toPayload(key, value), handler);
        return this;
    }

    @Override
    public RedisClient lrange(final String key, final long from, final long to, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(LRANGE, toPayload(key, from, to), handler);
        return this;
    }

    @Override
    public RedisClient lrem(final String key, final long count, final String value, final Handler<AsyncResult<Long>> handler) {
        sendLong(LREM, toPayload(key, count, value), handler);
        return this;
    }

    @Override
    public RedisClient lset(final String key, final long index, final String value, final Handler<AsyncResult<String>> handler) {
        sendString(LSET, toPayload(key, index, value), handler);
        return this;
    }

    @Override
    public RedisClient ltrim(final String key, final long from, final long to, final Handler<AsyncResult<String>> handler) {
        sendString(LTRIM, toPayload(key, from, to), handler);
        return this;
    }

    @Override
    public RedisClient mget(final String key, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(MGET, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient mgetMany(final List<String> keys, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(MGET, toPayload(keys), handler);
        return this;
    }

    @Override
    public RedisClient migrate(final String host, final int port, final String key, final int destdb, final long timeout, final MigrateOptions options, final Handler<AsyncResult<String>> handler) {
        sendString(MIGRATE, toPayload(host, port, key, destdb, timeout, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient monitor(final Handler<AsyncResult<Void>> handler) {
        sendVoid(MONITOR, null, handler);
        return this;
    }

    @Override
    public RedisClient move(final String key, final int destdb, final Handler<AsyncResult<Long>> handler) {
        sendLong(MOVE, toPayload(key, destdb), handler);
        return this;
    }

    @Override
    public RedisClient mset(final JsonObject keyvals, final Handler<AsyncResult<String>> handler) {
        sendString(MSET, toPayload(keyvals), handler);
        return this;
    }

    @Override
    public RedisClient msetnx(final JsonObject keyvals, final Handler<AsyncResult<Long>> handler) {
        sendLong(MSETNX, toPayload(keyvals), handler);
        return this;
    }

    @Override
    public RedisClient object(final String key, final ObjectCmd cmd, final Handler<AsyncResult<Void>> handler) {
        sendVoid(OBJECT, toPayload(cmd.name(), key), handler);
        return this;
    }

    @Override
    public RedisClient persist(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(PERSIST, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient pexpire(final String key, final long millis, final Handler<AsyncResult<Long>> handler) {
        sendLong(PEXPIRE, toPayload(key, millis), handler);
        return this;
    }

    @Override
    public RedisClient pexpireat(final String key, final long millis, final Handler<AsyncResult<Long>> handler) {
        sendLong(PEXPIREAT, toPayload(key, millis), handler);
        return this;
    }

    @Override
    public RedisClient pfadd(final String key, final String element, final Handler<AsyncResult<Long>> handler) {
        sendLong(PFADD, toPayload(key, element), handler);
        return this;
    }

    @Override
    public RedisClient pfaddMany(final String key, final List<String> elements, final Handler<AsyncResult<Long>> handler) {
        sendLong(PFADD, toPayload(key, elements), handler);
        return this;
    }

    @Override
    public RedisClient pfcount(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(PFCOUNT, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient pfcountMany(final List<String> key, final Handler<AsyncResult<Long>> handler) {
        sendLong(PFCOUNT, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient pfmerge(final String destkey, final List<String> keys, final Handler<AsyncResult<String>> handler) {
        sendString(PFMERGE, toPayload(destkey, keys), handler);
        return this;
    }

    @Override
    public RedisClient ping(final Handler<AsyncResult<String>> handler) {
        sendString(PING, null, handler);
        return this;
    }

    @Override
    public RedisClient psetex(final String key, final long millis, final String value, final Handler<AsyncResult<Void>> handler) {
        sendVoid(PSETEX, toPayload(key, millis, value), handler);
        return this;
    }

    @Override
    public RedisClient psubscribe(final String pattern, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(PSUBSCRIBE, toPayload(pattern), handler);
        return this;
    }

    @Override
    public RedisClient psubscribeMany(final List<String> patterns, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(PSUBSCRIBE, toPayload(patterns), handler);
        return this;
    }

    @Override
    public RedisClient pubsubChannels(final String pattern, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(PUBSUB, toPayload("CHANNELS", pattern == null || "".equals(pattern) ? null : pattern), handler);
        return this;
    }

    @Override
    public RedisClient pubsubNumsub(final List<String> channels, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(PUBSUB, toPayload("NUMSUB", channels), handler);
        return this;
    }

    @Override
    public RedisClient pubsubNumpat(final Handler<AsyncResult<Long>> handler) {
        sendLong(PUBSUB, toPayload("NUMPAT"), handler);
        return this;
    }

    @Override
    public RedisClient pttl(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(PTTL, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient publish(final String channel, final String message, final Handler<AsyncResult<Long>> handler) {
        sendLong(PUBLISH, toPayload(channel, message), handler);
        return this;
    }

    @Override
    public RedisClient punsubscribe(final List<String> patterns, final Handler<AsyncResult<Void>> handler) {
        sendVoid(PUNSUBSCRIBE, toPayload(patterns), handler);
        return this;
    }


    @Override
    public RedisClient randomkey(final Handler<AsyncResult<String>> handler) {
        sendString(RANDOMKEY, null, handler);
        return this;
    }

    @Override
    public RedisClient rename(final String key, final String newkey, final Handler<AsyncResult<String>> handler) {
        sendString(RENAME, toPayload(key, newkey), handler);
        return this;
    }

    @Override
    public RedisClient renamenx(final String key, final String newkey, final Handler<AsyncResult<Long>> handler) {
        sendLong(RENAMENX, toPayload(key, newkey), handler);
        return this;
    }

    @Override
    public RedisClient restore(final String key, final long millis, final String serialized, final Handler<AsyncResult<String>> handler) {
        sendString(RESTORE, toPayload(key, millis, RedisEncoding.decode(serialized)), handler);
        return this;
    }

    @Override
    public RedisClient role(final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(ROLE, null, handler);
        return this;
    }

    @Override
    public RedisClient rpop(final String key, final Handler<AsyncResult<String>> handler) {
        sendString(RPOP, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient rpoplpush(final String key, final String destkey, final Handler<AsyncResult<String>> handler) {
        sendString(RPOPLPUSH, toPayload(key, destkey), handler);
        return this;
    }

    @Override
    public RedisClient rpushMany(final String key, final List<String> values, final Handler<AsyncResult<Long>> handler) {
        sendLong(RPUSH, toPayload(key, values), handler);
        return this;
    }

    @Override
    public RedisClient rpush(final String key, final String value, final Handler<AsyncResult<Long>> handler) {
        sendLong(RPUSH, toPayload(key, value), handler);
        return this;
    }

    @Override
    public RedisClient rpushx(final String key, final String value, final Handler<AsyncResult<Long>> handler) {
        sendLong(RPUSHX, toPayload(key, value), handler);
        return this;
    }

    @Override
    public RedisClient sadd(final String key, final String member, final Handler<AsyncResult<Long>> handler) {
        sendLong(SADD, toPayload(key, member), handler);
        return this;
    }

    @Override
    public RedisClient saddMany(final String key, final List<String> members, final Handler<AsyncResult<Long>> handler) {
        sendLong(SADD, toPayload(key, members), handler);
        return this;
    }

    @Override
    public RedisClient save(final Handler<AsyncResult<String>> handler) {
        sendString(SAVE, null, handler);
        return this;
    }

    @Override
    public RedisClient scard(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(SCARD, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient scriptExists(final String script, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SCRIPT, toPayload("EXISTS", script), handler);
        return this;
    }

    @Override
    public RedisClient scriptExistsMany(final List<String> scripts, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SCRIPT, toPayload("EXISTS", scripts), handler);
        return this;
    }

    @Override
    public RedisClient scriptFlush(final Handler<AsyncResult<String>> handler) {
        sendString(SCRIPT, toPayload("FLUSH"), handler);
        return this;
    }

    @Override
    public RedisClient scriptKill(final Handler<AsyncResult<String>> handler) {
        sendString(SCRIPT, toPayload("KILL"), handler);
        return this;
    }

    @Override
    public RedisClient scriptLoad(final String script, final Handler<AsyncResult<String>> handler) {
        sendString(SCRIPT, toPayload("LOAD", script), handler);
        return this;
    }

    @Override
    public RedisClient sdiff(final String key, final List<String> cmpkeys, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SDIFF, toPayload(key, cmpkeys), handler);
        return this;
    }

    @Override
    public RedisClient sdiffstore(final String destkey, final String key, final List<String> cmpkeys, final Handler<AsyncResult<Long>> handler) {
        sendLong(SDIFFSTORE, toPayload(destkey, key, cmpkeys), handler);
        return this;
    }

    @Override
    public RedisClient select(final int dbindex, final Handler<AsyncResult<String>> handler) {
        sendString(SELECT, toPayload(dbindex), handler);
        return this;
    }

    @Override
    public RedisClient set(final String key, final String value, final Handler<AsyncResult<Void>> handler) {
        sendVoid(SET, toPayload(key, value), handler);
        return this;
    }

    @Override
    public RedisClient setWithOptions(final String key, final String value, final SetOptions options, final Handler<AsyncResult<String>> handler) {
        sendString(SET, toPayload(key, value, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient setBinary(final String key, final Buffer value, final Handler<AsyncResult<Void>> handler) {
        sendVoid(SET, toPayload(key, value), handler);
        return this;
    }

    @Override
    public RedisClient setBinaryWithOptions(final String key, final Buffer value, final SetOptions options, final Handler<AsyncResult<Void>> handler) {
        sendVoid(SET, toPayload(key, value, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient setbit(final String key, final long offset, final int bit, final Handler<AsyncResult<Long>> handler) {
        sendLong(SETBIT, toPayload(key, offset, bit), handler);
        return this;
    }

    @Override
    public RedisClient setex(final String key, final long seconds, final String value, final Handler<AsyncResult<String>> handler) {
        sendString(SETEX, toPayload(key, seconds, value), handler);
        return this;
    }

    @Override
    public RedisClient setnx(final String key, final String value, final Handler<AsyncResult<Long>> handler) {
        sendLong(SETNX, toPayload(key, value), handler);
        return this;
    }

    @Override
    public RedisClient setrange(final String key, final int offset, final String value, final Handler<AsyncResult<Long>> handler) {
        sendLong(SETRANGE, toPayload(key, offset, value), handler);
        return this;
    }

    @Override
    public RedisClient sinter(final List<String> keys, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SINTER, toPayload(keys), handler);
        return this;
    }

    @Override
    public RedisClient sinterstore(final String destkey, final List<String> keys, final Handler<AsyncResult<Long>> handler) {
        sendLong(SINTERSTORE, toPayload(destkey, keys), handler);
        return this;
    }

    @Override
    public RedisClient sismember(final String key, final String member, final Handler<AsyncResult<Long>> handler) {
        sendLong(SISMEMBER, toPayload(key, member), handler);
        return this;
    }

    @Override
    public RedisClient slaveof(final String host, final int port, final Handler<AsyncResult<String>> handler) {
        sendString(SLAVEOF, toPayload(host, port), handler);
        return this;
    }

    @Override
    public RedisClient slaveofNoone(final Handler<AsyncResult<String>> handler) {
        sendString(SLAVEOF, toPayload("NO", "ONE"), handler);
        return this;
    }

    @Override
    public RedisClient slowlogGet(final int limit, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SLOWLOG, toPayload("GET", limit < 0 ? null : limit), handler);
        return this;
    }

    @Override
    public RedisClient slowlogLen(final Handler<AsyncResult<Long>> handler) {
        sendLong(SLOWLOG, toPayload("LEN"), handler);
        return this;
    }

    @Override
    public RedisClient slowlogReset(final Handler<AsyncResult<Void>> handler) {
        sendVoid(SLOWLOG, toPayload("RESET"), handler);
        return this;
    }

    @Override
    public RedisClient smembers(final String key, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SMEMBERS, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient smove(final String key, final String destkey, final String member, final Handler<AsyncResult<Long>> handler) {
        sendLong(SMOVE, toPayload(key, destkey, member), handler);
        return this;
    }

    @Override
    public RedisClient sort(final String key, final SortOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SORT, toPayload(key, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient spop(final String key, final Handler<AsyncResult<String>> handler) {
        sendString(SPOP, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient spopMany(final String key, final int count, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SPOP, toPayload(key, count), handler);
        return this;
    }

    @Override
    public RedisClient srandmember(final String key, final Handler<AsyncResult<String>> handler) {
        sendString(SRANDMEMBER, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient srandmemberCount(final String key, final int count, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SRANDMEMBER, toPayload(key, count), handler);
        return this;
    }

    @Override
    public RedisClient srem(final String key, final String member, final Handler<AsyncResult<Long>> handler) {
        sendLong(SREM, toPayload(key, member), handler);
        return this;
    }

    @Override
    public RedisClient sremMany(final String key, final List<String> members, final Handler<AsyncResult<Long>> handler) {
        sendLong(SREM, toPayload(key, members), handler);
        return this;
    }

    @Override
    public RedisClient strlen(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(STRLEN, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient subscribe(final String channel, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SUBSCRIBE, toPayload(channel), handler);
        return this;
    }

    @Override
    public RedisClient subscribeMany(final List<String> channels, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SUBSCRIBE, toPayload(channels), handler);
        return this;
    }

    @Override
    public RedisClient sunion(final List<String> keys, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SUNION, toPayload(keys), handler);
        return this;
    }

    @Override
    public RedisClient sunionstore(final String destkey, final List<String> keys, final Handler<AsyncResult<Long>> handler) {
        sendLong(SUNIONSTORE, toPayload(destkey, keys), handler);
        return this;
    }

    @Override
    public RedisClient sync(final Handler<AsyncResult<Void>> handler) {
        sendVoid(SYNC, null, handler);
        return this;
    }

    @Override
    public RedisClient time(final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(TIME, null, handler);
        return this;
    }

    @Override
    public RedisTransaction transaction() {
        return new RedisClientStub.RedisTransactionImpl();
    }


    @Override
    public RedisClient ttl(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(TTL, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient type(final String key, final Handler<AsyncResult<String>> handler) {
        sendString(TYPE, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient unsubscribe(final List<String> channels, final Handler<AsyncResult<Void>> handler) {
        sendVoid(UNSUBSCRIBE, toPayload(channels), handler);
        return this;
    }

    @Override
    public RedisClient wait(final long numSlaves, final long timeout, final Handler<AsyncResult<String>> handler) {
        sendString(WAIT, toPayload(numSlaves, timeout), handler);
        return this;
    }

    @Override
    public RedisClient zadd(final String key, final double score, final String member, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZADD, toPayload(key, score, member), handler);
        return this;
    }

    @Override
    public RedisClient zaddMany(final String key, final Map<String, Double> members, final Handler<AsyncResult<Long>> handler) {
        // flip from <String, Double> to <Double, String> when wrapping
        final Stream flipped = members.entrySet().stream().map(e -> new Object[]{e.getValue(), e.getKey()});
        sendLong(ZADD, toPayload(key, flipped), handler);
        return this;
    }

    @Override
    public RedisClient zcard(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZCARD, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient zcount(final String key, final double min, final double max, final Handler<AsyncResult<Long>> handler) {
        final String minVal = (min == Double.NEGATIVE_INFINITY) ? "-inf" : String.valueOf(min);
        final String maxVal = (max == Double.POSITIVE_INFINITY) ? "+inf" : String.valueOf(max);
        sendLong(ZCOUNT, toPayload(key, minVal, maxVal), handler);
        return this;
    }

    @Override
    public RedisClient zincrby(final String key, final double increment, final String member, final Handler<AsyncResult<String>> handler) {
        sendString(ZINCRBY, toPayload(key, increment, member), handler);
        return this;
    }

    @Override
    public RedisClient zinterstore(final String destkey, final List<String> sets, final AggregateOptions options, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZINTERSTORE, toPayload(destkey, sets.size(), sets, options != null ? options.name() : null), handler);
        return this;
    }

    @Override
    public RedisClient zinterstoreWeighed(final String destkey, final Map<String, Double> sets, final AggregateOptions options, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZINTERSTORE, toPayload(destkey, sets.size(), sets.keySet(), "WEIGHTS", sets.values(),
                options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient zlexcount(final String key, final String min, final String max, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZLEXCOUNT, toPayload(key, min, max), handler);
        return this;
    }

    @Override
    public RedisClient zrange(final String key, final long start, final long stop, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(ZRANGE, toPayload(key, start, stop), handler);
        return this;
    }

    @Override
    public RedisClient zrangeWithOptions(final String key, final long start, final long stop, final RangeOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(ZRANGE, toPayload(key, start, stop, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient zrangebylex(final String key, final String min, final String max, final LimitOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(ZRANGEBYLEX, toPayload(key, min, max, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient zrangebyscore(final String key, final String min, final String max, final RangeLimitOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(ZRANGEBYSCORE, toPayload(key, min, max, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient zrank(final String key, final String member, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZRANK, toPayload(key, member), handler);
        return this;
    }

    @Override
    public RedisClient zrem(final String key, final String member, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZREM, toPayload(key, member), handler);
        return this;
    }

    @Override
    public RedisClient zremMany(final String key, final List<String> members, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZREM, toPayload(key, members), handler);
        return this;
    }

    @Override
    public RedisClient zremrangebylex(final String key, final String min, final String max, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZREMRANGEBYLEX, toPayload(key, min, max), handler);
        return this;
    }

    @Override
    public RedisClient zremrangebyrank(final String key, final long start, final long stop, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZREMRANGEBYRANK, toPayload(key, start, stop), handler);
        return this;
    }

    @Override
    public RedisClient zremrangebyscore(final String key, final String min, final String max, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZREMRANGEBYSCORE, toPayload(key, min, max), handler);
        return this;
    }

    @Override
    public RedisClient zrevrange(final String key, final long start, final long stop, final RangeOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(ZREVRANGE, toPayload(key, start, stop, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient zrevrangebylex(final String key, final String max, final String min, final LimitOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(ZREVRANGEBYLEX, toPayload(key, max, min, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient zrevrangebyscore(final String key, final String max, final String min, final RangeLimitOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(ZREVRANGEBYSCORE, toPayload(key, max, min, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient zrevrank(final String key, final String member, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZREVRANK, toPayload(key, member), handler);
        return this;
    }

    @Override
    public RedisClient zscore(final String key, final String member, final Handler<AsyncResult<String>> handler) {
        sendString(ZSCORE, toPayload(key, member), handler);
        return this;
    }

    @Override
    public RedisClient zunionstore(final String destkey, final List<String> sets, final AggregateOptions options, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZUNIONSTORE, toPayload(destkey, sets.size(), sets, options != null ? options.name() : null), handler);
        return this;
    }

    @Override
    public RedisClient zunionstoreWeighed(final String destkey, final Map<String, Double> sets, final AggregateOptions options, final Handler<AsyncResult<Long>> handler) {
        sendLong(ZUNIONSTORE, toPayload(destkey, sets.size(), sets.keySet(), "WEIGHTS", sets.values(),
                options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient scan(final String cursor, final ScanOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SCAN, toPayload(cursor, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient sscan(final String key, final String cursor, final ScanOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(SSCAN, toPayload(key, cursor, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient hscan(final String key, final String cursor, final ScanOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(HSCAN, toPayload(key, cursor, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient zscan(final String key, final String cursor, final ScanOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(ZSCAN, toPayload(key, cursor, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient geoadd(final String key, final double longitude, final double latitude, final String member, final Handler<AsyncResult<Long>> handler) {
        sendLong(GEOADD, toPayload(key, longitude, latitude, member), handler);
        return this;
    }

    @Override
    public RedisClient geoaddMany(final String key, final List<GeoMember> members, final Handler<AsyncResult<Long>> handler) {
        sendLong(GEOADD, toPayload(key, members), handler);
        return this;
    }

    @Override
    public RedisClient geohash(final String key, final String member, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(GEOHASH, toPayload(key, member), handler);
        return this;
    }

    @Override
    public RedisClient geohashMany(final String key, final List<String> members, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(GEOHASH, toPayload(key, members), handler);
        return this;
    }

    @Override
    public RedisClient geopos(final String key, final String member, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(GEOPOS, toPayload(key, member), handler);
        return this;
    }

    @Override
    public RedisClient geoposMany(final String key, final List<String> members, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(GEOPOS, toPayload(key, members), handler);
        return this;
    }

    @Override
    public RedisClient geodist(final String key, final String member1, final String member2, final Handler<AsyncResult<String>> handler) {
        sendString(GEODIST, toPayload(key, member1, member2), handler);
        return this;
    }

    @Override
    public RedisClient geodistWithUnit(final String key, final String member1, final String member2, final GeoUnit unit, final Handler<AsyncResult<String>> handler) {
        sendString(GEODIST, toPayload(key, member1, member2, unit), handler);
        return this;
    }

    @Override
    public RedisClient georadius(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(GEORADIUS, toPayload(key, longitude, latitude, radius, unit), handler);
        return this;
    }

    @Override
    public RedisClient georadiusWithOptions(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit, final GeoRadiusOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(GEORADIUS, toPayload(key, longitude, latitude, radius, unit, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient georadiusbymember(final String key, final String member, final double radius, final GeoUnit unit, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(GEORADIUSBYMEMBER, toPayload(key, member, radius, unit), handler);
        return this;
    }

    @Override
    public RedisClient georadiusbymemberWithOptions(final String key, final String member, final double radius, final GeoUnit unit, final GeoRadiusOptions options, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(GEORADIUSBYMEMBER, toPayload(key, member, radius, unit, options != null ? options.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient clientReply(final ClientReplyOptions options, final Handler<AsyncResult<String>> handler) {
        sendString(CLIENT, toPayload("REPLY", options), handler);
        return this;
    }

    @Override
    public RedisClient hstrlen(final String key, final String field, final Handler<AsyncResult<Long>> handler) {
        sendLong(HSTRLEN, toPayload(key, field), handler);
        return this;
    }

    @Override
    public RedisClient touch(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(TOUCH, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient touchMany(final List<String> keys, final Handler<AsyncResult<Long>> handler) {
        sendLong(TOUCH, keys, handler);
        return this;
    }

    @Override
    public RedisClient scriptDebug(final ScriptDebugOptions scriptDebugOptions, final Handler<AsyncResult<String>> handler) {
        sendString(SCRIPT, toPayload("DEBUG", scriptDebugOptions), handler);
        return this;
    }

    @Override
    public RedisClient bitfield(final String key, final BitFieldOptions bitFieldOptions, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(BITFIELD, toPayload(key, bitFieldOptions != null ? bitFieldOptions.toJsonArray() : null), handler);
        return this;
    }

    @Override
    public RedisClient bitfieldWithOverflow(final String key, final BitFieldOptions bitFieldOptions, final BitFieldOverflowOptions overflow, final Handler<AsyncResult<JsonArray>> handler) {
        sendJsonArray(BITFIELD, toPayload(key, bitFieldOptions != null ? bitFieldOptions.toJsonArray() : null, overflow), handler);
        return this;
    }

    @Override
    public RedisClient unlink(final String key, final Handler<AsyncResult<Long>> handler) {
        sendLong(UNLINK, toPayload(key), handler);
        return this;
    }

    @Override
    public RedisClient unlinkMany(final List<String> keys, final Handler<AsyncResult<Long>> handler) {
        sendLong(UNLINK, toPayload(keys), handler);
        return this;
    }

    @Override
    public RedisClient swapdb(final int index1, final int index2, final Handler<AsyncResult<String>> handler) {
        sendString(SWAPDB, toPayload(index1, index2), handler);
        return this;
    }

    public class RedisTransactionImpl implements RedisTransaction {

        @Override
        public void close(final Handler<AsyncResult<Void>> handler) {
            RedisClientStub.this.close(handler);
        }

        @Override
        public RedisTransaction append(final String key, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(APPEND, toPayload(key, value), handler);
            return this;
        }

        @Override
        public RedisTransaction auth(final String password, final Handler<AsyncResult<String>> handler) {
            sendString(AUTH, toPayload(password), handler);
            return this;
        }

        @Override
        public RedisTransaction bgrewriteaof(final Handler<AsyncResult<String>> handler) {
            sendString(BGREWRITEAOF, null, handler);
            return this;
        }

        @Override
        public RedisTransaction bgsave(final Handler<AsyncResult<String>> handler) {
            sendString(BGSAVE, null, handler);
            return this;
        }

        @Override
        public RedisTransaction bitcount(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(BITCOUNT, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction bitcountRange(final String key, final long start, final long end, final Handler<AsyncResult<String>> handler) {
            sendString(BITCOUNT, toPayload(key, start, end), handler);
            return this;
        }

        @Override
        public RedisTransaction bitop(final BitOperation operation, final String destkey, final List<String> keys, final Handler<AsyncResult<String>> handler) {
            sendString(BITOP, toPayload(operation.name(), destkey, keys), handler);
            return this;
        }

        @Override
        public RedisTransaction bitpos(final String key, final int bit, final Handler<AsyncResult<String>> handler) {
            sendString(BITPOS, toPayload(key, bit), handler);
            return this;
        }

        @Override
        public RedisTransaction bitposFrom(final String key, final int bit, final int start, final Handler<AsyncResult<String>> handler) {
            sendString(BITPOS, toPayload(key, bit, start), handler);
            return this;
        }

        @Override
        public RedisTransaction bitposRange(final String key, final int bit, final int start, final int stop, final Handler<AsyncResult<String>> handler) {
            sendString(BITPOS, toPayload(key, bit, start, stop), handler);
            return this;
        }

        @Override
        public RedisTransaction blpop(final String key, final int seconds, final Handler<AsyncResult<String>> handler) {
            sendString(BLPOP, toPayload(key, seconds), handler);
            return this;
        }

        @Override
        public RedisTransaction blpopMany(final List<String> keys, final int seconds, final Handler<AsyncResult<String>> handler) {
            sendString(BLPOP, toPayload(keys, seconds), handler);
            return this;
        }

        @Override
        public RedisTransaction brpop(final String key, final int seconds, final Handler<AsyncResult<String>> handler) {
            sendString(BRPOP, toPayload(key, seconds), handler);
            return this;
        }

        @Override
        public RedisTransaction brpopMany(final List<String> keys, final int seconds, final Handler<AsyncResult<String>> handler) {
            sendString(BRPOP, toPayload(keys, seconds), handler);
            return this;
        }

        @Override
        public RedisTransaction brpoplpush(final String key, final String destkey, final int seconds, final Handler<AsyncResult<String>> handler) {
            sendString(BRPOPLPUSH, toPayload(key, destkey, seconds), handler);
            return this;
        }

        @Override
        public RedisTransaction clientKill(final KillFilter filter, final Handler<AsyncResult<String>> handler) {
            sendString(CLIENT, toPayload("KILL", filter.toJsonArray().getList()), handler);
            return this;
        }

        @Override
        public RedisTransaction clientList(final Handler<AsyncResult<String>> handler) {
            sendString(CLIENT, toPayload("LIST"), handler);
            return this;
        }

        @Override
        public RedisTransaction clientGetname(final Handler<AsyncResult<String>> handler) {
            sendString(CLIENT, toPayload("GETNAME"), handler);
            return this;
        }

        @Override
        public RedisTransaction clientPause(final long millis, final Handler<AsyncResult<String>> handler) {
            sendString(CLIENT, toPayload("PAUSE", millis), handler);
            return this;
        }

        @Override
        public RedisTransaction clientSetname(final String name, final Handler<AsyncResult<String>> handler) {
            sendString(CLIENT, toPayload("SETNAME", name), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterAddslots(final List<String> slots, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("ADDSLOTS"), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterCountFailureReports(final String nodeId, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("COUNT-FAILURE-REPORTS", nodeId), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterCountkeysinslot(final long slot, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("COUNTKEYSINSLOT", slot), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterDelslots(final long slot, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("DELSLOTS", slot), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterDelslotsMany(final List<String> slots, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("DELSLOTS", slots), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterFailover(final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("FAILOVER"), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterFailOverWithOptions(final FailoverOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("FAILOVER", options), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterForget(final String nodeId, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("FORGET", nodeId), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterGetkeysinslot(final long slot, final long count, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("GETKEYSINSLOT", slot, count), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterInfo(final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("INFO"), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterKeyslot(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("KEYSLOT", key), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterMeet(final String ip, final long port, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("MEET", ip, port), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterNodes(final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("NODES"), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterReplicate(final String nodeId, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("REPLICATE", nodeId), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterReset(final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("RESET"), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterResetWithOptions(final ResetOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("RESET", options), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterSaveconfig(final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("SAVECONFIG"), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterSetConfigEpoch(final long epoch, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("SET-CONFIG-EPOCH", epoch), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterSetslot(final long slot, final SlotCmd subcommand, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("SETSLOT", slot, subcommand), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterSetslotWithNode(final long slot, final SlotCmd subcommand, final String nodeId, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("SETSLOT", slot, subcommand, nodeId), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterSlaves(final String nodeId, final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("SLAVES", nodeId), handler);
            return this;
        }

        @Override
        public RedisTransaction clusterSlots(final Handler<AsyncResult<String>> handler) {
            sendString(CLUSTER, toPayload("SLOTS"), handler);
            return this;
        }

        @Override
        public RedisTransaction command(final Handler<AsyncResult<String>> handler) {
            sendString(COMMAND, null, handler);
            return this;
        }

        @Override
        public RedisTransaction commandCount(final Handler<AsyncResult<String>> handler) {
            sendString(COMMAND, toPayload("COUNT"), handler);
            return this;
        }

        @Override
        public RedisTransaction commandGetkeys(final Handler<AsyncResult<String>> handler) {
            sendString(COMMAND, toPayload("GETKEYS"), handler);
            return this;
        }

        @Override
        public RedisTransaction commandInfo(final List<String> commands, final Handler<AsyncResult<String>> handler) {
            sendString(COMMAND, toPayload("INFO", commands), handler);
            return this;
        }

        @Override
        public RedisTransaction configGet(final String parameter, final Handler<AsyncResult<String>> handler) {
            sendString(CONFIG, toPayload("GET", parameter), handler);
            return this;
        }

        @Override
        public RedisTransaction configRewrite(final Handler<AsyncResult<String>> handler) {
            sendString(CONFIG, toPayload("REWRITE"), handler);
            return this;
        }

        @Override
        public RedisTransaction configSet(final String parameter, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(CONFIG, toPayload("SET", parameter, value), handler);
            return this;
        }

        @Override
        public RedisTransaction configResetstat(final Handler<AsyncResult<String>> handler) {
            sendString(CONFIG, toPayload("RESETSTAT"), handler);
            return this;
        }

        @Override
        public RedisTransaction dbsize(final Handler<AsyncResult<String>> handler) {
            sendString(DBSIZE, null, handler);
            return this;
        }

        @Override
        public RedisTransaction debugObject(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(DEBUG, toPayload("OBJECT", key), handler);
            return this;
        }

        @Override
        public RedisTransaction debugSegfault(final Handler<AsyncResult<String>> handler) {
            sendString(DEBUG, toPayload("SEGFAULT"), handler);
            return this;
        }

        @Override
        public RedisTransaction decr(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(DECR, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction decrby(final String key, final long decrement, final Handler<AsyncResult<String>> handler) {
            sendString(DECRBY, toPayload(key, decrement), handler);
            return this;
        }

        @Override
        public RedisTransaction del(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(DEL, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction delMany(final List<String> keys, final Handler<AsyncResult<String>> handler) {
            sendString(DEL, toPayload(keys), handler);
            return this;
        }

        @Override
        public RedisTransaction discard(final Handler<AsyncResult<String>> handler) {
            sendString(DISCARD, null, handler);
            return this;
        }

        @Override
        public RedisTransaction dump(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(DUMP, toPayload(key), dump -> {
                if (dump.failed()) {
                    handler.handle(dump);
                } else {
                    handler.handle(Future.succeededFuture(RedisEncoding.encode(dump.result())));
                }
            });
            return this;
        }

        @Override
        public RedisTransaction echo(final String message, final Handler<AsyncResult<String>> handler) {
            sendString(ECHO, toPayload(message), handler);
            return this;
        }

        @Override
        public RedisTransaction eval(final String script, List<String> keys, List<String> args, final Handler<AsyncResult<String>> handler) {
            keys = (keys != null) ? keys : Collections.emptyList();
            args = (args != null) ? args : Collections.emptyList();
            sendString(EVAL, toPayload(script, keys.size(), keys, args), handler);
            return this;
        }

        @Override
        public RedisTransaction evalsha(final String sha1, List<String> keys, List<String> args, final Handler<AsyncResult<String>> handler) {
            keys = (keys != null) ? keys : Collections.emptyList();
            args = (args != null) ? args : Collections.emptyList();
            sendString(EVALSHA, toPayload(sha1, keys.size(), keys, args), handler);
            return this;
        }

        @Override
        public RedisTransaction exec(final Handler<AsyncResult<JsonArray>> handler) {
            sendJsonArray(EXEC, null, handler);
            return this;
        }

        @Override
        public RedisTransaction exists(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(EXISTS, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction existsMany(final List<String> keys, final Handler<AsyncResult<String>> handler) {
            sendString(EXISTS, toPayload(keys), handler);
            return this;
        }

        @Override
        public RedisTransaction expire(final String key, final int seconds, final Handler<AsyncResult<String>> handler) {
            sendString(EXPIRE, toPayload(key, seconds), handler);
            return this;
        }

        @Override
        public RedisTransaction expireat(final String key, final long seconds, final Handler<AsyncResult<String>> handler) {
            sendString(EXPIREAT, toPayload(key, seconds), handler);
            return this;
        }

        @Override
        public RedisTransaction flushall(final Handler<AsyncResult<String>> handler) {
            sendString(FLUSHALL, null, handler);
            return this;
        }

        @Override
        public RedisTransaction flushdb(final Handler<AsyncResult<String>> handler) {
            sendString(FLUSHDB, null, handler);
            return this;
        }

        @Override
        public RedisTransaction get(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(GET, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction getBinary(final String key, final Handler<AsyncResult<Buffer>> handler) {
            sendBuffer(GET, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction getbit(final String key, final long offset, final Handler<AsyncResult<String>> handler) {
            sendString(GETBIT, toPayload(key, offset), handler);
            return this;
        }

        @Override
        public RedisTransaction getrange(final String key, final long start, final long end, final Handler<AsyncResult<String>> handler) {
            sendString(GETRANGE, toPayload(key, start, end), handler);
            return this;
        }

        @Override
        public RedisTransaction getset(final String key, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(GETSET, toPayload(key, value), handler);
            return this;
        }

        @Override
        public RedisTransaction hdel(final String key, final String field, final Handler<AsyncResult<String>> handler) {
            sendString(HDEL, toPayload(key, field), handler);
            return this;
        }

        @Override
        public RedisTransaction hdelMany(final String key, final List<String> fields, final Handler<AsyncResult<String>> handler) {
            sendString(HDEL, toPayload(key, fields), handler);
            return this;
        }

        @Override
        public RedisTransaction hexists(final String key, final String field, final Handler<AsyncResult<String>> handler) {
            sendString(HEXISTS, toPayload(key, field), handler);
            return this;
        }

        @Override
        public RedisTransaction hget(final String key, final String field, final Handler<AsyncResult<String>> handler) {
            sendString(HGET, toPayload(key, field), handler);
            return this;
        }

        @Override
        public RedisTransaction hgetall(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(HGETALL, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction hincrby(final String key, final String field, final long increment, final Handler<AsyncResult<String>> handler) {
            sendString(HINCRBY, toPayload(key, field, increment), handler);
            return this;
        }

        @Override
        public RedisTransaction hincrbyfloat(final String key, final String field, final double increment, final Handler<AsyncResult<String>> handler) {
            sendString(HINCRBYFLOAT, toPayload(key, field, increment), handler);
            return this;
        }

        @Override
        public RedisTransaction hkeys(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(HKEYS, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction hlen(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(HLEN, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction hmget(final String key, final List<String> fields, final Handler<AsyncResult<String>> handler) {
            sendString(HMGET, toPayload(key, fields), handler);
            return this;
        }

        @Override
        public RedisTransaction hmset(final String key, final JsonObject values, final Handler<AsyncResult<String>> handler) {
            sendString(HMSET, toPayload(key, values), handler);
            return this;
        }

        @Override
        public RedisTransaction hset(final String key, final String field, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(HSET, toPayload(key, field, value), handler);
            return this;
        }

        @Override
        public RedisTransaction hsetnx(final String key, final String field, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(HSETNX, toPayload(key, field, value), handler);
            return this;
        }

        @Override
        public RedisTransaction hvals(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(HVALS, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction incr(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(INCR, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction incrby(final String key, final long increment, final Handler<AsyncResult<String>> handler) {
            sendString(INCRBY, toPayload(key, increment), handler);
            return this;
        }

        @Override
        public RedisTransaction incrbyfloat(final String key, final double increment, final Handler<AsyncResult<String>> handler) {
            sendString(INCRBYFLOAT, toPayload(key, increment), handler);
            return this;
        }

        @Override
        public RedisTransaction info(final Handler<AsyncResult<String>> handler) {
            sendString(INFO, Collections.emptyList(), handler);
            return this;
        }

        @Override
        public RedisTransaction infoSection(final String section, final Handler<AsyncResult<String>> handler) {
            sendString(INFO, toPayload(section), handler);
            return this;
        }

        @Override
        public RedisTransaction keys(final String pattern, final Handler<AsyncResult<String>> handler) {
            sendString(KEYS, toPayload(pattern), handler);
            return this;
        }

        @Override
        public RedisTransaction lastsave(final Handler<AsyncResult<String>> handler) {
            sendString(LASTSAVE, null, handler);
            return this;
        }

        @Override
        public RedisTransaction lindex(final String key, final int index, final Handler<AsyncResult<String>> handler) {
            sendString(LINDEX, toPayload(key, index), handler);
            return this;
        }

        @Override
        public RedisTransaction linsert(final String key, final InsertOptions option, final String pivot, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(LINSERT, toPayload(key, option.name(), pivot, value), handler);
            return this;
        }

        @Override
        public RedisTransaction llen(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(LLEN, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction lpop(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(LPOP, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction lpushMany(final String key, final List<String> values, final Handler<AsyncResult<String>> handler) {
            sendString(LPUSH, toPayload(key, values), handler);
            return this;
        }

        @Override
        public RedisTransaction lpush(final String key, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(LPUSH, toPayload(key, value), handler);
            return this;
        }

        @Override
        public RedisTransaction lpushx(final String key, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(LPUSHX, toPayload(key, value), handler);
            return this;
        }

        @Override
        public RedisTransaction lrange(final String key, final long from, final long to, final Handler<AsyncResult<String>> handler) {
            sendString(LRANGE, toPayload(key, from, to), handler);
            return this;
        }

        @Override
        public RedisTransaction lrem(final String key, final long count, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(LREM, toPayload(key, count, value), handler);
            return this;
        }

        @Override
        public RedisTransaction lset(final String key, final long index, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(LSET, toPayload(key, index, value), handler);
            return this;
        }

        @Override
        public RedisTransaction ltrim(final String key, final long from, final long to, final Handler<AsyncResult<String>> handler) {
            sendString(LTRIM, toPayload(key, from, to), handler);
            return this;
        }

        @Override
        public RedisTransaction mget(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(MGET, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction mgetMany(final List<String> keys, final Handler<AsyncResult<String>> handler) {
            sendString(MGET, toPayload(keys), handler);
            return this;
        }

        @Override
        public RedisTransaction migrate(final String host, final int port, final String key, final int destdb, final long timeout, final MigrateOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(MIGRATE, toPayload(host, port, key, destdb, timeout, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction monitor(final Handler<AsyncResult<String>> handler) {
            sendString(MONITOR, null, handler);
            return this;
        }

        @Override
        public RedisTransaction move(final String key, final int destdb, final Handler<AsyncResult<String>> handler) {
            sendString(MOVE, toPayload(key, destdb), handler);
            return this;
        }

        @Override
        public RedisTransaction mset(final JsonObject keyvals, final Handler<AsyncResult<String>> handler) {
            sendString(MSET, toPayload(keyvals), handler);
            return this;
        }

        @Override
        public RedisTransaction msetnx(final JsonObject keyvals, final Handler<AsyncResult<String>> handler) {
            sendString(MSETNX, toPayload(keyvals), handler);
            return this;
        }

        @Override
        public RedisTransaction multi(final Handler<AsyncResult<String>> handler) {
            sendString(MULTI, null, handler);
            return this;
        }

        @Override
        public RedisTransaction object(final String key, final ObjectCmd cmd, final Handler<AsyncResult<String>> handler) {
            sendString(OBJECT, toPayload(cmd.name(), key), handler);
            return this;
        }

        @Override
        public RedisTransaction persist(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(PERSIST, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction pexpire(final String key, final long millis, final Handler<AsyncResult<String>> handler) {
            sendString(PEXPIRE, toPayload(key, millis), handler);
            return this;
        }

        @Override
        public RedisTransaction pexpireat(final String key, final long millis, final Handler<AsyncResult<String>> handler) {
            sendString(PEXPIREAT, toPayload(key, millis), handler);
            return this;
        }

        @Override
        public RedisTransaction pfadd(final String key, final String element, final Handler<AsyncResult<String>> handler) {
            sendString(PFADD, toPayload(key, element), handler);
            return this;
        }

        @Override
        public RedisTransaction pfaddMany(final String key, final List<String> elements, final Handler<AsyncResult<String>> handler) {
            sendString(PFADD, toPayload(key, elements), handler);
            return this;
        }

        @Override
        public RedisTransaction pfcount(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(PFCOUNT, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction pfcountMany(final List<String> key, final Handler<AsyncResult<String>> handler) {
            sendString(PFCOUNT, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction pfmerge(final String destkey, final List<String> keys, final Handler<AsyncResult<String>> handler) {
            sendString(PFMERGE, toPayload(destkey, keys), handler);
            return this;
        }

        @Override
        public RedisTransaction ping(final Handler<AsyncResult<String>> handler) {
            sendString(PING, null, handler);
            return this;
        }

        @Override
        public RedisTransaction psetex(final String key, final long millis, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(PSETEX, toPayload(key, millis, value), handler);
            return this;
        }

        @Override
        public RedisTransaction psubscribe(final String pattern, final Handler<AsyncResult<String>> handler) {
            sendString(PSUBSCRIBE, toPayload(pattern), handler);
            return this;
        }

        @Override
        public RedisTransaction psubscribeMany(final List<String> patterns, final Handler<AsyncResult<String>> handler) {
            sendString(PSUBSCRIBE, toPayload(patterns), handler);
            return this;
        }

        @Override
        public RedisTransaction pubsubChannels(final String pattern, final Handler<AsyncResult<String>> handler) {
            sendString(PUBSUB, toPayload("CHANNELS", pattern == null || "".equals(pattern) ? null : pattern), handler);
            return this;
        }

        @Override
        public RedisTransaction pubsubNumsub(final List<String> channels, final Handler<AsyncResult<String>> handler) {
            sendString(PUBSUB, toPayload("NUMSUB", channels), handler);
            return this;
        }

        @Override
        public RedisTransaction pubsubNumpat(final Handler<AsyncResult<String>> handler) {
            sendString(PUBSUB, toPayload("NUMPAT"), handler);
            return this;
        }

        @Override
        public RedisTransaction pttl(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(PTTL, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction publish(final String channel, final String message, final Handler<AsyncResult<String>> handler) {
            sendString(PUBLISH, toPayload(channel, message), handler);
            return this;
        }

        @Override
        public RedisTransaction punsubscribe(final List<String> patterns, final Handler<AsyncResult<String>> handler) {
            sendString(PUNSUBSCRIBE, toPayload(patterns), handler);
            return this;
        }


        @Override
        public RedisTransaction randomkey(final Handler<AsyncResult<String>> handler) {
            sendString(RANDOMKEY, null, handler);
            return this;
        }

        @Override
        public RedisTransaction rename(final String key, final String newkey, final Handler<AsyncResult<String>> handler) {
            sendString(RENAME, toPayload(key, newkey), handler);
            return this;
        }

        @Override
        public RedisTransaction renamenx(final String key, final String newkey, final Handler<AsyncResult<String>> handler) {
            sendString(RENAMENX, toPayload(key, newkey), handler);
            return this;
        }

        @Override
        public RedisTransaction restore(final String key, final long millis, final String serialized, final Handler<AsyncResult<String>> handler) {
            sendString(RESTORE, toPayload(key, millis, RedisEncoding.decode(serialized)), handler);
            return this;
        }

        @Override
        public RedisTransaction role(final Handler<AsyncResult<String>> handler) {
            sendString(ROLE, null, handler);
            return this;
        }

        @Override
        public RedisTransaction rpop(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(RPOP, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction rpoplpush(final String key, final String destkey, final Handler<AsyncResult<String>> handler) {
            sendString(RPOPLPUSH, toPayload(key, destkey), handler);
            return this;
        }

        @Override
        public RedisTransaction rpushMany(final String key, final List<String> values, final Handler<AsyncResult<String>> handler) {
            sendString(RPUSH, toPayload(key, values), handler);
            return this;
        }

        @Override
        public RedisTransaction rpush(final String key, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(RPUSH, toPayload(key, value), handler);
            return this;
        }

        @Override
        public RedisTransaction rpushx(final String key, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(RPUSHX, toPayload(key, value), handler);
            return this;
        }

        @Override
        public RedisTransaction sadd(final String key, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(SADD, toPayload(key, member), handler);
            return this;
        }

        @Override
        public RedisTransaction saddMany(final String key, final List<String> members, final Handler<AsyncResult<String>> handler) {
            sendString(SADD, toPayload(key, members), handler);
            return this;
        }

        @Override
        public RedisTransaction save(final Handler<AsyncResult<String>> handler) {
            sendString(SAVE, null, handler);
            return this;
        }

        @Override
        public RedisTransaction scard(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(SCARD, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction scriptExists(final String script, final Handler<AsyncResult<String>> handler) {
            sendString(SCRIPT, toPayload("EXISTS", script), handler);
            return this;
        }

        @Override
        public RedisTransaction scriptExistsMany(final List<String> scripts, final Handler<AsyncResult<String>> handler) {
            sendString(SCRIPT, toPayload("EXISTS", scripts), handler);
            return this;
        }

        @Override
        public RedisTransaction scriptFlush(final Handler<AsyncResult<String>> handler) {
            sendString(SCRIPT, toPayload("FLUSH"), handler);
            return this;
        }

        @Override
        public RedisTransaction scriptKill(final Handler<AsyncResult<String>> handler) {
            sendString(SCRIPT, toPayload("KILL"), handler);
            return this;
        }

        @Override
        public RedisTransaction scriptLoad(final String script, final Handler<AsyncResult<String>> handler) {
            sendString(SCRIPT, toPayload("LOAD", script), handler);
            return this;
        }

        @Override
        public RedisTransaction sdiff(final String key, final List<String> cmpkeys, final Handler<AsyncResult<String>> handler) {
            sendString(SDIFF, toPayload(key, cmpkeys), handler);
            return this;
        }

        @Override
        public RedisTransaction sdiffstore(final String destkey, final String key, final List<String> cmpkeys, final Handler<AsyncResult<String>> handler) {
            sendString(SDIFFSTORE, toPayload(destkey, key, cmpkeys), handler);
            return this;
        }

        @Override
        public RedisTransaction select(final int dbindex, final Handler<AsyncResult<String>> handler) {
            sendString(SELECT, toPayload(dbindex), handler);
            return this;
        }

        @Override
        public RedisTransaction set(final String key, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(SET, toPayload(key, value), handler);
            return this;
        }

        @Override
        public RedisTransaction setWithOptions(final String key, final String value, final SetOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(SET, toPayload(key, value, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction setBinary(final String key, final Buffer value, final Handler<AsyncResult<String>> handler) {
            sendString(SET, toPayload(key, value), handler);
            return this;
        }

        @Override
        public RedisTransaction setBinaryWithOptions(final String key, final Buffer value, final SetOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(SET, toPayload(key, value, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction setbit(final String key, final long offset, final int bit, final Handler<AsyncResult<String>> handler) {
            sendString(SETBIT, toPayload(key, offset, bit), handler);
            return this;
        }

        @Override
        public RedisTransaction setex(final String key, final long seconds, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(SETEX, toPayload(key, seconds, value), handler);
            return this;
        }

        @Override
        public RedisTransaction setnx(final String key, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(SETNX, toPayload(key, value), handler);
            return this;
        }

        @Override
        public RedisTransaction setrange(final String key, final int offset, final String value, final Handler<AsyncResult<String>> handler) {
            sendString(SETRANGE, toPayload(key, offset, value), handler);
            return this;
        }

        @Override
        public RedisTransaction sinter(final List<String> keys, final Handler<AsyncResult<String>> handler) {
            sendString(SINTER, toPayload(keys), handler);
            return this;
        }

        @Override
        public RedisTransaction sinterstore(final String destkey, final List<String> keys, final Handler<AsyncResult<String>> handler) {
            sendString(SINTERSTORE, toPayload(destkey, keys), handler);
            return this;
        }

        @Override
        public RedisTransaction sismember(final String key, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(SISMEMBER, toPayload(key, member), handler);
            return this;
        }

        @Override
        public RedisTransaction slaveof(final String host, final int port, final Handler<AsyncResult<String>> handler) {
            sendString(SLAVEOF, toPayload(host, port), handler);
            return this;
        }

        @Override
        public RedisTransaction slaveofNoone(final Handler<AsyncResult<String>> handler) {
            sendString(SLAVEOF, toPayload("NO", "ONE"), handler);
            return this;
        }

        @Override
        public RedisTransaction slowlogGet(final int limit, final Handler<AsyncResult<String>> handler) {
            sendString(SLOWLOG, toPayload("GET", limit < 0 ? null : limit), handler);
            return this;
        }

        @Override
        public RedisTransaction slowlogLen(final Handler<AsyncResult<String>> handler) {
            sendString(SLOWLOG, toPayload("LEN"), handler);
            return this;
        }

        @Override
        public RedisTransaction slowlogReset(final Handler<AsyncResult<String>> handler) {
            sendString(SLOWLOG, toPayload("RESET"), handler);
            return this;
        }

        @Override
        public RedisTransaction smembers(final String key, final Handler<AsyncResult<JsonArray>> handler) {
            sendJsonArray(SMEMBERS, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction smove(final String key, final String destkey, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(SMOVE, toPayload(key, destkey, member), handler);
            return this;
        }

        @Override
        public RedisTransaction sort(final String key, final SortOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(SORT, toPayload(key, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction spop(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(SPOP, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction spopMany(final String key, final int count, final Handler<AsyncResult<String>> handler) {
            sendString(SPOP, toPayload(key, count), handler);
            return this;
        }

        @Override
        public RedisTransaction srandmember(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(SRANDMEMBER, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction srandmemberCount(final String key, final int count, final Handler<AsyncResult<String>> handler) {
            sendString(SRANDMEMBER, toPayload(key, count), handler);
            return this;
        }

        @Override
        public RedisTransaction srem(final String key, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(SREM, toPayload(key, member), handler);
            return this;
        }

        @Override
        public RedisTransaction sremMany(final String key, final List<String> members, final Handler<AsyncResult<String>> handler) {
            sendString(SREM, toPayload(key, members), handler);
            return this;
        }

        @Override
        public RedisTransaction strlen(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(STRLEN, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction subscribe(final String channel, final Handler<AsyncResult<String>> handler) {
            sendString(SUBSCRIBE, toPayload(channel), handler);
            return this;
        }

        @Override
        public RedisTransaction subscribeMany(final List<String> channels, final Handler<AsyncResult<String>> handler) {
            sendString(SUBSCRIBE, toPayload(channels), handler);
            return this;
        }

        @Override
        public RedisTransaction sunion(final List<String> keys, final Handler<AsyncResult<String>> handler) {
            sendString(SUNION, toPayload(keys), handler);
            return this;
        }

        @Override
        public RedisTransaction sunionstore(final String destkey, final List<String> keys, final Handler<AsyncResult<String>> handler) {
            sendString(SUNIONSTORE, toPayload(destkey, keys), handler);
            return this;
        }

        @Override
        public RedisTransaction sync(final Handler<AsyncResult<String>> handler) {
            sendString(SYNC, null, handler);
            return this;
        }

        @Override
        public RedisTransaction time(final Handler<AsyncResult<String>> handler) {
            sendString(TIME, null, handler);
            return this;
        }

        @Override
        public RedisTransaction ttl(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(TTL, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction type(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(TYPE, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction unsubscribe(final List<String> channels, final Handler<AsyncResult<String>> handler) {
            sendString(UNSUBSCRIBE, toPayload(channels), handler);
            return this;
        }

        @Override
        public RedisTransaction unwatch(final Handler<AsyncResult<String>> handler) {
            sendString(UNWATCH, null, handler);
            return this;
        }

        @Override
        public RedisTransaction wait(final long numSlaves, final long timeout, final Handler<AsyncResult<String>> handler) {
            sendString(WAIT, toPayload(numSlaves, timeout), handler);
            return this;
        }

        @Override
        public RedisTransaction watch(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(WATCH, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction watchMany(final List<String> keys, final Handler<AsyncResult<String>> handler) {
            sendString(WATCH, toPayload(keys), handler);
            return this;
        }

        @Override
        public RedisTransaction zadd(final String key, final double score, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(ZADD, toPayload(key, score, member), handler);
            return this;
        }

        @Override
        public RedisTransaction zaddMany(final String key, final Map<String, Double> members, final Handler<AsyncResult<String>> handler) {
            // flip from <String, Double> to <Double, String> when wrapping
            final Stream flipped = members.entrySet().stream().map(e -> new Object[]{e.getValue(), e.getKey()});
            sendString(ZADD, toPayload(key, flipped), handler);
            return this;
        }

        @Override
        public RedisTransaction zcard(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(ZCARD, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction zcount(final String key, final double min, final double max, final Handler<AsyncResult<String>> handler) {
            final String minVal = (min == Double.NEGATIVE_INFINITY) ? "-inf" : String.valueOf(min);
            final String maxVal = (max == Double.POSITIVE_INFINITY) ? "+inf" : String.valueOf(max);
            sendString(ZCOUNT, toPayload(key, minVal, maxVal), handler);
            return this;
        }

        @Override
        public RedisTransaction zincrby(final String key, final double increment, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(ZINCRBY, toPayload(key, increment, member), handler);
            return this;
        }

        @Override
        public RedisTransaction zinterstore(final String destkey, final List<String> sets, final AggregateOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(ZINTERSTORE, toPayload(destkey, sets.size(), sets, options != null ? options.name() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction zinterstoreWeighed(final String destkey, final Map<String, Double> sets, final AggregateOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(ZINTERSTORE, toPayload(destkey, sets.size(), sets.keySet(), "WEIGHTS", sets.values(),
                    options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction zlexcount(final String key, final String min, final String max, final Handler<AsyncResult<String>> handler) {
            sendString(ZLEXCOUNT, toPayload(key, min, max), handler);
            return this;
        }

        @Override
        public RedisTransaction zrange(final String key, final long start, final long stop, final Handler<AsyncResult<String>> handler) {
            sendString(ZRANGE, toPayload(key, start, stop), handler);
            return this;
        }

        @Override
        public RedisTransaction zrangeWithOptions(final String key, final long start, final long stop, final RangeOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(ZRANGE, toPayload(key, start, stop, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction zrangebylex(final String key, final String min, final String max, final LimitOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(ZRANGEBYLEX, toPayload(key, min, max, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction zrangebyscore(final String key, final String min, final String max, final RangeLimitOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(ZRANGEBYSCORE, toPayload(key, min, max, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction zrank(final String key, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(ZRANK, toPayload(key, member), handler);
            return this;
        }

        @Override
        public RedisTransaction zrem(final String key, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(ZREM, toPayload(key, member), handler);
            return this;
        }

        @Override
        public RedisTransaction zremMany(final String key, final List<String> members, final Handler<AsyncResult<String>> handler) {
            sendString(ZREM, toPayload(key, members), handler);
            return this;
        }

        @Override
        public RedisTransaction zremrangebylex(final String key, final String min, final String max, final Handler<AsyncResult<String>> handler) {
            sendString(ZREMRANGEBYLEX, toPayload(key, min, max), handler);
            return this;
        }

        @Override
        public RedisTransaction zremrangebyrank(final String key, final long start, final long stop, final Handler<AsyncResult<String>> handler) {
            sendString(ZREMRANGEBYRANK, toPayload(key, start, stop), handler);
            return this;
        }

        @Override
        public RedisTransaction zremrangebyscore(final String key, final String min, final String max, final Handler<AsyncResult<String>> handler) {
            sendString(ZREMRANGEBYSCORE, toPayload(key, min, max), handler);
            return this;
        }

        @Override
        public RedisTransaction zrevrange(final String key, final long start, final long stop, final RangeOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(ZREVRANGE, toPayload(key, start, stop, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction zrevrangebylex(final String key, final String max, final String min, final LimitOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(ZREVRANGEBYLEX, toPayload(key, max, min, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction zrevrangebyscore(final String key, final String max, final String min, final RangeLimitOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(ZREVRANGEBYSCORE, toPayload(key, max, min, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction zrevrank(final String key, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(ZREVRANK, toPayload(key, member), handler);
            return this;
        }

        @Override
        public RedisTransaction zscore(final String key, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(ZSCORE, toPayload(key, member), handler);
            return this;
        }

        @Override
        public RedisTransaction zunionstore(final String destkey, final List<String> sets, final AggregateOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(ZUNIONSTORE, toPayload(destkey, sets.size(), sets, options != null ? options.name() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction zunionstoreWeighed(final String destkey, final Map<String, Double> sets, final AggregateOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(ZUNIONSTORE, toPayload(destkey, sets.size(), sets.keySet(), "WEIGHTS", sets.values(),
                    options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction scan(final String cursor, final ScanOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(SCAN, toPayload(cursor, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction sscan(final String key, final String cursor, final ScanOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(SSCAN, toPayload(key, cursor, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction hscan(final String key, final String cursor, final ScanOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(HSCAN, toPayload(key, cursor, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction zscan(final String key, final String cursor, final ScanOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(ZSCAN, toPayload(key, cursor, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction geoadd(final String key, final double longitude, final double latitude, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(GEOADD, toPayload(key, longitude, latitude, member), handler);
            return this;
        }

        @Override
        public RedisTransaction geoaddMany(final String key, final List<GeoMember> members, final Handler<AsyncResult<String>> handler) {
            sendString(GEOADD, toPayload(key, members), handler);
            return this;
        }

        @Override
        public RedisTransaction geohash(final String key, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(GEOHASH, toPayload(key, member), handler);
            return this;
        }

        @Override
        public RedisTransaction geohashMany(final String key, final List<String> members, final Handler<AsyncResult<String>> handler) {
            sendString(GEOHASH, toPayload(key, members), handler);
            return this;
        }

        @Override
        public RedisTransaction geopos(final String key, final String member, final Handler<AsyncResult<String>> handler) {
            sendString(GEOPOS, toPayload(key, member), handler);
            return this;
        }

        @Override
        public RedisTransaction geoposMany(final String key, final List<String> members, final Handler<AsyncResult<String>> handler) {
            sendString(GEOPOS, toPayload(key, members), handler);
            return this;
        }

        @Override
        public RedisTransaction geodist(final String key, final String member1, final String member2, final Handler<AsyncResult<String>> handler) {
            sendString(GEODIST, toPayload(key, member1, member2), handler);
            return this;
        }

        @Override
        public RedisTransaction geodistWithUnit(final String key, final String member1, final String member2, final GeoUnit unit, final Handler<AsyncResult<String>> handler) {
            sendString(GEODIST, toPayload(key, member1, member2, unit), handler);
            return this;
        }

        @Override
        public RedisTransaction georadius(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit, final Handler<AsyncResult<String>> handler) {
            sendString(GEORADIUS, toPayload(key, longitude, latitude, radius, unit), handler);
            return this;
        }

        @Override
        public RedisTransaction georadiusWithOptions(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit, final GeoRadiusOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(GEORADIUS, toPayload(key, longitude, latitude, radius, unit, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction georadiusbymember(final String key, final String member, final double radius, final GeoUnit unit, final Handler<AsyncResult<String>> handler) {
            sendString(GEORADIUSBYMEMBER, toPayload(key, member, radius, unit), handler);
            return this;
        }

        @Override
        public RedisTransaction georadiusbymemberWithOptions(final String key, final String member, final double radius, final GeoUnit unit, final GeoRadiusOptions options, final Handler<AsyncResult<String>> handler) {
            sendString(GEORADIUSBYMEMBER, toPayload(key, member, radius, unit, options != null ? options.toJsonArray() : null), handler);
            return this;
        }

        @Override
        public RedisTransaction unlink(final String key, final Handler<AsyncResult<String>> handler) {
            sendString(UNLINK, toPayload(key), handler);
            return this;
        }

        @Override
        public RedisTransaction unlinkMany(final List<String> keys, final Handler<AsyncResult<String>> handler) {
            sendString(UNLINK, toPayload(keys), handler);
            return this;
        }

        @Override
        public RedisTransaction swapdb(final int index1, final int index2, final Handler<AsyncResult<String>> handler) {
            sendString(SWAPDB, toPayload(index1, index2), handler);
            return this;
        }
    }
}
