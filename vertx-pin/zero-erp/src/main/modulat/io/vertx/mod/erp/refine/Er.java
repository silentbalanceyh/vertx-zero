package io.vertx.mod.erp.refine;

import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;

/*
 *
 */
public class Er {

    public interface LOG {
        String MODULE = "Επιχείρηση";

        LogModule Worker = Log.modulat(MODULE).program("Worker");
    }
}
