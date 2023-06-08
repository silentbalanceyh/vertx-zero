package io.vertx.up.boot.anima;

import io.horizon.uca.log.Annal;
import io.macrocosm.specification.config.HConfig;
import io.vertx.up.configuration.BootStore;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.exception.boot.RpcPreparingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAgent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractFactor implements Factor {

    @Override
    public ConcurrentMap<ServerType, Class<?>> endpoint(final HConfig hConfig) {

        /* 1.Find Agent for deploy **/
        final ConcurrentMap<ServerType, Class<?>> agentMap = this.internals();
        final ConcurrentMap<ServerType, Class<?>> agents
            = ZeroAgent.agentCommon(ServerType.HTTP, this.defaults(), agentMap);
        if (agents.containsKey(ServerType.IPC)) {
            // 2. Check etcd server status, IPC Only
            final BootStore config = (BootStore) hConfig;
            Fn.outBoot(!config.isEtcd(),
                this.logger(), RpcPreparingException.class, this.getClass());
        }
        // 3. Filter invalid agents
        final Set<ServerType> scanned = new HashSet<>(agents.keySet());
        final Set<ServerType> keeped = agentMap.keySet();
        scanned.removeAll(keeped);
        scanned.forEach(agents::remove);
        return agents;
    }

    public abstract Class<?>[] defaults();

    public abstract ConcurrentMap<ServerType, Class<?>> internals();

    private Annal logger() {
        return Annal.get(this.getClass());
    }
}
