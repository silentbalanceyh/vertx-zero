package io.vertx.up.uca.web.limit;

import io.horizon.eon.em.container.ServerType;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;
import io.vertx.up.verticle.ZeroHttpAgent;

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
public class HttpFactor extends AbstractFactor {

    private static final Set<Class<?>> AGENT_SET = new HashSet<>() {{
        this.add(ZeroHttpAgent.class);
        // this.add(ZeroSockAgent.class);
    }};
    private static final ConcurrentMap<ServerType, Class<?>> INTERNALS = new ConcurrentHashMap<>() {{
        this.put(ServerType.HTTP, ZeroHttpAgent.class);
        // this.put(ServerType.SOCK, ZeroSockAgent.class);
    }};
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);

    static {
        final JsonObject data = VISITOR.read();
        if (data.containsKey(KName.Micro.ETCD)) {
            final Class<?> clazz = Ut.clazz(KWeb.COMPONENTS.AGENT_RPC, null);
            if (Objects.nonNull(clazz)) {
                /*
                 * Plugin In ( gRpc Environment )
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
