package io.vertx.tp.rbac.init;

import io.horizon.uca.log.Annal;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.atom.ScConfig;

import static io.vertx.tp.rbac.refine.Sc.LOG;

/*
 * Init Plugin for `initAsync` static life
 */
public class ScPin {

    private static final Annal LOGGER = Annal.get(ScPin.class);

    public static void init() {
        Ke.banner("「Ακριβώς」- Rbac ( Sc )");
        LOG.Init.info(LOGGER, "ScConfiguration...");
        ScConfiguration.init();
    }


    public static ScConfig getConfig() {
        return ScConfiguration.getConfig();
    }
}
