package io.vertx.up.uca.job.plugin;

import io.horizon.uca.log.Annal;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.uca.job.center.Agha;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class JobClientImpl implements JobClient {

    private static final Annal LOGGER = Annal.get(JobClientImpl.class);
    private transient final Vertx vertx;
    private transient final JsonObject config;

    JobClientImpl(final Vertx vertx, final JsonObject config) {
        this.vertx = vertx;
        this.config = config;
    }


    @Override
    public Future<Long> startAsync(final String code) {
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
            return agha.begin(mission);
        } else {
            LOGGER.info("( JobClient ) The pool could not find job of code = `{0}`", code);
            return Ux.future(-1L);
        }
    }

    @Override
    public JobClient startAsync(final String code, final Handler<AsyncResult<Long>> handler) {
        handler.handle(this.startAsync(code));
        return this;
    }

    @Override
    public Future<Boolean> stopAsync(final String code) {
        final Long timerId = JobPool.timeId(code);
        /* Remove mission from running pool */
        JobPool.stop(timerId);
        /* Cancel job */
        this.vertx.cancelTimer(timerId);
        return Future.succeededFuture(Boolean.TRUE);
    }

    @Override
    public JobClient stopAsync(final String code, final Handler<AsyncResult<Boolean>> handler) {
        handler.handle(this.stopAsync(code));
        return this;
    }

    @Override
    public Future<Long> resumeAsync(final String code) {
        final Long timeId = JobPool.timeId(code);
        JobPool.resume(timeId);
        return this.startAsync(code);
    }

    @Override
    public JobClient resumeAsync(final String code, final Handler<AsyncResult<Long>> handler) {
        handler.handle(this.resumeAsync(code));
        return this;
    }

    @Override
    public JobClient fetchAsync(final String code, final Handler<AsyncResult<Mission>> handler) {
        handler.handle(this.fetchAsync(code));
        return this;
    }

    @Override
    public Future<Mission> fetchAsync(final String code) {
        return Future.succeededFuture(this.fetch(code));
    }

    @Override
    public Mission fetch(final String code) {
        return JobPool.get(code);
    }

    @Override
    public JobClient fetchAsync(final Set<String> codes, final Handler<AsyncResult<List<Mission>>> handler) {
        handler.handle(this.fetchAsync(codes));
        return this;
    }

    @Override
    public List<Mission> fetch(final Set<String> codes) {
        final List<Mission> missionList = JobPool.get();
        if (Objects.isNull(codes) || codes.isEmpty()) {
            return new ArrayList<>();
        } else {
            return missionList.stream()
                .filter(mission -> codes.contains(mission.getCode()))
                .collect(Collectors.toList());
        }
    }

    @Override
    public Future<List<Mission>> fetchAsync(final Set<String> codes) {
        return Ux.future(this.fetch(codes));
    }


    @Override
    public JobClient saveAsync(final Mission mission, final Handler<AsyncResult<Mission>> handler) {
        handler.handle(this.saveAsync(mission));
        return this;
    }

    @Override
    public Mission save(final Mission mission) {
        JobPool.save(mission);
        return mission;
    }

    @Override
    public Future<Mission> saveAsync(final Mission mission) {
        return Ux.future(this.save(mission));
    }


    @Override
    public Mission remove(final String code) {
        final Mission mission = this.fetch(code);
        JobPool.remove(code);
        return mission;
    }

    @Override
    public Future<Mission> removeAsync(final String code) {
        return Ux.future(this.remove(code));
    }

    @Override
    public JobClient removeAsync(final String code, final Handler<AsyncResult<Mission>> handler) {
        handler.handle(this.removeAsync(code));
        return this;
    }

    @Override
    public JobClient saveAsync(final Set<Mission> missions, final Handler<AsyncResult<Set<Mission>>> handler) {
        handler.handle(this.saveAsync(missions));
        return this;
    }

    @Override
    public Future<Set<Mission>> saveAsync(final Set<Mission> missions) {
        return Ux.future(this.save(missions));
    }

    @Override
    public Set<Mission> save(final Set<Mission> missions) {
        missions.forEach(this::save);
        return missions;
    }

    @Override
    public JsonObject status(final String namespace) {
        return JobPool.status(namespace);
    }

    @Override
    public Future<JsonObject> statusAsync(final String namespace) {
        return Ux.future(this.status(namespace));
    }

    @Override
    public JobClient statusAsync(final String namespace, final Handler<AsyncResult<JsonObject>> handler) {
        handler.handle(this.statusAsync(namespace));
        return this;
    }
}
