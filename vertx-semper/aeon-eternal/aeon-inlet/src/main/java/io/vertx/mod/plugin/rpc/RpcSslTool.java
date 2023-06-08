package io.vertx.mod.plugin.rpc;

import io.grpc.ManagedChannel;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.grpc.VertxChannelBuilder;
import io.vertx.up.atom.agent.IpcData;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.uca.micro.ssl.TrustPipe;
import io.vertx.up.util.Ut;

import java.util.function.Supplier;

public class RpcSslTool {

    private static final Annal LOGGER = Annal.get(RpcSslTool.class);
    private static final Cc<String, ManagedChannel> CC_CHANNEL = Cc.open();

    /**
     * @param vertx  Vert.x instance
     * @param config configuration
     *
     * @return ManagedChannel
     */
    public static ManagedChannel getChannel(final Vertx vertx,
                                            final JsonObject config) {
        final String rpcHost = config.getString(YmlCore.rpc.client.HOST);
        final Integer rpcPort = config.getInteger(YmlCore.rpc.client.PORT);

        return getChannel(rpcHost, rpcPort, () -> {
            final VertxChannelBuilder builder =
                VertxChannelBuilder
                    .forAddress(vertx, rpcHost, rpcPort);
            Fn.runAt(null != config.getValue(YmlCore.rpc.SSL), LOGGER, () -> {
                final JsonObject sslConfig = config.getJsonObject(YmlCore.rpc.SSL);
                if (null != sslConfig && !sslConfig.isEmpty()) {
                    final TrustPipe<JsonObject> pipe = getPipe(sslConfig);
                    // Enable SSL
                    builder.useSsl(pipe.parse(sslConfig));
                } else {
                    // 4.x
                    builder.usePlaintext();
                }
            });
            final ManagedChannel channel = builder.build();
            LOGGER.info(Info.CLIENT_RPC, rpcHost, String.valueOf(rpcPort), String.valueOf(channel.hashCode()));
            return channel;
        });
    }

    private static ManagedChannel getChannel(final String host, final Integer port,
                                             final Supplier<ManagedChannel> supplier) {
        final String key = host + port;
        return CC_CHANNEL.pick(supplier, key); // Fn.po?l(CHANNELS, key, supplier);
    }

    public static ManagedChannel getChannel(final Vertx vertx,
                                            final IpcData data) {
        final String grpcHost = data.getHost();
        final Integer grpcPort = data.getPort();

        return getChannel(grpcHost, grpcPort, () -> {
            LOGGER.info(Info.CLIENT_BUILD, grpcHost, String.valueOf(grpcPort));
            final VertxChannelBuilder builder =
                VertxChannelBuilder
                    .forAddress(vertx, grpcHost, grpcPort);

            Fn.runAt(ZeroStore.is(YmlCore.rpc.__KEY), LOGGER, () -> {
                // Extension or Uniform
                final JsonObject rpcConfig = ZeroStore.option(YmlCore.rpc.__KEY);
                final String name = data.getName();
                final JsonObject ssl = RpcHelper.getSslConfig(name, rpcConfig);
                if (ssl.isEmpty()) {
                    // Disabled SSL, 4.x
                    builder.usePlaintext();
                } else {
                    final TrustPipe<JsonObject> pipe = getPipe(ssl);
                    // Enabled SSL
                    builder.useSsl(pipe.parse(ssl));
                }
            });
            return builder.build();
        });
    }

    private static TrustPipe<JsonObject> getPipe(final JsonObject ssl) {
        final Object type = ssl.getValue("type");
        final EmSecure.CertType certType = null == type ?
            EmSecure.CertType.PEM : Ut.toEnum(type::toString, EmSecure.CertType.class);
        return TrustPipe.get(certType);
    }
}
