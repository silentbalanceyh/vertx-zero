package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class SlMessage {
    static final JsonObject WELCOME = SlConfig.welcome();

    static void failEmpty() {
        Sl.output(message("empty",
                () -> "Empty arguments here for redo"));
    }

    static TermStatus failError(final Throwable ex) {
        final String error = Objects.isNull(ex) ? "Error" : ex.getMessage();
        Sl.output(message("error",
                () -> "Error occurs {0}"), error);
        return TermStatus.FAILURE;
    }

    static String message(final String key, final Supplier<String> defaultSupplier) {
        final JsonObject message = WELCOME.getJsonObject("message");
        final String information = message.getString(key);
        if (Ut.isNil(information)) {
            return defaultSupplier.get();
        } else {
            return information;
        }
    }
}
