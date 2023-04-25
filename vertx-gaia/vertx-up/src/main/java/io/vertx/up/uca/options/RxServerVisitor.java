package io.vertx.up.uca.options;

import io.horizon.eon.em.container.ServerType;

public class RxServerVisitor extends HttpServerVisitor {

    @Override
    public ServerType serverType() {
        return ServerType.RX;
    }
}
