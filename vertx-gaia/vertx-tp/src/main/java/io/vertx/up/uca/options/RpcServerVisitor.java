package io.vertx.up.uca.options;

import io.vertx.core.ServidorOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.eon.Info;
import io.vertx.up.eon.Plugins;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.demon.ServerConfigException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.marshal.RpcServerStrada;
import io.vertx.up.uca.marshal.Transformer;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Rpc options only, it's different from Http
 */
public class RpcServerVisitor implements ServerVisitor<ServidorOptions> {

    private static final Annal LOGGER = Annal.get(RpcServerVisitor.class);

    private transient final Node<JsonObject> node = Node.infix(Plugins.SERVER);
    private transient final Transformer<ServidorOptions>
        transformer = Ut.singleton(RpcServerStrada.class);

    @Override
    @SuppressWarnings("all")
    public ConcurrentMap<Integer, ServidorOptions> visit(final String... key)
        throws ZeroException {
        // 1. Must be the first line, fixed position.
        Fn.inLenEq(this.getClass(), 0, key);
        // 2. Visit the node for server, http
        final JsonObject data = this.node.read();

        Fn.outZero(null == data || !data.containsKey(KEY), LOGGER,
            ServerConfigException.class,
            this.getClass(), null == data ? null : data.encode());

        return this.visit(data.getJsonArray(KEY));
    }

    private ConcurrentMap<Integer, ServidorOptions> visit(final JsonArray serverData)
        throws ZeroException {
        LOGGER.info(Info.INF_B_VERIFY, KEY, ServerType.IPC, serverData.encode());
        Ruler.verify(KEY, serverData);
        final ConcurrentMap<Integer, ServidorOptions> map =
            new ConcurrentHashMap<>();
        Ut.itJArray(serverData, JsonObject.class, (item, index) -> {
            if (this.isServer(item)) {
                // 1. Extract port
                final int port = this.extractPort(item.getJsonObject(YKEY_CONFIG));
                // 2. Convert JsonObject to HttpServerOptions
                final ServidorOptions options = this.transformer.transform(item);
                Fn.safeNull(() -> {
                    // 3. Add to map;
                    map.put(port, options);
                }, port, options);
            }
        });
        LOGGER.info(Info.INF_A_VERIFY, KEY, ServerType.IPC, map.keySet());
        return map;
    }

    private int extractPort(final JsonObject config) {
        if (null != config) {
            return config.getInteger("port", ServidorOptions.DEFAULT_PORT);
        }
        return ServidorOptions.DEFAULT_PORT;
    }

    private boolean isServer(final JsonObject item) {
        return ServerType.IPC.match(item.getString(YKEY_TYPE));
    }
}
