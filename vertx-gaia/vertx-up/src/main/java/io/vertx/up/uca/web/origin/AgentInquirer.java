package io.vertx.up.uca.web.origin;

import io.vertx.up.annotations.Agent;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.runtime.ZeroHelper;
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
        return Ut.elementGroup(agents, ZeroHelper::getAgentKey,
            (item) -> item);
    }
}
