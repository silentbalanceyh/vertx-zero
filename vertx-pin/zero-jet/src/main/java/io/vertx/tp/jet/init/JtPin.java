package io.vertx.tp.jet.init;

import io.vertx.tp.jet.atom.JtConfig;
import io.vertx.tp.jet.refine.Jt;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.environment.UnityApp;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;

public class JtPin {
    private static final Annal LOGGER = Annal.get(JtPin.class);

    public static void init() {
        Ke.banner("「Πίδακας δρομολογητή」- ( Api )");
        Jt.infoInit(LOGGER, "JtConfiguration...");
        JtConfiguration.init();

        Jt.infoInit(LOGGER, "Ambient XHeader Start...");
    }

    public static JtConfig getConfig() {
        return JtConfiguration.getConfig();
    }

    public static UnityApp getUnity() {
        final UnityApp unity = Ut.singleton(JtConfiguration.getConfig().getUnity());
        return Objects.isNull(unity) ? null : unity;
    }
}
