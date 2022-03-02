package io.vertx.tp.ke.booter;

import io.vertx.tp.ke.cv.KeIpc;
import io.vertx.tp.plugin.booting.AbstractBoot;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BatteryOOB extends AbstractBoot {
    public BatteryOOB() {
        super(KeIpc.Module.BATTERY);
    }
}
