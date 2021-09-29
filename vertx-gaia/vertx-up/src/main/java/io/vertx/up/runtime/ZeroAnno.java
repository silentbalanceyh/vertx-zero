package io.vertx.up.runtime;

import io.vertx.core.http.HttpMethod;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.web.origin.*;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Transfer Class<?> set to difference mapping.
 */
public class ZeroAnno {

    private static final Annal LOGGER = Annal.get(ZeroAnno.class);

    private final static Set<Class<?>>
        ENDPOINTS = new HashSet<>();
    private final static ConcurrentMap<Class<?>, ConcurrentMap<String, Class<?>>>
        PLUGINS = new ConcurrentHashMap<>();
    private final static Set<Receipt>
        RECEIPTS = new HashSet<>();
    private final static Set<Event>
        EVENTS = new HashSet<>();
    private final static ConcurrentMap<String, Set<Event>>
        FILTERS = new ConcurrentHashMap<>();
    private final static ConcurrentMap<ServerType, List<Class<?>>>
        AGENTS = new ConcurrentHashMap<>();
    private final static Set<Class<?>>
        WORKERS = new HashSet<>();
    private final static Set<Aegis>
        WALLS = new TreeSet<>();
    private final static ConcurrentMap<String, Method>
        IPCS = new ConcurrentHashMap<>();
    private final static Set<Class<?>>
        POINTER = new HashSet<>();
    private final static Set<Class<?>>
        TPS = new HashSet<>();
    private final static Set<Mission>
        JOBS = new HashSet<>();

    /*
     * Move to main thread to do init instead of static block initialization
     */
    public static void prepare() {
        /* 1.Scan the packages **/
        final Set<Class<?>> clazzes = ZeroPack.getClasses();
        /* EndPoint **/
        Inquirer<Set<Class<?>>> inquirer =
            Ut.singleton(EndPointInquirer.class);
        ENDPOINTS.addAll(inquirer.scan(clazzes));

        /* EndPoint -> Event **/
        Fn.safeSemi(!ENDPOINTS.isEmpty(),
            LOGGER,
            () -> {
                final Inquirer<Set<Event>> event = Ut.singleton(EventInquirer.class);
                EVENTS.addAll(event.scan(ENDPOINTS));
            });
        /* 1.1. Put Path Uri into Set */
        EVENTS.stream()
            .filter(Objects::nonNull)
            /* Only Uri Pattern will be extracted to URI_PATHS */
            .filter(item -> 0 < item.getPath().indexOf(":"))
            .forEach(ZeroUri::resolve);
        ZeroUri.report();

        /* Wall -> Authenticate, Authorize **/
        final Inquirer<Set<Aegis>> walls =
            Ut.singleton(WallInquirer.class);
        WALLS.addAll(walls.scan(clazzes));

        /* Filter -> WebFilter **/
        final Inquirer<ConcurrentMap<String, Set<Event>>> filters =
            Ut.singleton(FilterInquirer.class);
        FILTERS.putAll(filters.scan(clazzes));

        /* Queue **/
        inquirer = Ut.singleton(QueueInquirer.class);
        final Set<Class<?>> queues = inquirer.scan(clazzes);

        /* Queue -> Receipt **/
        Fn.safeSemi(!queues.isEmpty(),
            LOGGER,
            () -> {
                final Inquirer<Set<Receipt>> receipt = Ut.singleton(ReceiptInquirer.class);
                RECEIPTS.addAll(receipt.scan(queues));
            });

        /* Ipc Only **/
        final Inquirer<ConcurrentMap<String, Method>> ipc = Ut.singleton(IpcInquirer.class);
        IPCS.putAll(ipc.scan(clazzes));
        /* Agent **/
        final Inquirer<ConcurrentMap<ServerType, List<Class<?>>>> agent = Ut.singleton(AgentInquirer.class);
        AGENTS.putAll(agent.scan(clazzes));

        /* JSR330 Fix **/
        final Inquirer<Set<Class<?>>> pointer = Ut.singleton(PointerInquirer.class);
        POINTER.addAll(pointer.scan(clazzes));

        /* Tp Clients **/
        final Inquirer<Set<Class<?>>> tps = Ut.singleton(PluginInquirer.class);
        TPS.addAll(tps.scan(clazzes));

        /* Worker **/
        final Inquirer<Set<Class<?>>> worker = Ut.singleton(WorkerInquirer.class);
        WORKERS.addAll(worker.scan(clazzes));

        /* Jobs with description in zero */
        final Inquirer<Set<Mission>> jobs = Ut.singleton(JobInquirer.class);
        JOBS.addAll(jobs.scan(clazzes));

        /* Injections **/
        final Inquirer<ConcurrentMap<Class<?>, ConcurrentMap<String, Class<?>>>> afflux = Ut.singleton(AffluxInquirer.class);
        PLUGINS.putAll(afflux.scan(clazzes));
    }

    /**
     * Get all plugins
     *
     * @return plugin map
     */
    public static ConcurrentMap<Class<?>, ConcurrentMap<String, Class<?>>> getPlugins() {
        return PLUGINS;
    }

    /**
     * Get all agents.
     *
     * @return agent map
     */
    public static ConcurrentMap<ServerType, List<Class<?>>> getAgents() {
        return AGENTS;
    }

    /**
     * Injects
     *
     * @return pointer set
     */
    public static Set<Class<?>> getInjects() {
        return POINTER;
    }

    /**
     * Tp Clients
     *
     * @return client set
     */
    public static Set<Class<?>> getTps() {
        return TPS;
    }

    /**
     * Get Jobs for current
     */
    public static Set<Mission> getJobs() {
        return JOBS;
    }

    /**
     * Get all workers
     *
     * @return worker set
     */
    public static Set<Class<?>> getWorkers() {
        return WORKERS;
    }

    /**
     * Get all receipts
     *
     * @return receipts set
     */
    public static Set<Receipt> getReceipts() {
        return RECEIPTS;
    }

    /**
     * Get all endpoints
     *
     * @return endpoint set
     */
    public static Set<Class<?>> getEndpoints() {
        return ENDPOINTS;
    }

    public static ConcurrentMap<String, Method> getIpcs() {
        return IPCS;
    }

    /**
     * Get all envents
     *
     * @return event set
     */
    public static Set<Event> getEvents() {
        return EVENTS;
    }

    /**
     * Get all filters
     *
     * @return filter map JSR340
     */
    public static ConcurrentMap<String, Set<Event>> getFilters() {
        return FILTERS;
    }

    /**
     * Get all guards
     *
     * @return guard set
     */
    public static Set<Aegis> getWalls() {
        return WALLS;
    }

    public static String recoveryUri(final String uri, final HttpMethod method) {
        return ZeroUri.recovery(uri, method);
    }
}
