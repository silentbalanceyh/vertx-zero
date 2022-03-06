package io.vertx.tp.ke.booter;

import io.vertx.tp.battery.init.BkPin;
import io.vertx.tp.ke.cv.KeIpc;
import io.vertx.tp.plugin.booting.AbstractBoot;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BatteryOOB extends AbstractBoot {
    public BatteryOOB() {
        super(KeIpc.Module.BATTERY);
    }

    @Override
    protected Set<String> configureBuiltIn() {
        return BkPin.getBuiltIn();
    }
}
