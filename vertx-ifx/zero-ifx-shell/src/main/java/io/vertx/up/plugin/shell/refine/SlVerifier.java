package io.vertx.up.plugin.shell.refine;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SlVerifier {

    private static final Annal LOGGER = Annal.get(SlVerifier.class);

    static boolean validate(final String[] args) {
        final JsonObject input = Ut.valueJObject(SlConfig.validate().getJsonObject(YmlCore.shell.validate.INPUT));
        /*
         * 1. required arguments for complex shell building
         */
        boolean validated = false;
        if (0 == args.length || Objects.isNull(args[0])) {
            if (input.containsKey(YmlCore.shell.validate.input.REQUIRED)) {
                SlMessage.output(input.getString(YmlCore.shell.validate.input.REQUIRED));
            } else {
                LOGGER.warn("Input no arguments, are you sure ?");
                validated = true;
            }
        } else {
            /*
             * 2. Command must be
             */
            final String argument = args[0];
            final Set<String> supported =
                Ut.toSet(Ut.valueJArray(SlConfig.validate().getJsonArray(YmlCore.shell.validate.ARGS)));
            if (supported.contains(argument)) {
                validated = true;
            } else {
                if (input.containsKey(YmlCore.shell.validate.input.EXISTING)) {
                    SlMessage.output(input.getString(YmlCore.shell.validate.input.EXISTING),
                        Ut.fromJoin(supported), argument);
                } else {
                    LOGGER.warn("There are {0} supported commands {1}, but you provide none ?",
                        supported.size(), Ut.fromJoin(supported));
                    validated = true;
                }
            }
        }
        return validated;
    }
}
