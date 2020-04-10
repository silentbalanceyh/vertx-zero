package io.vertx.tp.plugin.job;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.job.center.Agha;
import io.vertx.up.util.Ut;

import java.util.Objects;

public class JobClientImpl implements JobClient {

    private static final Annal LOGGER = Annal.get(JobClientImpl.class);
    private transient final Vertx vertx;
    private transient final JsonObject config;

    JobClientImpl(final Vertx vertx, final JsonObject config) {
        this.vertx = vertx;
        this.config = config;
    }

    @Override
    public JobClient start(final String code, final Handler<AsyncResult<Long>> handler) {
        /* Find Mission by code */
        final Mission mission = JobPool.get(code);
        if (Objects.nonNull(mission)) {
            /* Start new job here */
            final Agha agha = Agha.get(mission.getType());
            /* Bind vertx */
            Ut.contract(agha, Vertx.class, this.vertx);
            /*
             * begin method return Future<Long>, it's async result
             * that's why here it's not needed to use:
             * Future.successedFuture() to wrapper result, instead
             * returned directly.
             * */
            final Future<Long> future = agha.begin(mission);
            handler.handle(future);
        } else {
            LOGGER.info("( JobClient ) The pool could not find job of code = `{0}`", code);
        }
        return this;
    }

    @Override
    public JobClient stop(final Long timerId, final Handler<AsyncResult<Boolean>> handler) {
        /* Remove mission from running pool */
        JobPool.stop(timerId);
        handler.handle(Future.succeededFuture(Boolean.TRUE));
        /* Cancel job */
        this.vertx.cancelTimer(timerId);
        return this;
    }

    @Override
    public JobClient resume(final Long timeId, final Handler<AsyncResult<Long>> handler) {
        JobPool.resume(timeId);
        /* String code get and then start */
        final String code = JobPool.code(timeId);
        return this.start(code, handler);
    }
}
