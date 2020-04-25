package io.vertx.tp.plugin.shell.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
class SlVerifier {

    private static final Annal LOGGER = Annal.get(SlVerifier.class);

    static boolean validate(final String[] args) {
        final JsonObject input = Ut.sureJObject(SlConfig.validate().getJsonObject("input"));
        /*
         * 1. required arguments for complex shell building
         */
        boolean validated = false;
        if (1 == args.length || Objects.isNull(args[0])) {
            if (input.containsKey("required")) {
                SlLog.output(input.getString("required"));
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
                    Ut.toSet(Ut.sureJArray(SlConfig.validate().getJsonArray("args")));
            if (supported.contains(argument)) {
                validated = true;
            } else {
                if (input.containsKey("existing")) {
                    SlLog.output(input.getString("existing"),
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
