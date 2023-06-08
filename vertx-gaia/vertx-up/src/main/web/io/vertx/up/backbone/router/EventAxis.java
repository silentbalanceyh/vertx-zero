package io.vertx.up.backbone.router;

import io.horizon.specification.boot.HAxis;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.agent.Depot;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.backbone.Aim;
import io.vertx.up.backbone.Sentry;
import io.vertx.up.backbone.dispatch.ModeSplitter;
import io.vertx.up.backbone.dispatch.StandardVerifier;
import io.vertx.up.boot.handler.CommonEndurer;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.uca.soul.UriAeon;
import io.vertx.up.util.Ut;

import java.util.Set;

@SuppressWarnings("all")
public class EventAxis implements HAxis<Router> {
    private static final Annal LOGGER = Annal.get(EventAxis.class);
    /**
     * Extract all events that will be generated route.
     */
    private static final Set<Event> EVENTS = ZeroAnno.getEvents();
    private static final Cc<String, ModeSplitter> CC_SPLITTER = Cc.openThread();
    private static final Cc<String, Sentry<RoutingContext>> CC_VERIFIER = Cc.openThread();
    /**
     * Splitter
     */
    private transient final ModeSplitter splitter =
        CC_SPLITTER.pick(() -> Ut.instance(ModeSplitter.class));
    // Fn.po?lThread(Pool.THREADS, () -> Ut.instance(ModeSplitter.class));
    /**
     * Sentry
     */
    private transient final Sentry<RoutingContext> verifier =
        CC_VERIFIER.pick(() -> Ut.instance(StandardVerifier.class));
    // Fn.po?lThread(Pool.VERIFIERS, () -> Ut.instance(StandardVerifier.class));

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
        EVENTS.forEach(event -> Fn.runAt(null == event, LOGGER,
            () -> LOGGER.warn(INFO.NULL_EVENT, this.getClass().getName()),
            () -> {
                // 1. Verify
                Verifier.verify(event);

                final Route route = router.route();

                // 2. Path, Method, Order
                Hub<Route> hub = CACHE.CC_HUB_URI.pick(() -> Ut.instance(UriHub.class));
                // Fn.po?lThread(Pool.URIHUBS, () -> Ut.instance(UriHub.class));
                hub.mount(route, event);
                // 3. Consumes/Produces
                hub = CACHE.CC_HUB_MEDIA.pick(() -> Ut.instance(MediaHub.class));
                // Fn.po?lThread(Pool.MEDIAHUBS, () -> Ut.instance(MediaHub.class));
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
                 * 4) Uniform handler handler
                 */
                route.handler(this.verifier.signal(depot))
                    .failureHandler(CommonEndurer.create())
                    .handler(aim.attack(event))
                    .failureHandler(CommonEndurer.create());
            }));
    }
}
