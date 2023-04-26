package io.vertx.tp.battery.refine;

import io.vertx.up.log.Log;
import io.vertx.up.log.LogModule;

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
