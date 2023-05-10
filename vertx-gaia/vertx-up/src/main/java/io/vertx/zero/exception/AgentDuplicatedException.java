package io.vertx.zero.exception;

import io.horizon.exception.BootingException;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.util.Ut;

import java.util.Set;

public class AgentDuplicatedException extends BootingException {

    public AgentDuplicatedException(final Class<?> clazz,
                                    final ServerType type,
                                    final int numbber,
                                    final Set<String> agents) {
        super(clazz, numbber, type, Ut.fromJoin(agents));
    }

    @Override
    public int getCode() {
        return -40004;
    }
}
