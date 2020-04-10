package cn.vertxup.jet.service;

import cn.vertxup.cache.AmbientStub;
import cn.vertxup.jet.domain.tables.daos.IJobDao;
import cn.vertxup.jet.domain.tables.daos.IServiceDao;
import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.jet.refine.Jt;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class JobService implements JobStub {
    private static final Annal LOGGER = Annal.get(JobService.class);
    @Inject
    private transient AmbientStub ambient;

    @Override
    public Future<JsonArray> fetchAll(final String sigma) {
        return Ux.Jooq.on(IJobDao.class)
                .<IJob>fetchAsync("sigma", sigma)
                .compose(jobs -> {
                    /*
                     * Result for all jobs that are belong to current sigma here.
                     */
                    final Set<String> codes = jobs.stream()
                            .filter(Objects::nonNull)
                            /*
                             * Job name calculation for appending namespace
                             */
                            .map(Jt::jobCode)
                            .collect(Collectors.toSet());
                    Jt.infoWeb(LOGGER, "Job fetched from database: {0}", codes.size());
                    return JobKit.fetchMission(codes);
                });
    }

    @Override
    public Future<JsonObject> fetchByKey(final String key) {
        return Ux.Jooq.on(IJobDao.class)
                .<IJob>findByIdAsync(key)
                /*
                 * 1) Supplier here for `JsonObject` generated
                 * 2) Mission conversation here to JsonObject directly
                 */
                .compose(Ut.ifNil(JsonObject::new,
                        job -> JobKit.fetchMission(Jt.jobCode(job))));
    }

    @Override
    public Future<JsonObject> update(final String key, final JsonObject data) {
        /*
         * 1. Service / Job Split
         */
        JsonObject serviceJson = data.getJsonObject(KeField.SERVICE);
        if (Ut.isNil(serviceJson)) {
            serviceJson = new JsonObject();
        } else {
            serviceJson = serviceJson.copy();
            data.remove(KeField.SERVICE);
        }
        /*
         * 2. Upsert by Key for Job instance
         */
        final IJob job = Ux.fromJson(data, IJob.class);
        final IService service = JobKit.fromJson(serviceJson);
        return Ux.Jooq.on(IJobDao.class)
                /*
                 * 3. Upsert by Key for Service instance
                 */
                .upsertAsync(job.getKey(), job)
                .compose(updatedJob -> Ux.Jooq.on(IServiceDao.class)
                        .upsertAsync(service.getKey(), service)
                        /*
                         * 4. Merge updatedJob / updatedService
                         * -- Call `AmbientService` to updateJob cache
                         * -- Cache updating ( IJob / IService )
                         */
                        .compose(updatedSev ->
                                this.ambient.updateJob(updatedJob, updatedSev)));
    }
}
