package io.vertx.up.uca.rs.router;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("all")
public class FilterAxis implements Axis<Router> {
    private static final Annal LOGGER = Annal.get(FilterAxis.class);

    private static final ConcurrentMap<String, Set<Event>> FILTERS =
        ZeroAnno.getFilters();

    @Override
    public void mount(final Router router) {
        // Extract Event foreach
        FILTERS.forEach((path, events) -> events.forEach(event -> Fn.safeSemi(null == event, LOGGER,
            () -> LOGGER.warn(Info.NULL_EVENT, this.getClass().getName()),
            () -> {
                // Path for filter
                final Route route = router.route();

                Hub<Route> hub = Pool.CC_HUB_URI.pick(() -> Ut.instance(UriHub.class));
                // Fn.po?lThread(Pool.URIHUBS, () -> Ut.instance(UriHub.class));
                hub.mount(route, event);
                // Consumes/Produces
                hub = Pool.CC_HUB_MEDIA.pick(() -> Ut.instance(MediaHub.class));
                // Fn.po?lThread(Pool.MEDIAHUBS, () -> Ut.instance(MediaHub.class));
                hub.mount(route, event);
                // Filter Handler execution
                route.handler(context -> {
                    // Execute method
                    final Method method = event.getAction();
                    final Object proxy = event.getProxy();
                    // Call
                    this.execute(context, proxy, method);
                });
            }))
        );
    }

    private void execute(final RoutingContext context, final Object proxy, final Method method) {
        Fn.runAt(() -> Fn.jvmAt(() -> {
            // Init context;
            Ut.invoke(proxy, "init", context);
            // Extract Request/Response
            final HttpServerRequest request = context.request();
            final HttpServerResponse response = context.response();
            method.invoke(proxy, request, response);

            // Check whether called next or response
            if (!response.ended()) {
                context.next();
            }
        }, LOGGER), method, proxy);
    }
}
