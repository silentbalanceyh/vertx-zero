package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SlMessage {
    static final JsonObject WELCOME = SlConfig.welcome();

    static void failEmpty() {
        Sl.output(message("empty",
            () -> "Empty arguments here for redo"));
    }

    static void failInvalid(final String command) {
        Sl.output(message("invalid",
            () -> "Invalid command \"{0}\", it could not be recognised"), command);
    }

    static TermStatus failError(final Throwable ex) {
        final String error = Objects.isNull(ex) ? "Error" : ex.getMessage();
        Sl.output(message("error",
            () -> Ut.flagBRed("[ ERROR ]") + " " + Ut.flagNRed(" {0} ")), error);
        if (SlConfig.isDebug()) {
            ex.printStackTrace();
        }
        return TermStatus.FAILURE;
    }

    static void failWarn(final String message, final Object... args) {
        Sl.output(Ut.flagBYellow("[ WARN  ]") + " " + Ut.flagNGray(message), args);
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
