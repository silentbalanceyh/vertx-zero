package io.vertx.rx.rs.router;

import io.vertx.reactivex.ext.web.Route;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.rx.micro.ZeroRxEndurer;
import io.vertx.rx.rs.dispatch.StandardVerifier;
import io.vertx.up.atom.agent.Depot;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.Aim;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.uca.rs.Sentry;
import io.vertx.up.uca.rs.dispatch.ModeSplitter;
import io.vertx.up.uca.rs.router.Hub;
import io.vertx.up.uca.rs.router.Verifier;
import io.vertx.up.util.Ut;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAnno;

import java.util.Set;

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
            Fn.poolThread(Pool.THREADS,
                    () -> Ut.instance(ModeSplitter.class));
    /**
     * Sentry
     */
    private transient final Sentry<RoutingContext> verifier =
            Fn.poolThread(Pool.VERIFIERS,
                    () -> Ut.instance(StandardVerifier.class));

    @Override
    public void mount(final Router router) {
        // Extract Event foreach
        EVENTS.forEach(event -> {
            // Build Route and connect to each Action
            Fn.safeSemi(null == event, LOGGER,
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
                        final Aim aim = this.splitter.distribute(event);
                        route.handler(this.verifier.signal(depot))
                                .failureHandler(ZeroRxEndurer.create());
                    });
        });
    }
}
