package io.vertx.up.extension.router.websocket;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.Orders;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.runtime.ZeroAnno;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AresSockJs extends AbstractAres {

    private static final Set<Remind> SOCKS = ZeroAnno.getSocks();

    /*
     * This configuration is for `SockJs` only, it means that there is no secure channel
     * because of that SockJs has no security definition, in this situation this channel
     * is only one in current situation and we could not define any security part in
     * this channel
     */
    private final Set<Remind> sockOk;

    public AresSockJs(final Vertx vertx) {
        super(vertx);
        this.sockOk = SOCKS.stream().filter(sock -> !sock.isSecure()).collect(Collectors.toSet());
    }

    @Override
    public void mount(final Router router, final JsonObject config) {
        // Iterator the SOCKS
        this.sockOk.forEach(sock -> {
            /*
             * Here for each `secure = false` Remind, the framework
             * define following way to enable websocket
             */
            final Route route = router.route();
            final String path = Constants.DEFAULT_WEBSOCKET + "/*";
            route.path(path).order(Orders.SOCK);

            // config -> SockJsHandlerOptions
            final SockJSHandlerOptions options = new SockJSHandlerOptions(config);
            final SockJSHandler handler = SockJSHandler.create(this.vertx(), options);
            route.subRouter(handler.socketHandler(socket -> {
                // Do Code Logical of execution
            }));
        });
    }
}
