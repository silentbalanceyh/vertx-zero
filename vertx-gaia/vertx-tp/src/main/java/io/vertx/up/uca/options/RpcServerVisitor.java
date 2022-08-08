package io.vertx.up.uca.options;

import io.vertx.core.RpcOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.eon.Info;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Plugins;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.demon.ServerConfigException;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.marshal.JTransformer;
import io.vertx.up.uca.marshal.RpcServerSetUp;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Rpc options only, it's different from Http
 */
public class RpcServerVisitor implements ServerVisitor<RpcOptions> {

    private transient final Node<JsonObject> node = Node.infix(Plugins.SERVER);
    private transient final JTransformer<RpcOptions>
        transformer = Ut.singleton(RpcServerSetUp.class);

    @Override
    @SuppressWarnings("all")
    public ConcurrentMap<Integer, RpcOptions> visit(final String... key)
        throws ZeroException {
        // 1. Must be the first line, fixed position.
        Fn.verifyLenEq(this.getClass(), 0, key);
        // 2. Visit the node for server, http
        final JsonObject data = this.node.read();

        Fn.outZero(null == data || !data.containsKey(KName.SERVER), logger(),
            ServerConfigException.class,
            this.getClass(), null == data ? null : data.encode());

        return this.visit(data.getJsonArray(KName.SERVER));
    }

    private ConcurrentMap<Integer, RpcOptions> visit(final JsonArray serverData)
        throws ZeroException {
        this.logger().info(Info.INF_B_VERIFY, KName.SERVER, ServerType.IPC, serverData.encode());
        Ruler.verify(KName.SERVER, serverData);
        final ConcurrentMap<Integer, RpcOptions> map =
            new ConcurrentHashMap<>();
        Ut.itJArray(serverData, JsonObject.class, (item, index) -> {
            if (this.isServer(item)) {
                // 1. Extract port
                final int port = this.serverPort(item.getJsonObject(KName.CONFIG));
                // 2. Convert JsonObject to HttpServerOptions
                final RpcOptions options = this.transformer.transform(item);
                Fn.safeNull(() -> {
                    // 3. Add to map;
                    map.put(port, options);
                }, port, options);
            }
        });
        if (!map.isEmpty()) {
            this.logger().info(Info.INF_A_VERIFY, KName.SERVER, ServerType.IPC, map.keySet());
        }
        return map;
    }

    @Override
    public int serverPort(final JsonObject config) {
        if (null != config) {
            return config.getInteger(KName.PORT, RpcOptions.DEFAULT_PORT);
        }
        return RpcOptions.DEFAULT_PORT;
    }

    @Override
    public boolean isServer(final JsonObject item) {
        return ServerType.IPC.match(item.getString(KName.TYPE));
    }
}
