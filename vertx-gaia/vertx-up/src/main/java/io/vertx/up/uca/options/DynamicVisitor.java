package io.vertx.up.uca.options;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.eon.Info;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.ServerType;
import io.horizon.exception.ZeroException;

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
        throws ZeroException {
        final JsonArray serverData = this.serverPre(1, key);
        this.type = ServerType.valueOf(key[Values.IDX]);
        this.logger().info(Info.INF_B_VERIFY, KName.SERVER, this.type, serverData.encode());
        Ruler.verify(KName.SERVER, serverData);
        final ConcurrentMap<Integer, HttpServerOptions> map =
            new ConcurrentHashMap<>();
        this.extract(serverData, map);
        if (!map.isEmpty()) {
            this.logger().info(Info.INF_A_VERIFY, KName.SERVER, this.type, map.keySet());
        }
        return map;
    }

    @Override
    public boolean isServer(final JsonObject item) {
        return null != this.type && this.type.match(item.getString(KName.TYPE));
    }
}
