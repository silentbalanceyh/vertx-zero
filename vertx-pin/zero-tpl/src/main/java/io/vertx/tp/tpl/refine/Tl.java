package io.vertx.tp.tpl.refine;

import io.vertx.up.log.Log;
import io.vertx.up.log.LogModule;

/*
 * Tool class available in current service only
 */
public class Tl {

    public interface LOG {
        String MODULE = "Πρότυπο";

        LogModule Qr = Log.modulat(MODULE).program("Qr");
    }
}
