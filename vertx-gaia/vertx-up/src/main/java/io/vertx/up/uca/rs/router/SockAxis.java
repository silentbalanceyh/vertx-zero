package io.vertx.up.uca.rs.router;

import io.vertx.core.SockOptions;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.eon.Orders;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

import static io.vertx.up.eon.Constants.DEFAULT_WEBSOCKET;

public class SockAxis implements Axis<Router> {

    private static final Set<Remind> SOCKS = ZeroAnno.getSocks();
    private SockOptions options;

    public Axis<Router> bind(final SockOptions options) {
        this.options = options;
        return this;
    }

    @Override
    public void mount(final Router router) {
        if (Objects.nonNull(this.options)) {
            // Iterator the SOCKS variables
            SOCKS.forEach(sock -> {
                // Create new Route
                final Route route = router.route();
                // Mount the handler into Route
                this.mount(route, sock);
            });
        }
    }

    private void mount(final Route route, final Remind remind) {
        // Route Initializing
        final String path = remind.getAddress();
        final String uri = Ut.ioPath(DEFAULT_WEBSOCKET, path);
        route.path(uri).order(Orders.SOCK);
        //
    }
}
