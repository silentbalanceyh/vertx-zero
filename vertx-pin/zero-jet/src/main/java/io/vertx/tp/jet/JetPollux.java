package io.vertx.tp.jet;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.tp.jet.atom.JtConfig;
import io.vertx.tp.jet.atom.JtUri;
import io.vertx.tp.jet.monitor.JtMonitor;
import io.vertx.tp.jet.uca.aim.*;
import io.vertx.tp.optic.environment.Ambient;
import io.vertx.tp.optic.environment.AmbientEnvironment;
import io.vertx.up.extension.PlugRouter;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroJet;
import io.vertx.up.uca.web.failure.CommonEndurer;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/*
 * Agent entry of dynamic deployment, this component is mount to router also.
 * 1) The dynamic router could be authorized by zero @Wall class
 * 2) The dynamic router will call connection pool of configuration, will manage all the routers in current system.
 * 3) The dynamic router will registry the routers information when booting
 */
public class JetPollux implements PlugRouter {
    /*
     * Multi Application environment here
     */
    private static final ConcurrentMap<String, AmbientEnvironment> AMBIENT = Ambient.getEnvironments();
    private static final AtomicBoolean UNREADY = new AtomicBoolean(Boolean.TRUE);

    private final transient JtMonitor monitor = JtMonitor.create(this.getClass());
    private transient JetCastor castor;

    @Override
    @SuppressWarnings("all")
    public void mount(final Router router, final JsonObject config) {
        // MONITOR
        this.monitor.agentConfig(config);
        if (Objects.isNull(AMBIENT) || AMBIENT.isEmpty()) {
            /*
             * 「Failure」Deployment failure
             */
            this.monitor.workerFailure();
        } else {
            /*
             * 「Booting」( Multi application deployment on Router )
             */
            final Set<JtUri> uriSet = AMBIENT.keySet().stream()
                .flatMap(appId -> AMBIENT.get(appId).routes().stream())
                /*
                 * Start up and bind `order` and `config`
                 */
                .map(uri -> uri.bind(this.getOrder())
                    .<JtUri>bind(Ut.deserialize(config.copy(), JtConfig.class)))
                /*
                 * Routing deployment
                 */
                .map(uri -> {
                    /*
                     * 「ZeroJet」Mount the uri into Zero Uri
                     */
                    ZeroJet.resolve(uri.method(), uri.path());
                    /*
                     * 「Route」
                     */
                    final Route route = router.route();
                    /*
                     * Single route registry
                     */
                    this.registryUri(route, uri);
                    return uri;
                }).collect(Collectors.toSet());
            /*
             * 「Starting」
             */
            if (Objects.nonNull(this.castor)) {
                /*
                 * Worker deployment
                 * Here caused `block thread issue`, if we deploy agent x 32 and set `instances` of worker to 64
                 * It means that the code below will execute 32 times, and then the system will
                 * deploy 32 x 64 worker instances, it may take long time to do deployment and casued `block thread`
                 * issue. Instead we set UNREADY flag to mean execute worker deployment one time.
                 * Each time the `vert.x` insteance will set worker threads here.
                 */
                if (UNREADY.getAndSet(Boolean.FALSE)) {
                    this.monitor.workerStart();
                    this.castor.startWorkers(uriSet);
                }
            }
        }
    }

    /*
     * Bind two components to the same Vertx instance
     */
    @Override
    public void bind(final Vertx vertx) {
        if (Objects.nonNull(vertx)) {
            this.castor = JetCastor.create(vertx);
        }
    }

    private void registryUri(final Route route, final JtUri uri) {
        // Uri, Method, Order
        route.path(uri.path()).order(uri.order()).method(uri.method());
        // Consumes / Produces
        uri.consumes().forEach(route::consumes);
        uri.produces().forEach(route::produces);
        /*
         * Major Route: EngineAim
         * 1) Pre-Condition
         *      IN_RULE
         *      IN_MAPPING
         *      IN_PLUG
         *      IN_SCRIPT
         * 2) Major code logical ( Could not be configured )
         * 3) Send logical
         *      3.1) Send current request to worker ( Ha )
         *      3.2) Send message to worker
         *      3.3) Let worker consume component
         */
        final JtAim pre = Fn.poolThread(Pool.AIM_PRE_HUBS, () -> Ut.instance(PreAim.class));
        final JtAim in = Fn.poolThread(Pool.AIM_IN_HUBS, () -> Ut.instance(InAim.class));
        final JtAim engine = Fn.poolThread(Pool.AIM_ENGINE_HUBS, () -> Ut.instance(EngineAim.class));
        final JtAim send = Fn.poolThread(Pool.AIM_SEND_HUBS, () -> Ut.instance(SendAim.class));
        route
            /* Basic parameter validation / 400 Bad Request */
            .handler(pre.attack(uri))
            /*
             * Four rule here
             * IN_RULE , IN_MAPPING, IN_PLUG, IN_SCRIPT
             */
            .handler(in.attack(uri))
            /*
             * Handler major process and workflow
             */
            .handler(engine.attack(uri))
            /*
             * Message sender, connect to event bus
             */
            .handler(send.attack(uri))
            /*
             * Failure Handler when error occurs
             */
            .failureHandler(CommonEndurer.create());
    }
}
