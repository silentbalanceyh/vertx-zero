package io.vertx.mod.jet.init;

import cn.vertxup.jet.domain.tables.daos.IApiDao;
import cn.vertxup.jet.domain.tables.daos.IJobDao;
import cn.vertxup.jet.domain.tables.daos.IServiceDao;
import cn.vertxup.jet.domain.tables.pojos.IApi;
import cn.vertxup.jet.domain.tables.pojos.IJob;
import cn.vertxup.jet.domain.tables.pojos.IService;
import io.horizon.exception.boot.AmbientConnectException;
import io.horizon.uca.log.Annal;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.modello.atom.app.KDS;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mod.jet.atom.JtJob;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.database.DataPool;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.jet.refine.Jt.LOG;

/*
 * Cross Data Object
 * 1) App / Source information here
 * 2) Pool ( DSLContext, Connection, DataSource )
 */
public class ServiceEnvironment {

    private static final Annal LOGGER = Annal.get(ServiceEnvironment.class);
    /* Pool of Jobs, it will be consumed by each application */
    private final transient ConcurrentMap<String, JtJob> jobs
        = new ConcurrentHashMap<>();
    /* Pool of JtUri */
    private final transient ConcurrentMap<String, JtUri> uris
        = new ConcurrentHashMap<>();

    /* XApp application, class JtApp */
    private final transient HArk ark;
    private final transient Set<String> condition = new HashSet<>();
    private final transient DataPool poolMeta;
    /*
     * Service Map
     */
    private final ConcurrentMap<String, IService> serviceMap = new ConcurrentHashMap<>();
    /* Data source, DSLContext, DataSource */
    private transient DataPool pool;

    /**
     * 包内初始化
     *
     * @param ark 应用配置容器
     */
    ServiceEnvironment(final HArk ark) {
        this.ark = ark;
        final HApp app = ark.app();

        final String sigma = app.option(KName.SIGMA);
        if (Ut.isNil(sigma)) {
            throw new AmbientConnectException(this.getClass());
        }
        this.condition.add(sigma);

        // 是否配置了动态数据源
        // 1. 动态数据库         dynamic
        // 2. 静态元数据库       database
        final KDS<Database> kds = ark.database();
        if (Objects.nonNull(kds.dynamic())) {
            this.pool = DataPool.create(kds.dynamic());
        }
        this.poolMeta = DataPool.create(kds.database());
    }

    @Fluent
    public Future<ServiceEnvironment> init(final Vertx vertx) {

        return this.initService(vertx).compose(nil -> {

            final List<Future<Boolean>> futures = new ArrayList<>();
            /*
             * IApi + IService
             */
            futures.add(this.initUris(vertx));
            /*
             * IJob + IService
             */
            futures.add(this.initJobs(vertx));
            return Fn.combineT(futures).compose(res -> Ux.future(this));
        });
    }

    private Future<Boolean> initService(final Vertx vertx) {
        final IServiceDao serviceDao = new IServiceDao(this.poolMeta.getExecutor().configuration(), vertx);
        return serviceDao.findManyBySigma(this.condition).compose(services -> {
            this.serviceMap.putAll(Ut.elementZip(services, IService::getKey, service -> service));
            LOG.Init.info(LOGGER, "AE ( {0} ) Service initialized !!!",
                String.valueOf(this.serviceMap.keySet().size()));
            return Ux.future(Boolean.TRUE);
        });
    }

    private Future<Boolean> initJobs(final Vertx vertx) {
        final IJobDao jobDao = new IJobDao(this.poolMeta.getExecutor().configuration(), vertx);
        if (this.jobs.isEmpty()) {
            /*
             * Map for JOB + Service
             * serviceKey -> job
             * serviceKey -> service ( Cached )
             */
            return jobDao.findManyBySigma(this.condition).compose(jobList -> {
                final ConcurrentMap<String, IJob> jobMap = Ut.elementZip(jobList, IJob::getServiceId, job -> job);
                /* Job / Service Bind into data here */
                jobMap.keySet().stream()
                    .map(serviceId -> new JtJob(jobMap.get(serviceId), this.serviceMap.get(serviceId))
                        /* Job Bind app id directly */
                        .<JtJob>bind(this.ark)
                    )
                    .forEach(entry -> this.jobs.put(entry.key(), entry));
                LOG.Init.info(LOGGER, "AE ( {0} ) Jobs initialized !!!",
                    String.valueOf(this.jobs.keySet().size()));
                return Ux.future(Boolean.TRUE);
            });
        } else {
            return Ux.future(Boolean.TRUE);
        }
    }

    private Future<Boolean> initUris(final Vertx vertx) {
        final IApiDao apiDao = new IApiDao(this.poolMeta.getExecutor().configuration(), vertx);
        if (this.uris.isEmpty()) {
            /*
             * Map for API + Service
             * serviceKey -> api
             * serviceKey -> service ( Cached )
             */
            return apiDao.findManyBySigma(this.condition).compose(apiList -> {
                final ConcurrentMap<String, IApi> apiMap = Ut.elementZip(apiList, IApi::getServiceId, api -> api);
                /* Uri / Service Bind into data here */
                apiMap.keySet().stream()
                    .map(serviceId -> new JtUri(apiMap.get(serviceId), this.serviceMap.get(serviceId))
                        /* Job Bind app id directly */
                        .<JtUri>bind(this.ark))
                    .forEach(entry -> this.uris.put(entry.key(), entry));
                LOG.Init.info(LOGGER, "AE ( {0} ) Api initialized !!!",
                    String.valueOf(this.uris.keySet().size()));
                return Ux.future(Boolean.TRUE);
            });
        } else {
            return Ux.future(Boolean.TRUE);
        }
    }

    public Connection getConnection() {
        return Fn.failOr(() -> this.pool.getDataSource().getConnection(), this.pool);
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
