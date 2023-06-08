package io.vertx.up.plugin.redis;

import io.horizon.eon.VValue;
import io.horizon.exception.WebException;
import io.horizon.uca.log.Annal;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.auth.PRNG;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.redis.client.*;
import io.vertx.up.error.redis._409SessionVersionException;
import io.vertx.up.util.Ut;

import java.util.*;

public class RedisStore implements SessionStore {

    private static final Annal LOGGER = Annal.get(RedisStore.class);
    private static transient PRNG random;
    /* Local Map */
    private final transient List<String> sessionIds = new Vector<>();
    /* Client reference */
    private transient Redis client;
    private transient LocalMap<String, Session> localMap;

    /* Configuration of additional */
    private transient RedisExtra extra;

    @Override
    public SessionStore init(final Vertx vertx, final JsonObject options) {
        /* RedisClient options here */
        random = new PRNG(vertx);
        this.localMap = vertx.sharedData().getLocalMap(VValue.DEFAULT_SESSION);

        /* Redis options */
        final RedisOptions opts = new RedisOptions(options);
        LOGGER.info(RedisMsg.RD_OPTS,
            /* Endpoint information for current */
            opts.getEndpoint(),
            opts.toJson().encode());

        /* Client init, old version */
        this.client = Redis.createClient(vertx, opts);

        /* Extra configuration options */
        this.extra = Ut.deserialize(options, RedisExtra.class);
        return this;
    }

    @Override
    public long retryTimeout() {
        /* Get retry timeout field */
        return this.extra.getRetryTimeout();
    }

    @Override
    public Session createSession(final long timeout) {
        return new RedisSession(random, timeout, DEFAULT_SESSIONID_LENGTH);
    }

    @Override
    public Session createSession(final long timeout, final int length) {
        return new RedisSession(random, timeout, length);
    }

    private RedisSession createSession() {
        return new RedisSession(random, this.extra.getTimeout(), DEFAULT_SESSIONID_LENGTH);
    }

    @Override
    public SessionStore clear(final Handler<AsyncResult<Void>> handler) {
        handler.handle(this.clear());
        return this;
    }

    @Override
    public Future<Void> clear() {
        /*
         * To avoid: java.util.ConcurrentModificationException
         */
        final Promise<Void> promise = Promise.promise();
        final Set<String> idSet = new HashSet<>(this.sessionIds);
        idSet.forEach(sessionId -> this.client.send(Request.cmd(Command.DEL), res -> {
            if (res.succeeded()) {
                /*
                 * This sessionId should be removed
                 */
                this.sessionIds.remove(sessionId);
                promise.complete();
            }
        }));
        return promise.future();
    }

    @Override
    public SessionStore size(final Handler<AsyncResult<Integer>> handler) {
        /*
         * Session IDs
         */
        handler.handle(this.size());
        return this;
    }

    @Override
    public Future<Integer> size() {
        return Future.succeededFuture(this.sessionIds.size());
    }

    @Override
    public void close() {
        /*
         * Close Handler
         */
        this.client.close();
    }

    @Override
    public SessionStore delete(final String id, final Handler<AsyncResult<Void>> handler) {
        handler.handle(this.delete(id));
        return this;
    }

    @Override
    public Future<Void> delete(final String id) {
        final Promise<Void> promise = Promise.promise();
        LOGGER.debug(RedisMsg.RS_MESSAGE, id, "delete(String)");
        this.client.send(Request.cmd(Command.DEL), res -> {
            if (res.succeeded()) {
                /*
                 * Synced SessionIds
                 */
                this.sessionIds.remove(id);

                LOGGER.info(RedisMsg.RD_CLEAR, id);
                promise.complete();
            } else {
                /*
                 * ERROR throw out
                 */
                res.cause().printStackTrace();
                promise.fail(res.cause());
            }
        });
        return promise.future();
    }

    @Override
    public SessionStore get(final String id, final Handler<AsyncResult<Session>> handler) {
        handler.handle(this.get(id));
        return this;
    }

    @Override
    public Future<Session> get(final String id) {
        LOGGER.debug(RedisMsg.RS_MESSAGE, id, "get(String)");
        final Request request = Request.cmd(Command.GETBIT);
        request.arg(id);
        final Promise<Session> promise = Promise.promise();
        this.client.send(request, res -> {
            /*
             * Whether get buffer data after read data from redis.
             * If successful continue to process buffer data
             */
            if (res.succeeded()) {
                final Response response = res.result();
                final Buffer buffer = response.toBuffer();
                if (Objects.nonNull(buffer)) {
                    final RedisSession session = this.createSession();
                    if (0 < buffer.length()) {
                        session.readFromBuffer(0, buffer);
                    }
                    promise.complete(session);
                    // handler.handle(Future.succeededFuture(session));
                } else {
                    /*
                     * LocalMap of vertx-web.sessions
                     */
                    promise.complete(this.localMap.get(id));
                    // handler.handle(Future.succeededFuture(this.localMap.get(id)));
                }
            } else {
                /*
                 * ERROR throw out
                 */
                res.cause().printStackTrace();
                promise.fail(res.cause());
                // handler.handle(Future.failedFuture(res.cause()));
            }
        });
        return promise.future();
    }

    @Override
    public SessionStore put(final Session session, final Handler<AsyncResult<Void>> handler) {
        handler.handle(this.put(session));
        return this;
    }

    @Override
    @SuppressWarnings("all")
    public Future<Void> put(final Session session) {
        final String id = session.id();
        LOGGER.debug(RedisMsg.RS_MESSAGE, id, "put(Session)");
        final Request request = Request.cmd(Command.SET);
        request.arg(id);
        final Promise<Void> promise = Promise.promise();
        client.send(request, res -> {
            if (res.succeeded()) {
                final Response response = res.result();
                final Buffer buffer = response.toBuffer();
                final RedisSession finalSession;
                final RedisSession oldSession = createSession();
                if (Objects.isNull(buffer)) {
                    /*
                     * Data null
                     */
                    RedisSession sessionImpl = (RedisSession) session;
                    finalSession = sessionImpl;
                } else {
                    /*
                     * Data Existing
                     */
                    final RedisSession newSession = (RedisSession) session;
                    oldSession.readFromBuffer(0, buffer);
                    /*
                     * Version matching here for comparing
                     */
                    if (oldSession.version() != newSession.version()) {
                        final WebException error = new _409SessionVersionException(getClass(),
                            oldSession.version(), newSession.version());
                        promise.fail(error);
                        // handler.handle(Future.failedFuture(error));
                        return;
                    }
                    finalSession = newSession;
                }
                finalSession.incrementVersion();
                /*
                 * Write session data
                 */
                writeSession(session, res2 -> {
                    if (res2.succeeded()) {
                        if (Objects.nonNull(res2.result())) {
                            final String added = res2.result();
                            if (!sessionIds.contains(added)) {
                                sessionIds.add(res2.result());
                            }
                            /*
                             * Append data into localMap to cache
                             */
                            localMap.put(added, session);
                            promise.complete();
                        }
                        LOGGER.info(RedisMsg.RS_AFTER, finalSession.id(),
                            null == oldSession ? null : oldSession.id());
                    } else {
                        /*
                         * ERROR throw out
                         */
                        res2.cause().printStackTrace();
                        promise.fail(res.cause());
                        // handler.handle(Future.failedFuture(res.cause()));
                    }
                });
            } else {
                /*
                 * ERROR throw out
                 */
                res.cause().printStackTrace();
                promise.fail(res.cause());
                // handler.handle(Future.failedFuture(res.cause()));
            }
        });
        return promise.future();
    }

    private void writeSession(final Session session, final Handler<AsyncResult<String>> handler) {
        /* Write buffer */
        final Buffer buffer = Buffer.buffer();
        final RedisSession sessionImpl = (RedisSession) session;
        sessionImpl.writeToBuffer(buffer);

        /* Synced timeout here */
        final Request request = Request.cmd(Command.SETBIT);
        final String key = sessionImpl.id();
        request.arg(key);
        request.arg(buffer);
        this.client.send(request, res -> {
            if (res.succeeded()) {
                LOGGER.info(RedisMsg.RD_KEYS, Ut.fromJoin(session.data().keySet()));
                /*
                 * Add data if this id does not exist
                 */
                handler.handle(Future.succeededFuture(key));
            } else {
                /*
                 * ERROR throw out
                 *
                 */
                res.cause().printStackTrace();
                handler.handle(Future.failedFuture(res.cause()));
            }
        });
        /*
        final SetOptions options = new SetOptions().setPX(session.timeout());
        final String key = sessionImpl.id();
        this.client.setBinaryWithOptions(key, buffer, options, res -> {
            if (res.succeeded()) {
                LOGGER.info(RedisMsg.RD_KEYS, Ut.fromJoin(session.data().keySet()));
                /*
                 * Add data if this id does not exist
                handler.handle(Future.succeededFuture(key));
            } else {
                /*
                 * ERROR throw out
                res.cause().printStackTrace();
                handler.handle(Future.failedFuture(res.cause()));
            }
        });*/
    }
}
