package io.vertx.up.boot.options;

import io.vertx.up.eon.em.container.ServerType;

public class RxServerVisitor extends HttpServerVisitor {

    @Override
    public ServerType serverType() {
        return ServerType.RX;
    }
}
