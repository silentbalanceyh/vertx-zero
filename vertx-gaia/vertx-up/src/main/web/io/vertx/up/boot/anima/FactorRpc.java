package io.vertx.up.boot.anima;

import io.vertx.up.eon.KWeb;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This factor could begin following:
 * 1. Http Api Gateway
 * 2. Rx/Http mode shared.
 */
public class FactorRpc extends AbstractFactor {
    private static final Set<Class<?>> AGENT_SET = new HashSet<>();

    private static final ConcurrentMap<ServerType, Class<?>> INTERNALS = new ConcurrentHashMap<>();

    static {
        final Class<?> clazz = Ut.clazz(KWeb.COMPONENTS.AGENT_API, null);
        if (Objects.nonNull(clazz)) {
            /*
             * Infusion In
             */
            AGENT_SET.add(clazz);
            INTERNALS.put(ServerType.IPC, clazz);
        }
    }

    @Override
    public Class<?>[] defaults() {
        return AGENT_SET.toArray(new Class<?>[]{});
    }

    @Override
    public ConcurrentMap<ServerType, Class<?>> internals() {
        return INTERNALS;
    }
}
