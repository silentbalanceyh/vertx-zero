package io.vertx.up.uca.options;

import io.vertx.aeon.eon.HEnv;
import io.vertx.core.SockOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.eon.Info;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.marshal.JTransformer;
import io.vertx.up.uca.marshal.SockSetUp;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SockVisitor extends AbstractSVisitor implements ServerVisitor<SockOptions> {
    protected transient final JTransformer<SockOptions>
        transformer = Ut.singleton(SockSetUp.class);

    @Override
    public ConcurrentMap<Integer, SockOptions> visit(final String... key)
        throws ZeroException {
        final JsonArray serverData = this.serverPre(0, key);
        this.logger().info(Info.INF_B_VERIFY, KName.SERVER, this.serverType(), serverData.encode());
        Ruler.verify(KName.SERVER, serverData);
        final ConcurrentMap<Integer, SockOptions> map =
            new ConcurrentHashMap<>();
        this.extract(serverData, map);
        if (!map.isEmpty()) {
            this.logger().info(Info.INF_A_VERIFY, KName.SERVER, this.serverType(), map.keySet());
        }
        return map;
    }

    protected void extract(final JsonArray serverData, final ConcurrentMap<Integer, SockOptions> map) {
        Ut.itJArray(serverData).filter(this::isServer).forEach(item -> {
            /* 「Z_PORT_SOCK」环境变量注入，HttpServer专用 */
            final JsonObject configJ = item.getJsonObject(KName.CONFIG).copy();
            final String portCfg = Ut.valueString(configJ, KName.PORT);
            String portEnv = Ut.envIn(HEnv.Z_PORT_SOCK, null);
            if (Ut.isNil(portEnv)) {
                portEnv = Ut.envIn(HEnv.Z_PORT_WEB, portCfg);
            }
            configJ.put(KName.PORT, Integer.valueOf(portEnv));
            // 1. Extract port
            final int port = this.serverPort(configJ);
            // 2. Convert JsonObject to SockOptions
            final SockOptions options = this.transformer.transform(item);
            Fn.safeNull(() -> {
                // 3. Add to map;
                map.put(port, options);
            }, port, options);
        });
    }

    @Override
    public ServerType serverType() {
        return ServerType.SOCK;
    }
}
