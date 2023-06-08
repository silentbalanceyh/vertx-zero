package io.vertx.up.bottle;

import io.horizon.uca.log.Annal;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.RpcOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Worker;
import io.vertx.up.eon.KWeb;
import io.vertx.up.eon.em.EmTraffic;
import io.vertx.up.eon.em.Etat;
import io.vertx.up.uca.micro.center.ZeroRegistry;
import io.vertx.up.uca.micro.ipc.server.Tunnel;

/**
 * Get data from event bus and push metdata to Etcd ( IPC )
 */
@Worker(value = EmTraffic.Exchange.REQUEST_MICRO_WORKER, instances = 1)
public class ZeroRpcRegistry extends AbstractVerticle {

    private static final Annal LOGGER = Annal.get(ZeroRpcRegistry.class);
    private transient final ZeroRegistry registry
        = ZeroRegistry.create(this.getClass());

    @Override
    public void start() {
        final EventBus bus = this.vertx.eventBus();
        bus.<JsonObject>consumer(KWeb.ADDR.EBS_IPC_START, result -> {
            final JsonObject data = result.body();
            final RpcOptions options = new RpcOptions(data);
            // Write the data to registry.
            this.registry.registryRpc(options, Etat.RUNNING);
            this.registry.registryIpcs(options, Tunnel.IPCS.keySet());

            LOGGER.info(INFO.ZeroRegistry.MICRO_REGISTRY_CONSUME, this.getClass().getSimpleName(),
                options.getName(), KWeb.ADDR.EBS_IPC_START);
        });
    }
}
