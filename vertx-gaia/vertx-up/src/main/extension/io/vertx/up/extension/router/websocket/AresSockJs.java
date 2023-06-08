package io.vertx.up.extension.router.websocket;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.eon.KWeb;
import io.vertx.up.extension.AbstractAres;
import io.vertx.up.extension.router.AresGrid;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AresSockJs extends AbstractAres {

    /*
     * This configuration is for `SockJs` only, it means that there is no secure channel
     * because of that SockJs has no security definition, in this situation this channel
     * is only one in current situation and we could not define any security part in
     * this channel
     */
    private final Set<Remind> sockOk;

    public AresSockJs(final Vertx vertx) {
        super(vertx);
        this.sockOk = AresGrid.wsPublish();
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
            final String path = KWeb.ADDR.API_WEBSOCKET + "/*";
            route.path(path).order(KWeb.ORDER.SOCK);

            // config -> SockJsHandlerOptions
            final SockJSHandlerOptions options = new SockJSHandlerOptions(config);
            final SockJSHandler handler = SockJSHandler.create(this.vertx(), options);
            route.subRouter(handler.socketHandler(socket -> {
                // Do Code Logical of execution
            }));
        });
    }
}
