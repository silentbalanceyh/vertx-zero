package io.vertx.rx.rs.router;

import io.horizon.specification.boot.HAxis;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;
import io.vertx.up.eon.KWeb;

public class RouterAxis implements HAxis<Router> {

    @Override
    public void mount(final Router router) {
        // 1. Cookie, Body
        // 2. Enabled by default
        // router.route().order(Orders.COOKIE).handler(CookieHandler.create());
        router.route().order(KWeb.ORDER.BODY)
            .handler(BodyHandler.create());
        // 2. Session
    }
}
