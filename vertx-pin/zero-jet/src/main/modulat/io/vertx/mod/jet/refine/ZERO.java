package io.vertx.mod.jet.refine;

import io.vertx.mod.jet.uca.tunnel.ActorChannel;
import io.vertx.mod.jet.uca.tunnel.AdaptorChannel;
import io.vertx.mod.jet.uca.tunnel.ConnectorChannel;
import io.vertx.mod.jet.uca.tunnel.DirectorChannel;
import io.vertx.up.eon.em.EmTraffic;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<EmTraffic.Channel, Class<?>> CHANNELS = new ConcurrentHashMap<>() {
        {
            this.put(EmTraffic.Channel.ACTOR, ActorChannel.class);
            this.put(EmTraffic.Channel.DIRECTOR, DirectorChannel.class);
            this.put(EmTraffic.Channel.ADAPTOR, AdaptorChannel.class);
            this.put(EmTraffic.Channel.CONNECTOR, ConnectorChannel.class);
        }
    };
}
