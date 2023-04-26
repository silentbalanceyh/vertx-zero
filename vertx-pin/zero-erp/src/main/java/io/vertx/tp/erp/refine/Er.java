package io.vertx.tp.erp.refine;

import io.vertx.up.log.Log;
import io.vertx.up.log.LogModule;

/*
 *
 */
public class Er {

    public interface LOG {
        String MODULE = "Επιχείρηση";

        LogModule Worker = Log.modulat(MODULE).program("Worker");
    }
}
