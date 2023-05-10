package io.vertx.tp.jet.refine;

import io.vertx.tp.jet.uca.tunnel.ActorChannel;
import io.vertx.tp.jet.uca.tunnel.AdaptorChannel;
import io.vertx.tp.jet.uca.tunnel.ConnectorChannel;
import io.vertx.tp.jet.uca.tunnel.DirectorChannel;
import io.vertx.up.eon.em.container.ChannelType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<ChannelType, Class<?>> CHANNELS = new ConcurrentHashMap<>() {
        {
            this.put(ChannelType.ACTOR, ActorChannel.class);
            this.put(ChannelType.DIRECTOR, DirectorChannel.class);
            this.put(ChannelType.ADAPTOR, AdaptorChannel.class);
            this.put(ChannelType.CONNECTOR, ConnectorChannel.class);
        }
    };
}
