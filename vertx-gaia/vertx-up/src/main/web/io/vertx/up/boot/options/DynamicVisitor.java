package io.vertx.up.boot.options;

import io.horizon.eon.VMessage;
import io.horizon.eon.VValue;
import io.horizon.exception.ProgramException;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Ruler;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.container.ServerType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang
 * Http options for dynamic extension.
 */
public class DynamicVisitor extends HttpServerVisitor {

    private transient ServerType type;

    @Override
    public ConcurrentMap<Integer, HttpServerOptions> visit(final String... key)
        throws ProgramException {
        final JsonArray serverData = this.serverPre(1, key);
        this.type = ServerType.valueOf(key[VValue.IDX]);
        this.logger().info(VMessage.Visitor.V_BEFORE, KName.SERVER, this.type, serverData.encode());
        Ruler.verify(KName.SERVER, serverData);
        final ConcurrentMap<Integer, HttpServerOptions> map =
            new ConcurrentHashMap<>();
        this.extract(serverData, map);
        if (!map.isEmpty()) {
            this.logger().info(VMessage.Visitor.V_AFTER, KName.SERVER, this.type, map.keySet());
        }
        return map;
    }

    @Override
    public boolean isServer(final JsonObject item) {
        return null != this.type && this.type.match(item.getString(KName.TYPE));
    }
}
