package io.vertx.tp.route.init;

import io.vertx.tp.ke.refine.Ke;
import io.horizon.uca.log.Annal;

import static io.vertx.tp.route.refine.Rt.LOG;

public class RtPin {

    private static final Annal LOGGER = Annal.get(RtPin.class);

    public static void init() {
        Ke.banner("「πύλη」- ( Gateway )");
        LOG.Init.info(LOGGER, "RtConfiguration...");
        RtConfiguration.init();
    }

    /*
     * Get configured name of ipcAuth
     */
    public static String ipcAuth() {
        return RtConfiguration.getConfig().getIpcAuth();
    }
}
