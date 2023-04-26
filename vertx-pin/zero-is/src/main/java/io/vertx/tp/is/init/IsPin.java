package io.vertx.tp.is.init;

import io.vertx.tp.is.atom.IsConfig;
import io.vertx.tp.ke.refine.Ke;

import static io.vertx.tp.is.refine.Is.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IsPin {

    public static void init() {
        Ke.banner("「Ολοκλήρωση」- Integration ( Is )");
        LOG.Init.info(IsPin.class, "IsConfiguration...");
        IsConfiguration.init();
    }

    public static IsConfig getConfig() {
        return IsConfiguration.getConfig();
    }
}
