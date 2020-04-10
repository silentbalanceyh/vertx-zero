package io.vertx.up.fn;

import io.vertx.up.exception.ZeroException;

@FunctionalInterface
public interface ZeroActuator {

    void execute() throws ZeroException;
}
