package io.horizon.spi;

import io.horizon.exception.internal.EmptyIoException;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;

/**
 * @author lang : 2023/4/28
 */
public class HorizonIoLoad implements HorizonIo {

    private static final String FILENAME = YmlCore.of(YmlCore.readable.__KEY);
    private static final JsonObject MESSAGE = new JsonObject();

    @Override
    public JsonObject ofError() {
        //        final Node<JsonObject> node = Node.infix(YmlCore.error.__KEY);
        return ZeroStore.option(YmlCore.error.__KEY);
    }

    @Override
    public Class<?> ofLogger() {
        return ZeroStore.injection(YmlCore.inject.LOGGER); // ZeroAmbient.getPlugin("logger");
    }

    @Override
    public JsonObject ofFailure() {
        if (MESSAGE.isEmpty() && Ut.ioExist(FILENAME)) {
            try {
                MESSAGE.mergeIn(Ut.ioYaml(FILENAME));
            } catch (final EmptyIoException ex) {
                // Ignore
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
