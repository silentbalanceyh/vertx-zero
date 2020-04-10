package io.vertx.up.uca.options;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.eon.Info;
import io.vertx.up.eon.Plugins;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.demon.ServerConfigException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang
 * Http options for dynamic extension.
 */
@SuppressWarnings("all")
public class DynamicVisitor extends HttpServerVisitor {

    private static final Annal LOGGER = Annal.get(DynamicVisitor.class);

    private transient final Node<JsonObject> NODE = Node.infix(Plugins.SERVER);

    private transient ServerType type;

    @Override
    public ConcurrentMap<Integer, HttpServerOptions> visit(final String... key)
            throws ZeroException {
        // 1. Must be the first line, fixed position.
        Fn.inLenEq(this.getClass(), 1, key);
        // 2. Visit the node for server
        final JsonObject data = this.NODE.read();

        Fn.outZero(null == data || !data.containsKey(KEY), LOGGER,
                ServerConfigException.class,
                this.getClass(), null == data ? null : data.encode());
        // 3. Convert input parameters.
        this.type = ServerType.valueOf(key[Values.IDX]);
        return this.visit(data.getJsonArray(KEY));
    }

    private ConcurrentMap<Integer, HttpServerOptions> visit(final JsonArray serverData)
            throws ZeroException {
        this.getLogger().info(Info.INF_B_VERIFY, KEY, this.type, serverData.encode());
        Ruler.verify(KEY, serverData);
        final ConcurrentMap<Integer, HttpServerOptions> map =
                new ConcurrentHashMap<>();
        this.extract(serverData, map);
        this.getLogger().info(Info.INF_A_VERIFY, KEY, this.type, map.keySet());
        return map;
    }

    @Override
    protected boolean isServer(final JsonObject item) {
        return null != this.type &&
                this.type.match(item.getString(YKEY_TYPE));
    }
}
