package io.vertx.tp.plugin.shell.refine;

/**
 * The tool class for Shell
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class Sl {

    public static void init() {
        /* Configuration initialize */
        SlConfig.init();
    }

    public static boolean ready(final String[] args) {
        /* Validation for string */
        return SlVerifier.validate(args);
    }
}
