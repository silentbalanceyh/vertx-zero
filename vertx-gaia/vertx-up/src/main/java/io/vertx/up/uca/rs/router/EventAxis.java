package io.vertx.up.uca.rs.router;

import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.agent.Depot;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.runtime.soul.UriAeon;
import io.vertx.up.uca.rs.Aim;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.uca.rs.Sentry;
import io.vertx.up.uca.rs.dispatch.ModeSplitter;
import io.vertx.up.uca.rs.dispatch.StandardVerifier;
import io.vertx.up.uca.web.failure.CommonEndurer;
import io.vertx.up.util.Ut;

import java.util.Set;

@SuppressWarnings("all")
public class EventAxis implements Axis<Router> {
    private static final Annal LOGGER = Annal.get(EventAxis.class);
    /**
     * Extract all events that will be generated route.
     */
    private static final Set<Event> EVENTS =
        ZeroAnno.getEvents();
    /**
     * Splitter
     */
    private transient final ModeSplitter splitter =
        Fn.poolThread(Pool.THREADS, () -> Ut.instance(ModeSplitter.class));
    /**
     * Sentry
     */
    private transient final Sentry<RoutingContext> verifier =
        Fn.poolThread(Pool.VERIFIERS, () -> Ut.instance(StandardVerifier.class));

    /**
     * Secreter for security limitation
     * 1. Authorization
     * 2. Authorize
     */

    @Override
    public void mount(final Router router) {
        /*
         * It's new engine for routing dynamic deployment in zero framework
         * The class `ZeroAeon` could manage routing reference that stored into
         * zero framework `Router` pool here
         * **/
        UriAeon.connect(router);

        /*
         * Extract Event foreach
         */
        EVENTS.forEach(event -> Fn.safeSemi(null == event, LOGGER,
            () -> LOGGER.warn(Info.NULL_EVENT, this.getClass().getName()),
            () -> {
                // 1. Verify
                Verifier.verify(event);

                final Route route = router.route();

                // 2. Path, Method, Order
                Hub<Route> hub = Fn.poolThread(Pool.URIHUBS,
                    () -> Ut.instance(UriHub.class));
                hub.mount(route, event);
                // 3. Consumes/Produces
                hub = Fn.poolThread(Pool.MEDIAHUBS,
                    () -> Ut.instance(MediaHub.class));
                hub.mount(route, event);

                // 4. Request validation
                final Depot depot = Depot.create(event);
                // 5. Request workflow executor: handler
                final Aim<RoutingContext> aim = this.splitter.distribute(event);

                /*
                 * 6. Handler chain
                 * 1) Mime Analyzer ( Build arguments )
                 * 2) Validation
                 * 3) Execute handler ( Code Logical )
                 * 4) Uniform failure handler
                 */
                route.blockingHandler(this.verifier.signal(depot))
                    .failureHandler(CommonEndurer.create())
                    .blockingHandler(aim.attack(event))
                    .failureHandler(CommonEndurer.create());
            }));
    }
}
