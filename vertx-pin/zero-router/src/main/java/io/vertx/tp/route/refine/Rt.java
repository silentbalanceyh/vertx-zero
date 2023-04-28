package io.vertx.tp.route.refine;

import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;

public final class Rt {

    public interface LOG {
        String MODULE = "πύλη";

        LogModule Init = Log.modulat(MODULE).program("Init");
    }
}
