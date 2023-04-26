package io.vertx.tp.route.refine;

import io.vertx.up.log.Log;
import io.vertx.up.log.LogModule;

public final class Rt {

    public interface LOG {
        String MODULE = "πύλη";

        LogModule Init = Log.modulat(MODULE).program("Init");
    }
}
