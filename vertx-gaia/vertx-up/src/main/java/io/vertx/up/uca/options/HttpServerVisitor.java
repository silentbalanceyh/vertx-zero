package io.vertx.up.uca.options;

import io.vertx.aeon.eon.HEnv;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.eon.Info;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.marshal.HttpServerSetUp;
import io.vertx.up.uca.marshal.JTransformer;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang
 * Http options only, it's standard
 */
public class HttpServerVisitor extends AbstractSVisitor implements ServerVisitor<HttpServerOptions> {

    protected transient final JTransformer<HttpServerOptions>
        transformer = Ut.singleton(HttpServerSetUp.class);

    /**
     * @return Server config to generate HttpServerOptions by port
     * @throws ZeroException ServerConfigException
     */
    @Override
    public ConcurrentMap<Integer, HttpServerOptions> visit(final String... key)
        throws ZeroException {
        final JsonArray serverData = this.serverPre(0, key);
        this.logger().info(Info.INF_B_VERIFY, KName.SERVER, this.serverType(), serverData.encode());
        Ruler.verify(KName.SERVER, serverData);
        final ConcurrentMap<Integer, HttpServerOptions> map =
            new ConcurrentHashMap<>();
        this.extract(serverData, map);
        if (!map.isEmpty()) {
            this.logger().info(Info.INF_A_VERIFY, KName.SERVER, this.serverType(), map.keySet());
        }
        return map;
    }

    protected void extract(final JsonArray serverData, final ConcurrentMap<Integer, HttpServerOptions> map) {
        Ut.itJArray(serverData).filter(this::isServer).forEach(item -> {
            /* 「Z_PORT_WEB」环境变量注入，HttpServer专用 */
            final JsonObject configJ = item.getJsonObject(KName.CONFIG).copy();
            final String portCfg = Ut.valueString(configJ, KName.PORT);
            final String portEnv = Ut.valueEnv(HEnv.Z_PORT_WEB, portCfg);
            configJ.put(KName.PORT, Integer.valueOf(portEnv));

            // 1. Extract port
            final int port = this.serverPort(configJ);
            // 2. Convert JsonObject to HttpServerOptions
            final HttpServerOptions options = this.transformer.transform(item);
            Fn.safeNull(() -> {
                // 3. Add to map;
                map.put(port, options);
            }, port, options);
        });
    }
}
