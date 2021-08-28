package io.vertx.tp.jet.refine;

import io.vertx.tp.jet.uca.tunnel.ActorChannel;
import io.vertx.tp.jet.uca.tunnel.AdaptorChannel;
import io.vertx.tp.jet.uca.tunnel.ConnectorChannel;
import io.vertx.tp.jet.uca.tunnel.DirectorChannel;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.exchange.DictConfig;
import io.vertx.up.commune.exchange.DualMapping;
import io.vertx.up.eon.em.ChannelType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<ChannelType, Class<?>> CHANNELS = new ConcurrentHashMap<ChannelType, Class<?>>() {
        {
            this.put(ChannelType.ACTOR, ActorChannel.class);
            this.put(ChannelType.DIRECTOR, DirectorChannel.class);
            this.put(ChannelType.ADAPTOR, AdaptorChannel.class);
            this.put(ChannelType.CONNECTOR, ConnectorChannel.class);
        }
    };

    ConcurrentMap<String, DualMapping> POOL_MAPPING = new ConcurrentHashMap<>();
    ConcurrentMap<String, DictConfig> POOL_DICT = new ConcurrentHashMap<>();
    ConcurrentMap<String, Identity> POOL_IDENTITY = new ConcurrentHashMap<>();
}
