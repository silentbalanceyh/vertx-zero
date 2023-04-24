package io.vertx.up.verticle;

import io.horizon.eon.em.container.ServerType;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.RpcOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;
import io.vertx.up.annotations.Agent;
import io.vertx.up.eon.KWeb;
import io.vertx.up.eon.bridge.Values;
import io.vertx.up.eon.em.Etat;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroGrid;
import io.vertx.up.uca.micro.center.ZeroRegistry;
import io.vertx.up.uca.micro.ipc.server.Tunnel;
import io.vertx.up.uca.micro.ipc.server.UnityTunnel;
import io.vertx.up.util.Ut;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Internal Rpc Server, called IPC
 * Once you have defined another Agent, the default will be replaced.
 */
@Agent(type = ServerType.IPC)
public class ZeroRpcAgent extends AbstractVerticle {

    private static final Annal LOGGER = Annal.get(ZeroRpcAgent.class);
    private static final String SSL = "ssl";

    private final transient ZeroRegistry registry
        = ZeroRegistry.create(this.getClass());

    @Override
    public void start() {
        /* 1. Iterate all the configuration **/
        Ut.itMap(ZeroGrid.getRpcOptions(), (port, config) -> {
            /* 2.Rcp server builder initialized **/
            final VertxServerBuilder builder = VertxServerBuilder
                .forAddress(this.vertx, config.getHost(), config.getPort());
            /*
             * 3.Service added.
             */
            {
                // UnityService add ( Envelop )
                final Tunnel tunnel = Ut.singleton(UnityTunnel.class);
                builder.addService(tunnel.init(this.vertx));
            }
            /*
             * 4.Server added.
             */
            final VertxServer server = builder.build();
            server.start(handler -> this.registryServer(handler, config));
        });
    }

    @Override
    public void stop() {
        Ut.itMap(ZeroGrid.getRpcOptions(), (port, config) -> {
            // Status registry
            this.registry.registryRpc(config, Etat.STOPPED);
        });
    }

    /**
     * Registry the data into etcd
     *
     * @param handler async handler
     * @param options rpc options
     */
    private void registryServer(final AsyncResult<Void> handler,
                                final RpcOptions options) {
        final Integer port = options.getPort();
        final AtomicInteger out = ZeroGrid.ATOMIC_LOG.get(port);
        if (Values.ONE == out.getAndIncrement()) {
            if (handler.succeeded()) {
                LOGGER.info(Info.RPC_LISTEN, Ut.netIPv4(), String.valueOf(options.getPort()));
                // Started to write data in etcd center.
                LOGGER.info(Info.ETCD_SUCCESS, this.registry.getConfig());
                // Status registry
                this.startRegistry(options);
            } else {
                LOGGER.info(Info.RPC_FAILURE, null == handler.cause() ? "None" : handler.cause().getMessage());
            }
        }
    }

    private void startRegistry(final RpcOptions options) {
        // Rpc Agent is only valid in Micro mode
        final EventBus bus = this.vertx.eventBus();
        final String address = KWeb.ADDR.EBS_IPC_START;
        LOGGER.info(Info.IPC_REGISTRY_SEND, this.getClass().getSimpleName(), options.getName(), address);
        bus.publish(address, options.toJson());
    }
}
