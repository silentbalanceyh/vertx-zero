package io.vertx.up.uca.options;

import io.vertx.core.http.HttpServerOptions;
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
import io.vertx.up.uca.marshal.HttpServerStrada;
import io.vertx.up.uca.marshal.Transformer;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang
 * Http options only, it's standard
 */
public class HttpServerVisitor implements ServerVisitor<HttpServerOptions> {

    protected transient final Transformer<HttpServerOptions>
            transformer = Ut.singleton(HttpServerStrada.class);
    private transient final Node<JsonObject> NODE = Node.infix(Plugins.SERVER);

    /**
     * @return Server config to generate HttpServerOptions by port
     * @throws ZeroException ServerConfigException
     */
    @Override
    @SuppressWarnings("all")
    public ConcurrentMap<Integer, HttpServerOptions> visit(final String... key)
            throws ZeroException {
        // 1. Must be the first line, fixed position.
        Fn.inLenEq(getClass(), 0, (Object[]) key);
        // 2. Visit the node for server, http
        final JsonObject data = NODE.read();

        Fn.outZero(null == data || !data.containsKey(KEY), getLogger(),
                ServerConfigException.class,
                getClass(), null == data ? null : data.encode());

        return visit(data.getJsonArray(KEY));
    }

    private ConcurrentMap<Integer, HttpServerOptions> visit(final JsonArray serverData)
            throws ZeroException {
        this.getLogger().info(Info.INF_B_VERIFY, KEY, this.getType(), serverData.encode());
        Ruler.verify(KEY, serverData);
        final ConcurrentMap<Integer, HttpServerOptions> map =
                new ConcurrentHashMap<>();
        this.extract(serverData, map);
        this.getLogger().info(Info.INF_A_VERIFY, KEY, this.getType(), map.keySet());
        return map;
    }

    protected void extract(final JsonArray serverData, final ConcurrentMap<Integer, HttpServerOptions> map) {
        Ut.itJArray(serverData, JsonObject.class, (item, index) -> {
            if (this.isServer(item)) {
                // 1. Extract port
                final int port = this.extractPort(item.getJsonObject(YKEY_CONFIG));
                // 2. Convert JsonObject to HttpServerOptions
                final HttpServerOptions options = this.transformer.transform(item);
                Fn.safeNull(() -> {
                    // 3. Add to map;
                    map.put(port, options);
                }, port, options);
            }
        });
    }

    protected boolean isServer(final JsonObject item) {
        return this.getType().match(item.getString(YKEY_TYPE));
    }

    private int extractPort(final JsonObject config) {
        if (null != config) {
            return config.getInteger("port", HttpServerOptions.DEFAULT_PORT);
        }
        return HttpServerOptions.DEFAULT_PORT;
    }

    protected ServerType getType() {
        return ServerType.HTTP;
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
