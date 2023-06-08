package io.vertx.mod.battery.booter;

import io.vertx.mod.battery.init.BkPin;
import io.vertx.mod.ke.cv.KeIpc;
import io.vertx.up.plugin.booting.AbstractBoot;

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
