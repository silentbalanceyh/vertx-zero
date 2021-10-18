package io.vertx.up.uca.failure;

import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.WebException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.io.InputStream;

public class CodeReadible implements Readible {
    private static final Annal LOGGER = Annal.get(CodeReadible.class);
    private static final JsonObject MESSAGE = new JsonObject();
    private static final String FILENAME = "vertx-readible.yml";

    @Override
    public void interpret(final WebException error) {
        if (MESSAGE.isEmpty()) {
            final InputStream in = Ut.ioStream(FILENAME);
            // Do not throw out EmptyStreamException when up.god.file does not existing.
            if (null != in) {
                MESSAGE.mergeIn(Ut.ioYaml(FILENAME));
            }
        }
        // Pick up message from MESSAGE cache.
        final String message = MESSAGE.getString(String.valueOf(Math.abs(error.getCode())));
        // Check whether the readible set.
        Fn.safeSemi(Ut.isNil(error.getReadible()), LOGGER,
            () -> Fn.safeNull(() -> error.setReadible(message), error));
    }
}
