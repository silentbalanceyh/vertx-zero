package io.vertx.rx.rs.router;

import io.horizon.specification.boot.HAxis;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.rx.micro.ZeroRxEndurer;
import io.vertx.rx.rs.dispatch.StandardVerifier;
import io.vertx.rxjava3.ext.web.Route;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.up.atom.agent.Depot;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.backbone.Aim;
import io.vertx.up.backbone.Sentry;
import io.vertx.up.backbone.dispatch.ModeSplitter;
import io.vertx.up.backbone.router.Hub;
import io.vertx.up.backbone.router.Verifier;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.util.Ut;

import java.util.Set;

public class EventAxis implements HAxis<Router> {
    private static final Annal LOGGER = Annal.get(EventAxis.class);
    /**
     * Extract all events that will be generated route.
     */
    private static final Set<Event> EVENTS = ZeroAnno.getEvents();


    private static final Cc<String, ModeSplitter> CC_THREADS = Cc.openThread();
    private static final Cc<String, Sentry<RoutingContext>> CC_VERIFIERS = Cc.openThread();

    private static final Cc<String, Hub<Route>> CC_URI = Cc.openThread();
    private static final Cc<String, Hub<Route>> CC_MEDIA = Cc.openThread();

    /**
     * Splitter
     */
    private transient final ModeSplitter splitter =
        CC_THREADS.pick(() -> Ut.instance(ModeSplitter.class));
    // Fn.po?lThread(Pool.THREADS, () -> Ut.instance(ModeSplitter.class));

    /**
     * Sentry
     */
    private transient final Sentry<RoutingContext> verifier =
        CC_VERIFIERS.pick(() -> Ut.instance(StandardVerifier.class));
    // Fn.po?lThread(Pool.VERIFIERS, () -> Ut.instance(StandardVerifier.class));

    @Override
    @SuppressWarnings("all")
    public void mount(final Router router) {
        // Extract Event foreach
        EVENTS.forEach(event -> {
            // Build Route and connect to each Action
            Fn.runAt(null == event, LOGGER,
                () -> LOGGER.warn(Info.NULL_EVENT, this.getClass().getName()),
                () -> {
                    // 1. Verify
                    Verifier.verify(event);

                    final Route route = router.route();
                    // 2. Path, Method, Order
                    Hub<Route> hub = CC_URI.pick(() -> Ut.instance(UriHub.class));
                    // Fn.po?lThread(Pool.URIHUBS, () -> Ut.instance(UriHub.class));
                    hub.mount(route, event);
                    // 3. Consumes/Produces
                    hub = CC_MEDIA.pick(() -> Ut.instance(MediaHub.class));
                    // Fn.po?lThread(Pool.MEDIAHUBS, () -> Ut.instance(MediaHub.class));
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
