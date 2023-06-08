package io.vertx.up.boot.options;

import io.horizon.eon.VMessage;
import io.horizon.exception.ProgramException;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Ruler;
import io.vertx.up.eon.KName;
import io.vertx.up.runtime.env.MatureOn;
import io.vertx.up.uca.options.JTransformer;
import io.vertx.up.uca.options.ServerVisitor;
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
     * @throws ProgramException ServerConfigException
     */
    @Override
    public ConcurrentMap<Integer, HttpServerOptions> visit(final String... key)
        throws ProgramException {
        final JsonArray serverData = this.serverPre(0, key);
        this.logger().info(VMessage.Visitor.V_BEFORE, KName.SERVER, this.serverType(), serverData.encode());
        Ruler.verify(KName.SERVER, serverData);
        final ConcurrentMap<Integer, HttpServerOptions> map =
            new ConcurrentHashMap<>();
        this.extract(serverData, map);
        if (!map.isEmpty()) {
            this.logger().info(VMessage.Visitor.V_AFTER, KName.SERVER, this.serverType(), map.keySet());
        }
        return map;
    }

    protected void extract(final JsonArray serverData, final ConcurrentMap<Integer, HttpServerOptions> map) {
        /*
         * 多服务器模式：Server可使用环境变量，环境变量后缀为索引值（0抹去）
         */
        Ut.itJArray(serverData, (item, index) -> {
            if (this.isServer(item)) {
                // 合法 Server
                JsonObject configureJ = Ut.valueJObject(item, KName.CONFIG);
                configureJ = MatureOn.envApi(configureJ, index);
                // 1. Extract port
                final int port = this.serverPort(configureJ);
                // 2. Convert JsonObject to HttpServerOptions
                item.put(KName.CONFIG, configureJ);
                final HttpServerOptions options = this.transformer.transform(item);
                // 3. Add to map
                map.put(port, options);
            }
        });
    }
}
