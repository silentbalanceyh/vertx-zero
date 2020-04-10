package io.vertx.up.uca.options;

import io.vertx.up.eon.em.ServerType;

public class SockServerVisitor extends HttpServerVisitor {

    @Override
    public ServerType getType() {
        return ServerType.SOCK;
    }
}
