package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.core.Vertx;
import io.vertx.ext.stomp.Destination;
import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.eon.em.RemindType;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class MixerDestination extends AbstractMixer {
    private final transient Set<Remind> sockOk = new HashSet<>();

    public MixerDestination(final Vertx vertx, final Set<Remind> sockOk) {
        super(vertx);
        if (Objects.nonNull(sockOk)) {
            this.sockOk.addAll(sockOk);
        }
    }

    @Override
    public <T> T mount(final StompServerHandler handler, final StompServerOptions options) {
        /*
         * Build Map of address = type
         * Here are two types
         */
        final ConcurrentMap<String, RemindType> topicMap = new ConcurrentHashMap<>();
        this.sockOk.forEach(remind -> {
            final String subscribe = remind.getSubscribe();
            if (Ut.notNil(subscribe)) {
                topicMap.put(subscribe, Objects.isNull(remind.getType()) ? RemindType.TOPIC : remind.getType());
            }
        });
        // Destination Building
        handler.destinationFactory((v, name) -> {
            final RemindType type = topicMap.getOrDefault(name, null);
            if (Objects.isNull(type)) {
                // No Definition of Address
                this.logger().info(Info.SUBSCRIBE_NULL, name);
                return null;
            }
            if (RemindType.QUEUE == type) {
                // Queue
                this.logger().info(Info.SUBSCRIBE_QUEUE, name);
                return Destination.queue(v, name);
            } else {
                // Topic
                this.logger().info(Info.SUBSCRIBE_TOPIC, name);
                return Destination.topic(v, name);
            }
        });
        return null;
    }
}
