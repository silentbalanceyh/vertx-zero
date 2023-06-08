package io.vertx.up.runtime;

import io.horizon.eon.VMessage;
import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.AgentDuplicatedException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Start up tools shared in
 * Web EmApp & Rx EmApp
 */
public final class ZeroAgent {

    private static final Annal LOGGER = Annal.get(ZeroAgent.class);

    /**
     * Agent calculation
     *
     * @param defaultAgents default agent classes array
     * @param internals     default internal agent class
     *
     * @return The map to stored agent class for each ServerType here
     */
    public static ConcurrentMap<ServerType, Class<?>> agentCommon(
        final ServerType category,
        final Class<?>[] defaultAgents,
        final ConcurrentMap<ServerType, Class<?>> internals
    ) {
        final ConcurrentMap<ServerType, List<Class<?>>> agents =
            agentCombine(category, internals);
        final ConcurrentMap<ServerType, Boolean> defines = isAgentDefined(agents, defaultAgents);
        final ConcurrentMap<ServerType, Class<?>> ret =
            new ConcurrentHashMap<>();
        // Fix Boot
        // 1. If defined, use default
        Ut.itMap(agents, (type, list) -> {
            // 2. Defined -> You have defined
            Fn.runAt(defines.containsKey(type) && defines.get(type), LOGGER,
                () -> {
                    // Use user-defined Agent instead.
                    final Class<?> found = Ut.elementFind(list,
                        (item) -> internals.get(type) != item);
                    if (null != found) {
                        ret.put(type, found);
                    }
                }, () -> {
                    // Use internal defined ( system defaults )
                    final Class<?> found = Ut.elementFind(list,
                        (item) -> internals.get(type) == item);
                    if (null != found) {
                        LOGGER.info(VMessage.Motor.AGENT_DEFINED, found, type);
                        ret.put(type, found);
                    }
                });
        });
        // 2.Filter
        return agentFilter(ret);
    }

    private static ConcurrentMap<ServerType, Class<?>> agentFilter(
        final ConcurrentMap<ServerType, Class<?>> agents) {
        // Check Rpc Enabled
        if (ZeroOption.getRpcOptions().isEmpty()) {
            agents.remove(ServerType.IPC);
        } else {
            LOGGER.info(VMessage.Motor.RPC_ENABLED);
        }
        return agents;
    }

    private static ConcurrentMap<ServerType, List<Class<?>>> agentCombine(
        final ServerType category,
        final ConcurrentMap<ServerType, Class<?>> internals
    ) {
        final ConcurrentMap<ServerType, List<Class<?>>> agents = ZeroAnno.getAgents();
        if (agents.isEmpty()) {
            // Inject ServerType by category input.
            agents.put(category, new ArrayList<>(internals.values()));
        }
        return agents;
    }

    /**
     *
     **/
    private static ConcurrentMap<ServerType, Boolean> isAgentDefined(
        final ConcurrentMap<ServerType, List<Class<?>>> agents,
        final Class<?>... exclude) {
        final Set<Class<?>> excludes = new HashSet<>(Arrays.asList(exclude));
        final ConcurrentMap<ServerType, Boolean> defined
            = new ConcurrentHashMap<>();
        for (final ServerType server : agents.keySet()) {
            final List<Class<?>> item = agents.get(server);
            // Filter to result.
            final List<Class<?>> filtered =
                item.stream()
                    .filter(each -> !excludes.contains(each))
                    .toList();
            // > 1 means duplicated defined
            final int size = filtered.size();
            Fn.outBoot(1 < size,
                LOGGER, AgentDuplicatedException.class,
                ZeroAgent.class, server, size,
                filtered.stream()
                    .map(Class::getName)
                    .collect(Collectors.toSet()));
            // == 0 means undefined
            // == 1 means correct defined
            defined.put(server, VValue.ONE == size);
        }
        return defined;
    }
}
