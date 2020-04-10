package io.vertx.up.uca.options;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Plugins;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.demon.ServerConfigException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Micro service mode only, Fix for http endpoint part
 */
public class NamesVisitor implements ServerVisitor<String> {

    private transient final Node<JsonObject> NODE = Node.infix(Plugins.SERVER);
    private transient ServerType type = null;

    @Override
    public ConcurrentMap<Integer, String> visit(final String... key)
            throws ZeroException {
        // 1. Must be the first line, fixed position.
        Fn.inLenEq(this.getClass(), 1, key);
        // 2. Visit the node for server, http
        final JsonObject data = this.NODE.read();

        Fn.outZero(null == data || !data.containsKey(KEY), this.getLogger(),
                ServerConfigException.class,
                this.getClass(), null == data ? null : data.encode());
        // 3. Extract names.
        final JsonArray raw = data.getJsonArray(KEY);
        this.type = ServerType.valueOf(key[Values.IDX]);
        return this.extract(raw);
    }

    private ConcurrentMap<Integer, String> extract(final JsonArray serverData) {
        final ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
        Ut.itJArray(serverData, JsonObject.class, (item, index) -> {
            if (this.isServer(item)) {
                // 1. Extract port
                final int port = this.extractPort(item.getJsonObject(YKEY_CONFIG));
                Fn.safeNull(() -> {
                    // 3. Add to map;
                    map.put(port, item.getString(YKEY_NAME));
                }, port);
            }
        });
        return map;
    }

    private boolean isServer(final JsonObject item) {
        return null != this.type &&
                this.type.match(item.getString(YKEY_TYPE));
    }

    private int extractPort(final JsonObject config) {
        if (null != config) {
            return config.getInteger("port", HttpServerOptions.DEFAULT_PORT);
        }
        return HttpServerOptions.DEFAULT_PORT;
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
