package io.vertx.up.runtime;

import com.google.inject.Injector;
import io.aeon.atom.KSwitcher;
import io.aeon.uca.web.origin.HQaSInquirer;
import io.horizon.runtime.Runner;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.macrocosm.specification.nc.HAeon;
import io.vertx.core.http.HttpMethod;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.boot.origin.*;
import io.vertx.up.commune.secure.Aegis;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.fn.Fn;
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

    /*
     * Class Scanner
     */
    private final static Set<Class<?>> ENDPOINTS = new HashSet<>();
    private final static Set<Class<?>> QUEUES = new HashSet<>();
    private final static Set<Class<?>> WORKERS = new HashSet<>();
    private final static Set<Class<?>> TPS = new HashSet<>();
    private final static ConcurrentMap<String, Method> QAS = new ConcurrentHashMap<>();

    /*
     * Component Scanner
     */
    private final static Set<Receipt> RECEIPTS = new HashSet<>();
    private final static Set<Event> EVENTS = new HashSet<>();
    private final static ConcurrentMap<String, Set<Event>> FILTERS = new ConcurrentHashMap<>();
    private final static ConcurrentMap<ServerType, List<Class<?>>> AGENTS = new ConcurrentHashMap<>();
    private final static Set<Aegis> WALLS = new TreeSet<>();
    private final static ConcurrentMap<String, Method> IPCS = new ConcurrentHashMap<>();
    private final static Set<Mission> JOBS = new HashSet<>();
    private final static Set<Remind> SOCKS = new HashSet<>();
    private static final Cc<String, Set<Aegis>> CC_WALL = Cc.open();

    private static final Set<Class<?>> CLASS_SET;
    private static final Injector DI;

    static {
        CLASS_SET = ZeroPack.getClasses();
        final Inquirer<Injector> guice = Ut.singleton(GuiceInquirer.class);
        DI = guice.scan(CLASS_SET);
        LOGGER.info("Zero Zone DI Environment.... Size= {}", String.valueOf(CLASS_SET.size()));
    }

    /*
     * Move to main thread to do init instead of static block initialization
     * Here all the class must be prepared
     */
    public static void configure() {
        /*
         * Phase 1:
         * -- Scan the whole environment to extract all classes those will be analyzed.
         * -- Guice Module Start to extract all ( DI clazz )
         * -- Class<?> Seeking ( EndPoint, Queue, HQueue )
         */
        final long start = System.currentTimeMillis();
        final long end = System.currentTimeMillis();
        LOGGER.info("Zero Timer: Scanning = {0} ms",
            String.valueOf(end - start));
        Runner.run("meditate-class",
            // @EndPoint
            () -> {
                final Inquirer<Set<Class<?>>> inquirer = Ut.singleton(EndPointInquirer.class);
                ENDPOINTS.addAll(inquirer.scan(CLASS_SET));
            },
            // @Queue
            () -> {
                final Inquirer<Set<Class<?>>> inquirer = Ut.singleton(QueueInquirer.class);
                QUEUES.addAll(inquirer.scan(CLASS_SET));
            },
            // @QaS
            () -> {
                /* QAS */
                final HAeon aeon = KSwitcher.aeon();
                if (Objects.nonNull(aeon)) {
                    /* Aeon System Enabled */
                    final Inquirer<ConcurrentMap<String, Method>> inquirer = Ut.singleton(HQaSInquirer.class);
                    QAS.putAll(inquirer.scan(CLASS_SET));
                }
            },
            // TpClients Plugins
            () -> {
                final Inquirer<Set<Class<?>>> tps = Ut.singleton(PluginInquirer.class);
                TPS.addAll(tps.scan(CLASS_SET));
            },
            // Worker Class
            () -> {
                final Inquirer<Set<Class<?>>> worker = Ut.singleton(WorkerInquirer.class);
                WORKERS.addAll(worker.scan(CLASS_SET));
            }
        );


        /*
         * Phase 2:
         * Component Scan of following:
         * -- @EndPoint -> Event
         * -- @Wall
         * -- @WebFilter
         * -- @Queue/@QaS
         * -- @Ipc
         * -- Agent Component
         * -- @WebSock
         * -- @Job
         */
        Runner.run("meditate-component",
            // @EndPoint -> Event
            () -> Fn.runAt(!ENDPOINTS.isEmpty(), LOGGER, () -> {
                final Inquirer<Set<Event>> event = Ut.singleton(EventInquirer.class);
                EVENTS.addAll(event.scan(ENDPOINTS));
                /* 1.1. Put Path Uri into Set */
                EVENTS.stream().filter(Objects::nonNull)
                    /* Only Uri Pattern will be extracted to URI_PATHS */
                    .filter(item -> 0 < item.getPath().indexOf(":"))
                    .forEach(ZeroUri::resolve);
                ZeroUri.report();
            }),
            // @Wall -> Authenticate, Authorize
            () -> {
                final Inquirer<Set<Aegis>> walls = Ut.singleton(WallInquirer.class);
                WALLS.addAll(walls.scan(CLASS_SET));
            },
            // @WebFilter -> JSR340
            () -> {
                final Inquirer<ConcurrentMap<String, Set<Event>>> filters = Ut.singleton(FilterInquirer.class);
                FILTERS.putAll(filters.scan(CLASS_SET));
            },
            // @Queue/@QaS -> Receipt
            () -> Fn.runAt(!QUEUES.isEmpty(), LOGGER, () -> {
                final Inquirer<Set<Receipt>> receipt = Ut.singleton(ReceiptInquirer.class);
                RECEIPTS.addAll(receipt.scan(QUEUES));
            }),
            // @Ipc -> IPC Only
            () -> {
                final Inquirer<ConcurrentMap<String, Method>> ipc = Ut.singleton(IpcInquirer.class);
                IPCS.putAll(ipc.scan(CLASS_SET));
            },
            // Agent Component
            () -> {
                final Inquirer<ConcurrentMap<ServerType, List<Class<?>>>> agent = Ut.singleton(AgentInquirer.class);
                AGENTS.putAll(agent.scan(CLASS_SET));
            },
            // @WebSock
            () -> {
                final Inquirer<Set<Remind>> socks = Ut.singleton(SockInquirer.class);
                SOCKS.addAll(socks.scan(CLASS_SET));
            },
            // @Job
            () -> {
                final Inquirer<Set<Mission>> jobs = Ut.singleton(JobInquirer.class);
                JOBS.addAll(jobs.scan(CLASS_SET));
            }
        );
        LOGGER.info("Zero Timer: Meditate = {0} ms", String.valueOf(System.currentTimeMillis() - end));
    }

    public static Method getQaS(final String address) {
        return QAS.getOrDefault(address, null);
    }

    public static Injector getDi() {
        return DI;
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

    public static Set<Remind> getSocks() {
        return SOCKS;
    }

    /**
     * Get all filters
     *
     * @return filter map JSR340
     */
    public static ConcurrentMap<String, Set<Event>> getFilters() {
        return FILTERS;
    }

    public static Cc<String, Set<Aegis>> getWalls() {
        if (CC_WALL.isEmpty()) {
            // To Avoid Filling the value more than once
            WALLS.forEach(wall -> {
                final ConcurrentMap<String, Set<Aegis>> store = CC_WALL.store();
                if (!store.containsKey(wall.getPath())) {
                    store.put(wall.getPath(), new TreeSet<>());
                }
                /*
                 * 1. group by `path`, when you define more than one wall in one path, you can collect
                 * all the wall into Set.
                 * 2. The order will be re-calculated by each group
                 * 3. But you could not define `path + order` duplicated wall
                 */
                store.get(wall.getPath()).add(wall);
            });
        }
        return CC_WALL;
    }

    public static String recoveryUri(final String uri, final HttpMethod method) {
        return ZeroUri.recovery(uri, method);
    }
}
