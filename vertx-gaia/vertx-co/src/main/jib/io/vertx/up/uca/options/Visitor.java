package io.vertx.up.uca.options;

import io.horizon.exception.ProgramException;

public interface Visitor<T> {
    /**
     * @return The generic type of config
     * @throws ProgramException The zero exception that prevent start up
     */
    T visit(String... keys) throws ProgramException;
}
