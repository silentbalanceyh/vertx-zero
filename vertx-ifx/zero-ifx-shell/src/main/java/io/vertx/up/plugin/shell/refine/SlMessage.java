package io.vertx.up.plugin.shell.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.plugin.shell.cv.em.TermStatus;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SlMessage {
    static final JsonObject WELCOME = SlConfig.welcome();

    static void failEmpty() {
        Sl.output(message(YmlCore.shell.welcome.message.EMPTY,
            () -> "Empty arguments here for redo"));
    }

    static void failInvalid(final String command) {
        Sl.output(message(YmlCore.shell.welcome.message.INVALID,
            () -> "Invalid command \"{0}\", it could not be recognised"), command);
    }

    static TermStatus failError(final Throwable ex) {
        final String error = Objects.isNull(ex) ? "Error" : ex.getMessage();
        Sl.output(message(YmlCore.shell.welcome.message.ERROR,
            () -> Ut.rgbRedB("[ ERROR ]") + " " + Ut.rgbRedN(" {0} ")), error);
        if (SlConfig.isDebug()) {
            ex.printStackTrace();
        }
        return TermStatus.FAILURE;
    }

    static void failWarn(final String message, final Object... args) {
        Sl.output(Ut.rgbYellowB("[ WARN  ]") + " " + Ut.rgbGrayN(message), args);
    }

    static String message(final String key, final Supplier<String> defaultSupplier) {
        final JsonObject message = WELCOME.getJsonObject(YmlCore.shell.welcome.MESSAGE);
        final String information = message.getString(key);
        if (Ut.isNil(information)) {
            return defaultSupplier.get();
        } else {
            return information;
        }
    }

    static void output(final String message, final Object... args) {
        stream(message, Ut.rgbBlueB("[ μηδέν ]"), args);
    }

    static void outputOpt(final String message, final String name, final Object... args) {
        stream(message, Ut.rgbBlueB("[ μηδέν ]") + " (" + name + ")", args);
    }

    private static void stream(final String message, final String flag, final Object... args) {
        if (0 == args.length) {
            System.out.println(flag + message);
        } else {
            System.out.println(Ut.fromMessage(flag + message, args));
        }
    }

    static String message(final String message, final Object... args) {
        if (0 == args.length) {
            return ("[ μηδέν ] " + message);
        } else {
            return (Ut.fromMessage("[ μηδέν ] " + message, args));
        }
    }
}
