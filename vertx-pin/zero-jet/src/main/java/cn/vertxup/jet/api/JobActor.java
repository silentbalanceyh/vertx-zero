package cn.vertxup.jet.api;

import cn.vertxup.jet.service.JobStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.cv.JtAddr;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.plugin.job.JobPool;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Queue
public class JobActor {

    @Inject
    private transient JobStub stub;

    @Address(JtAddr.Job.START)
    public Future<Boolean> start(final String code) {
        return Ux.Job.on().start(code);
    }

    @Address(JtAddr.Job.STOP)
    public Future<Boolean> stop(final String code) {
        return Ux.Job.on().stop(code);
    }

    @Address(JtAddr.Job.RESUME)
    public Future<Boolean> resume(final String code) {
        return Ux.Job.on().resume(code);
    }

    @Address(JtAddr.Job.STATUS)
    public JsonObject status(final String namespace) {
        /*
         * JtApp ( namespace processing )
         */
        final ConcurrentMap<String, Mission> jobs = JobPool.mapJobs();
        final ConcurrentMap<Long, String> runs = JobPool.mapRuns();
        /*
         * Revert
         */
        final ConcurrentMap<String, Long> runsRevert =
                new ConcurrentHashMap<>();
        runs.forEach((timer, code) -> runsRevert.put(code, timer));
        final JsonObject response = new JsonObject();
        jobs.forEach((code, mission) -> {
            /*
             * Processing
             */
            final JsonObject instance = new JsonObject();
            instance.put(KeField.NAME, mission.getName());
            instance.put(KeField.STATUS, mission.getStatus().name());
            /*
             * Timer
             */
            instance.put("timer", runsRevert.get(mission.getCode()));
            response.put(mission.getCode(), instance);
        });
        return response;
    }

    /*
     * Basic Job api here
     */
    @Address(JtAddr.Job.BY_SIGMA)
    public Future<JsonObject> fetch(final String sigma, final JsonObject body, final Boolean grouped) {
        return this.stub.searchJobs(sigma, body, grouped);
    }

    @Address(JtAddr.Job.GET_BY_KEY)
    public Future<JsonObject> fetchByKey(final String key) {
        return this.stub.fetchByKey(key);
    }

    @Address(JtAddr.Job.UPDATE_BY_KEY)
    public Future<JsonObject> updateByKey(final String key, final JsonObject data) {
        return this.stub.update(key, data);
    }
}
