package io.vertx.tp.plugin.rpc;

import io.grpc.ManagedChannel;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.grpc.VertxChannelBuilder;
import io.vertx.up.atom.rpc.IpcData;
import io.vertx.up.eon.em.CertType;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.micro.ssl.TrustPipe;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class RpcSslTool {

    private static final Annal LOGGER = Annal.get(RpcSslTool.class);

    private static final Node<JsonObject> node = Ut.singleton(ZeroUniform.class);

    private static final ConcurrentMap<String, ManagedChannel> CHANNELS =
        new ConcurrentHashMap<>();

    /**
     * @param vertx  Vert.x instance
     * @param config configuration
     *
     * @return ManagedChannel
     */
    public static ManagedChannel getChannel(final Vertx vertx,
                                            final JsonObject config) {
        final String rpcHost = config.getString(Key.HOST);
        final Integer rpcPort = config.getInteger(Key.PORT);

        return getChannel(rpcHost, rpcPort, () -> {
            final VertxChannelBuilder builder =
                VertxChannelBuilder
                    .forAddress(vertx, rpcHost, rpcPort);
            Fn.safeSemi(null != config.getValue(Key.SSL), LOGGER, () -> {
                final JsonObject sslConfig = config.getJsonObject(Key.SSL);
                if (null != sslConfig && !sslConfig.isEmpty()) {
                    final TrustPipe<JsonObject> pipe = getPipe(sslConfig);
                    // Enable SSL
                    builder.useSsl(pipe.parse(sslConfig));
                } else {
                    builder.usePlaintext(true);
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
        return Fn.pool(CHANNELS, key, supplier);
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
            // Ssl Required
            final JsonObject config = node.read();

            Fn.safeSemi(null != config && null != config.getValue("rpc"), LOGGER, () -> {
                // Extension or Uniform
                final JsonObject rpcConfig = config.getJsonObject("rpc");
                final String name = data.getName();
                final JsonObject ssl = RpcHelper.getSslConfig(name, rpcConfig);
                if (ssl.isEmpty()) {
                    // Disabled SSL
                    builder.usePlaintext(true);
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
        final CertType certType = null == type ?
            CertType.PEM : Ut.toEnum(CertType.class, type.toString());
        return TrustPipe.get(certType);
    }
}
