package io.vertx.mod.battery.refine;

import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Bk {
    public interface LOG {
        String MODULE = "Πρότυπο";

        LogModule Init = Log.modulat(MODULE).program("Init");
        LogModule Spi = Log.modulat(MODULE).program("Service Loader");
    }
}
