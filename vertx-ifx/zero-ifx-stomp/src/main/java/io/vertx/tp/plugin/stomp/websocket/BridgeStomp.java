package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.stomp.BridgeOptions;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.extension.router.AresGrid;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BridgeStomp {
    private static BridgeOptions BRIDGE = new BridgeOptions();

    public synchronized static BridgeOptions wsOptionBridge() {
        final Set<Remind> wsSock = AresGrid.wsSecure();
        if (Objects.isNull(BRIDGE) && !wsSock.isEmpty()) {
            BRIDGE = new BridgeOptions();
            /*
             * To enable the bridge you need to configure the inbound and outbound addresses.
             */
            wsSock.forEach(remind -> {
                if (Ut.notNil(remind.getAddress()) && Ut.notNil(remind.getSubscribe())) {

                    /*
                     * From Stomp to EventBus
                     * Inbound addresses are STOMP destination that are transferred to the event bus.
                     * The STOMP destination is used as the event bus address.
                     * @Subscribe -> @Address
                     *
                     * Here the address is event bus
                     */
                    final PermittedOptions inbound = new PermittedOptions();
                    inbound.setAddress(remind.getAddress());

                    /*
                     * From EventBus to Stomp
                     * Outbound addresses are event bus addresses that are transferred to STOMP.
                     * @Address -> @Subscribe
                     *
                     * Here the address is stomp address
                     */
                    final PermittedOptions outbound = new PermittedOptions();
                    outbound.setAddress(remind.getSubscribe());
                    BRIDGE.addInboundPermitted(inbound).addOutboundPermitted(outbound);
                }
            });
        }
        return BRIDGE;
    }
}
