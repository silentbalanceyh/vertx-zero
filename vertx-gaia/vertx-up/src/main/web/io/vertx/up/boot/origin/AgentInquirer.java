package io.vertx.up.boot.origin;

import io.horizon.uca.log.Annal;
import io.vertx.up.annotations.Agent;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * This component is for @Agent annotated class
 */
public class AgentInquirer implements
    Inquirer<ConcurrentMap<ServerType, List<Class<?>>>> {

    @Override
    public ConcurrentMap<ServerType, List<Class<?>>> scan(final Set<Class<?>> classes) {
        final Set<Class<?>> agents =
            classes.stream()
                .filter((item) -> item.isAnnotationPresent(Agent.class))
                .collect(Collectors.toSet());
        return Ut.elementGroup(agents, this::getAgentKey,
            (item) -> item);
    }

    public ServerType getAgentKey(final Class<?> clazz) {
        final Annal logger = Annal.get(this.getClass());
        return Fn.runOr(clazz.isAnnotationPresent(Agent.class), logger,
            () -> Ut.invoke(clazz.getDeclaredAnnotation(Agent.class), "type"),
            () -> null);
    }
}
