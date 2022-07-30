package io.vertx.tp.is.init;

import io.vertx.tp.is.atom.IsConfig;
import io.vertx.tp.is.refine.Is;
import io.vertx.tp.ke.refine.Ke;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IsPin {

    public static void init() {
        Ke.banner("「Ολοκλήρωση」- Integration ( Is )");
        Is.Log.infoInit(IsPin.class, "IsConfiguration...");
        IsConfiguration.init();
    }

    public static IsConfig getConfig() {
        return IsConfiguration.getConfig();
    }
}
