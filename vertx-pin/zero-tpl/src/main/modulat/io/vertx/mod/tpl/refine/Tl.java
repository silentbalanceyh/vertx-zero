package io.vertx.mod.tpl.refine;

import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;

/*
 * Tool class available in current service only
 */
public class Tl {

    public interface LOG {
        String MODULE = "Πρότυπο";

        LogModule Qr = Log.modulat(MODULE).program("Qr");
    }
}
