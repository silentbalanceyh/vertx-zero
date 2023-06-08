package io.vertx.up.boot.options;

import io.horizon.eon.VValue;
import io.horizon.exception.ProgramException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.container.ServerType;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.options.ServerVisitor;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Micro service mode only, Fix for http endpoint part
 */
public class NamesVisitor extends AbstractSVisitor implements ServerVisitor<String> {
    private transient ServerType type = null;

    @Override
    public ConcurrentMap<Integer, String> visit(final String... key)
        throws ProgramException {
        final JsonArray serverData = this.serverPre(1, key);
        this.type = ServerType.valueOf(key[VValue.IDX]);
        return this.extract(serverData);
    }

    private ConcurrentMap<Integer, String> extract(final JsonArray serverData) {
        final ConcurrentMap<Integer, String> map = new ConcurrentHashMap<>();
        Ut.itJArray(serverData, JsonObject.class, (item, index) -> {
            if (this.isServer(item)) {
                // 1. Extract port
                final int port = this.serverPort(item.getJsonObject(KName.CONFIG));
                Fn.runAt(() -> {
                    // 3. Add to map;
                    map.put(port, item.getString(KName.NAME));
                }, port);
            }
        });
        return map;
    }

    @Override
    public boolean isServer(final JsonObject item) {
        return null != this.type && this.type.match(item.getString(KName.TYPE));
    }
}
