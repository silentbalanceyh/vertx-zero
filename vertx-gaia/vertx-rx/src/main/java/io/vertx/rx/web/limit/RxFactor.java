package io.vertx.rx.web.limit;

import io.vertx.rx.micro.ZeroRxAgent;
import io.horizon.eon.em.container.ServerType;
import io.vertx.up.runtime.ZeroMotor;
import io.vertx.up.uca.web.limit.Factor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This factor could begin following:
 * 1. Rx Http Server,
 * 2. Rx Rpc Server.
 */
public class RxFactor implements Factor {

    private static final Class<?>[] DEFAULT_AGENTS = new Class<?>[]{
        ZeroRxAgent.class
    };

    private static final ConcurrentMap<ServerType, Class<?>> INTERNALS
        = new ConcurrentHashMap<ServerType, Class<?>>() {
        {
            this.put(ServerType.RX, ZeroRxAgent.class);
        }
    };

    @Override
    public ConcurrentMap<ServerType, Class<?>> agents() {
        /** 1.Find Agent for deploy **/
        final ConcurrentMap<ServerType, Class<?>> agents
            = ZeroMotor.agents(ServerType.RX, DEFAULT_AGENTS, INTERNALS);
        // 3. Filter invalid agents
        final Set<ServerType> scanned = new HashSet<>(agents.keySet());
        final Set<ServerType> keeped = INTERNALS.keySet();
        scanned.removeAll(keeped);
        scanned.forEach(agents::remove);
        return agents;
    }
}
