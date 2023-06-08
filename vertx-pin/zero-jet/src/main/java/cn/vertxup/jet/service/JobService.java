package cn.vertxup.jet.service;

import cn.vertxup.cache.AmbientStub;
import cn.vertxup.jet.domain.tables.daos.IJobDao;
import cn.vertxup.jet.domain.tables.daos.IServiceDao;
import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import io.horizon.uca.log.Annal;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.jet.refine.Jt;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static io.vertx.mod.jet.refine.Jt.LOG;

public class JobService implements JobStub {
    private static final Annal LOGGER = Annal.get(JobService.class);
    @Inject
    private transient AmbientStub ambient;

    @Override
    public Future<JsonObject> searchJobs(final String sigma, final JsonObject body, final boolean grouped) {
        final Ir qr = Ir.create(body);
        qr.getCriteria().save("sigma", sigma);
        final JsonObject condition = qr.toJson();
        LOGGER.info("Job condition: {0}", condition);
        return Ux.Jooq.on(IJobDao.class)
            .searchAsync(condition)
            .compose(jobs -> {
                /*
                 * Result for all jobs that are belong to current sigma here.
                 */
                final List<IJob> jobList = Ux.fromPage(jobs, IJob.class);
                final Set<String> codes = jobList.stream()
                    .filter(Objects::nonNull)
                    /*
                     * Job name calculation for appending namespace
                     */
                    .map(Jt::jobCode)
                    .collect(Collectors.toSet());
                LOG.Web.info(LOGGER, "Job fetched from database: {0}, input sigma: {1}",
                    codes.size(), sigma);
                return JobKit.fetchMission(codes).compose(normalized -> {
                    jobs.put("list", normalized);
                    /*
                     * count group
                     * */
                    if (grouped) {
                        final JsonObject criteria = qr.getCriteria().toJson();
                        return Ux.Jooq.on(IJobDao.class).countByAsync(criteria, "group")
                            .compose(aggregation -> {
                                final JsonObject aggregationJson = new JsonObject();
                                aggregation.forEach(aggregationJson::put);
                                jobs.put("aggregation", aggregationJson);
                                return Ux.future(jobs);
                            });
                    } else {
                        return Ux.future(jobs);
                    }
                });
            });
    }

    @Override
    public Future<JsonObject> fetchByKey(final String key) {
        return Ux.Jooq.on(IJobDao.class)
            .<IJob>fetchByIdAsync(key)
            /*
             * 1) Supplier here for `JsonObject` generated
             * 2) Mission conversation here to JsonObject directly
             */
            .compose(Fn.ofJObject(job -> JobKit.fetchMission(Jt.jobCode(job))));
    }

    @Override
    public Future<JsonObject> update(final String key, final JsonObject data) {
        /*
         * 1. Service / Job Split
         */
        JsonObject serviceJson = data.getJsonObject(KName.SERVICE);
        if (Ut.isNil(serviceJson)) {
            serviceJson = new JsonObject();
        } else {
            serviceJson = serviceJson.copy();
            data.remove(KName.SERVICE);
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
