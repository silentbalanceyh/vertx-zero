package io.vertx.up.boot.anima;

import io.vertx.up.bottle.ZeroHttpAgent;
import io.vertx.up.eon.KWeb;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This factor could begin following:
 * 1. Http Server,
 * 2. Rpc Server.
 */
public class FactorHttp extends AbstractFactor {

    private static final Set<Class<?>> AGENT_SET = new HashSet<>() {{
        this.add(ZeroHttpAgent.class);
        // this.add(ZeroSockAgent.class);
    }};
    private static final ConcurrentMap<ServerType, Class<?>> INTERNALS = new ConcurrentHashMap<>() {{
        this.put(ServerType.HTTP, ZeroHttpAgent.class);
        // this.put(ServerType.SOCK, ZeroSockAgent.class);
    }};

    static {
        if (ZeroStore.is(YmlCore.etcd.__KEY)) {
            final Class<?> clazz = Ut.clazz(KWeb.COMPONENTS.AGENT_RPC, null);
            if (Objects.nonNull(clazz)) {
                /*
                 * Infusion In ( gRpc Environment )
                 */
                AGENT_SET.add(clazz);
                INTERNALS.put(ServerType.IPC, clazz);
            }
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
