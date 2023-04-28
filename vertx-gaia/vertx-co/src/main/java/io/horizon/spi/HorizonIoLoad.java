package io.horizon.spi;

import io.vertx.core.json.JsonObject;
import io.vertx.up.runtime.ZeroAmbient;
import io.vertx.up.runtime.ZeroYml;
import io.vertx.up.uca.yaml.Node;

/**
 * @author lang : 2023/4/28
 */
public class HorizonIoLoad implements HorizonIo {

    @Override
    public JsonObject ofError() {
        final Node<JsonObject> node = Node.infix(ZeroYml._error);
        return node.read();
    }

    @Override
    public Class<?> ofAnnal() {
        return ZeroAmbient.getPlugin("logger");
    }
}
