package io.vertx.up.uca.rs.router;

import io.vertx.core.Future;
import io.vertx.core.SockOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.Orders;
import io.vertx.up.exception.web._500ReturnNullException;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.uca.invoker.InvokerUtil;
import io.vertx.up.uca.rs.Axis;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

public class SockAxis implements Axis<Router> {

    private static final Set<Remind> SOCKS = ZeroAnno.getSocks();
    private SockOptions options;
    private Vertx vertxRef;

    public Axis<Router> bind(final Vertx vertxRef, final SockOptions options) {
        this.vertxRef = vertxRef;
        this.options = options;
        return this;
    }

    @Override
    public void mount(final Router router) {
        if (Objects.nonNull(this.options)) {
            // Iterator the SOCKS variables
            SOCKS.forEach(sock -> {
                /*
                 * Here for each Remind, the framework defined following way to enable
                 *
                 */
                // Create new Route
                final Route route = router.route();
                // Mount the handler into Route
                this.mount(route, sock);
            });
        }
    }

    private void mount(final Route route, final Remind remind) {
        // Route Initializing
        final String pathRoot = Constants.DEFAULT_WEBSOCKET + "/*";
        route.path(pathRoot).order(Orders.SOCK);
        // SockJSHandler / SockJSBridge ( Part )
        final SockJSHandlerOptions optHandler = this.options.configHandler();
        final SockJSHandler handler = SockJSHandler.create(this.vertxRef, optHandler);
        route.subRouter(handler.socketHandler(socket -> {
            final String path = remind.getAddress();
            System.out.println(socket.uri() + "," + socket.webUser() + ", " + path);
            // Internal Calling on Remind
            final String inputAddress = remind.getInputAddress();
            // There are input from inputAddress
            final EventBus bus = this.vertxRef.eventBus();
            bus.<Envelop>consumer(inputAddress, message -> this.invoke(message, remind, socket));
        }));
    }

    @SuppressWarnings("all")
    private void invoke(final Message<Envelop> message, final Remind remind, final SockJSSocket socket) {
        final Object proxy = remind.getProxy();
        final Method method = remind.getMethod();
        // Return value here.
        final Object returnValue = InvokerUtil.invokeCall(proxy, method, message.body());
        // Null Pointer return value checking
        Fn.out(Objects.isNull(returnValue), _500ReturnNullException.class, this.getClass(), method);
        final Future future = (Future) returnValue;
        future.onSuccess(response -> {
            System.out.println(response);
            socket.write(response.toString());
        });
        future.onFailure(error -> {
            if (Objects.nonNull(error)) {
                ((Throwable) error).printStackTrace();
            }
        });
    }
}
