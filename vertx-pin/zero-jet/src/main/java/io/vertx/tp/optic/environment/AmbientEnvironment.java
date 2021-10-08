package io.vertx.tp.optic.environment;

import cn.vertxup.jet.domain.tables.daos.IApiDao;
import cn.vertxup.jet.domain.tables.daos.IJobDao;
import cn.vertxup.jet.domain.tables.daos.IServiceDao;
import cn.vertxup.jet.domain.tables.pojos.IApi;
import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.jet.atom.JtJob;
import io.vertx.tp.jet.atom.JtUri;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Cross Data Object
 * 1) App / Source information here
 * 2) Pool ( DSLContext, Connection, DataSource )
 */
// TODO: NEW VERSION
public class AmbientEnvironment {

    /* Pool of Jobs, it will be consumed by each application */
    private final transient ConcurrentMap<String, JtJob> jobs
        = new ConcurrentHashMap<>();
    /* Pool of JtUri */
    private final transient ConcurrentMap<String, JtUri> uris
        = new ConcurrentHashMap<>();

    /* XApp application, class JtApp */
    private final transient JtApp app;

    /* Data source, DSLContext, DataSource */
    private final transient DataPool pool;

    /*
     * Service Map
     */
    private final ConcurrentMap<String, IService> serviceMap = new ConcurrentHashMap<>();

    /*
     * Could not initialized outside package
     */
    AmbientEnvironment(final JtApp app) {
        /* Reference of application */
        this.app = app;
        /* ZPool created by Database */
        this.pool = DataPool.create(app.getSource());
        {
            /*
             * Service Init
             * serviceKey -> service
             */
            final List<IService> serviceList = Ux.Jooq.on(IServiceDao.class, DataPool.create(Database.getCurrent()))
                .fetch(KName.SIGMA, app.getSigma());
            this.serviceMap.putAll(Ut.elementZip(serviceList, IService::getKey, service -> service));
        }
    }

    @Fluent
    public AmbientEnvironment init() {
        /*
         * IApi + IService
         */
        this.initUris();
        /*
         * IJob + IService
         */
        this.initJobs();
        return this;
    }

    private void initJobs() {
        final List<IJob> jobList = Ux.Jooq.on(IJobDao.class, DataPool.create(Database.getCurrent()))
            .fetch(KName.SIGMA, this.app.getSigma());
        if (this.jobs.isEmpty()) {
            /*
             * Map for JOB + Service
             * serviceKey -> job
             * serviceKey -> service ( Cached )
             */
            final ConcurrentMap<String, IJob> jobMap = Ut.elementZip(jobList, IJob::getServiceId, job -> job);
            /* Job / Service Bind into data here */
            jobMap.keySet().stream()
                .map(serviceId -> new JtJob(jobMap.get(serviceId), this.serviceMap.get(serviceId))
                    /* Job Bind app id directly */
                    .<JtJob>bind(this.app.getAppId())
                )
                .forEach(entry -> this.jobs.put(entry.key(), entry));
        }
    }

    private void initUris() {
        final List<IApi> apiList = Ux.Jooq.on(IApiDao.class, DataPool.create(Database.getCurrent()))
            .fetch(KName.SIGMA, this.app.getSigma());
        if (this.uris.isEmpty()) {
            /*
             * Map for API + Service
             * serviceKey -> api
             * serviceKey -> service ( Cached )
             */
            final ConcurrentMap<String, IApi> apiMap = Ut.elementZip(apiList, IApi::getServiceId, api -> api);
            /* Uri / Service Bind into data here */
            apiMap.keySet().stream()
                .map(serviceId -> new JtUri(apiMap.get(serviceId), this.serviceMap.get(serviceId))
                    /* Job Bind app id directly */
                    .<JtUri>bind(this.app.getAppId()))
                .forEach(entry -> this.uris.put(entry.key(), entry));
        }
    }

    public Connection getConnection() {
        return Fn.getJvm(() -> this.pool.getDataSource().getConnection(), this.pool);
    }

    public DataPool getPool() {
        return this.pool;
    }

    public Set<JtUri> routes() {
        return new HashSet<>(this.uris.values());
    }

    public Set<JtJob> jobs() {
        return new HashSet<>(this.jobs.values());
    }

    /*
     * Cache flush for Job
     */
    public void flushJob(final JtJob job) {
        /*
         * serviceKey -> service (Cached)
         */
        final IService service = job.service();
        this.serviceMap.put(service.getKey(), service);
        /*
         * serviceKey -> job (JtJob)
         */
        this.jobs.put(service.getKey(), job);
    }

    /*
     * Cache flush for Uri
     */
    public void flushUri(final JtUri uri) {
        /*
         * serviceKey -> service (Cached)
         */
        final IService service = uri.service();
        this.serviceMap.put(service.getKey(), service);
        /*
         * serviceKey -> uri (JtUri)
         */
        this.uris.put(service.getKey(), uri);
    }
}
