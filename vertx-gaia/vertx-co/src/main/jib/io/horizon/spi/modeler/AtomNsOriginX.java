package io.horizon.spi.modeler;

import io.horizon.eon.VEnv;
import io.horizon.eon.VString;
import io.horizon.util.HUt;
import io.vertx.up.util.Ut;

/**
 * @author lang : 2023-05-08
 */
public class AtomNsOriginX implements AtomNs {
    @Override
    public String ns(final String appName) {
        return Ut.isNil(appName) ?
            VEnv.APP.NS_DEFAULT : HUt.fromMessage(VEnv.APP.NS, appName);
    }

    @Override
    public String ns(final String appName, final String identifier) {
        return this.ns(appName) + VString.DASH + identifier;
    }
}
