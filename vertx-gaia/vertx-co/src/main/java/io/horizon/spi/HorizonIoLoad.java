package io.horizon.spi;

import io.vertx.core.json.JsonObject;
import io.vertx.up.runtime.ZeroAmbient;
import io.vertx.up.runtime.ZeroYml;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.util.Ut;

import java.io.InputStream;

/**
 * @author lang : 2023/4/28
 */
public class HorizonIoLoad implements HorizonIo {

    private static final String FILENAME = ZeroYml.equip(ZeroYml._readable);
    private static final JsonObject MESSAGE = new JsonObject();

    @Override
    public JsonObject ofError() {
        final Node<JsonObject> node = Node.infix(ZeroYml._error);
        return node.read();
    }

    @Override
    public Class<?> ofLogger() {
        return ZeroAmbient.getPlugin("logger");
    }

    @Override
    public JsonObject ofFailure() {
        if (MESSAGE.isEmpty()) {
            final InputStream in = Ut.ioStream(FILENAME);
            // Do not throw out EmptyStreamException when up.god.file does not existing.
            if (null != in) {
                MESSAGE.mergeIn(Ut.ioYaml(FILENAME));
            }
        }
        return MESSAGE;
        //        // Pick up message from MESSAGE cache.
        //        final String message = MESSAGE.getString(String.valueOf(Math.abs(error.getCode())));
        //        // Check whether the readible set.
        //        Fn.runAt(Ut.isNil(error.getReadible()), LOGGER,
        //            () -> Fn.runAt(() -> error.setReadible(message), error));
        //        return null;
    }
}
