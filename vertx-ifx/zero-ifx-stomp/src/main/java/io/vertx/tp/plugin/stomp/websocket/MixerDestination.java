package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.core.Vertx;
import io.vertx.ext.stomp.Destination;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.ext.stomp.impl.RemindDestination;
import io.vertx.up.eon.em.uca.RemindType;
import io.vertx.up.extension.router.AresGrid;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MixerDestination extends AbstractMixer {

    public MixerDestination(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public <T> T mount(final StompServerHandler handler, final StompServerOptions options) {
        /*
         * Build Map of address = type
         * Here are two types
         */
        final ConcurrentMap<String, RemindType> topicMap = AresGrid.configTopic();
        // Destination Building
        handler.destinationFactory((v, name) -> {
            final RemindType type = topicMap.getOrDefault(name, null);
            if (Objects.isNull(type)) {
                // No Definition of Address
                this.logger().info(Info.SUBSCRIBE_NULL, name);
                return null;
            }
            // Create new Remind Destination
            return this.destination(v, name, type);
        });
        return this.finished();
    }

    private Destination destination(final Vertx vertx, final String name,
                                    final RemindType type) {
        if (RemindType.REMIND == type) {
            // Remind
            this.logger().info(Info.SUBSCRIBE_REMIND, name);
            return new RemindDestination(vertx);
        }
        if (RemindType.QUEUE == type) {
            // Queue
            this.logger().info(Info.SUBSCRIBE_QUEUE, name);
            return Destination.queue(vertx, name);
        }
        if (RemindType.BRIDGE == type) {
            // Modify Bridge
            this.logger().info(Info.SUBSCRIBE_BRIDGE, name);
            return Destination.bridge(vertx, BridgeStomp.wsOptionBridge());
        }
        // Topic ( Default as Vert.x )
        this.logger().info(Info.SUBSCRIBE_TOPIC, name);
        return Destination.topic(vertx, name);
    }
}
