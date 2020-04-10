package io.vertx.tp.atom.init;

import io.vertx.tp.atom.modeling.config.AoConfig;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.ke.refine.Ke;

public class AoPin {
    /*
     * Start Up method here
     */
    public static void init() {
        Ke.banner("「διαμορφωτής」- Atom ( Ao )");
        Ao.infoInit(AoPin.class, "AoConfiguration...");
        AoConfiguration.init();
    }

    public static AoConfig getConfig() {
        return AoConfiguration.getConfig();
    }
}
