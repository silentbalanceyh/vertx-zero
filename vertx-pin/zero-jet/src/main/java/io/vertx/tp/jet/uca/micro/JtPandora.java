package io.vertx.tp.jet.uca.micro;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.error._424ChannelConflictException;
import io.vertx.tp.error._424ChannelDefineException;
import io.vertx.tp.error._424ChannelDefinitionException;
import io.vertx.tp.jet.monitor.JtMonitor;
import io.vertx.tp.jet.uca.tunnel.ActorChannel;
import io.vertx.tp.jet.uca.tunnel.AdaptorChannel;
import io.vertx.tp.jet.uca.tunnel.ConnectorChannel;
import io.vertx.tp.jet.uca.tunnel.DirectorChannel;
import io.vertx.tp.optic.jet.JtChannel;
import io.vertx.up.atom.Refer;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.commune.Commercial;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.em.ChannelType;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/*
 * Channel uniform interface call
 */
class JtPandora {

    private static final ConcurrentMap<ChannelType, Class<?>> EXPECTED_MAP =
        new ConcurrentHashMap<ChannelType, Class<?>>() {
            {
                this.put(ChannelType.ADAPTOR, AdaptorChannel.class);
                this.put(ChannelType.CONNECTOR, ConnectorChannel.class);
                this.put(ChannelType.DIRECTOR, DirectorChannel.class);
                this.put(ChannelType.ACTOR, ActorChannel.class);
            }
        };

    static Future<Envelop> async(final Envelop envelop,
                                 final Commercial commercial,
                                 final Mission mission,
                                 final Refer refer,
                                 final JtMonitor monitor) {
        /* Channel class for current consumer thread */
        final Class<?> channelClass = getChannel(commercial);

        /* Initialization for channel */
        final JtChannel channel = Ut.instance(channelClass);

        /* Find the target Field */
        Ut.contract(channel, Commercial.class, commercial);
        Ut.contract(channel, Mission.class, mission);

        /* Dictionary */
        if (Objects.nonNull(refer)) {
            final ConcurrentMap<String, JsonArray> dict = refer.get();
            if (Objects.nonNull(dict)) {
                Ut.contract(channel, ConcurrentMap.class, dict);
            }
        }

        monitor.channelHit(channelClass);
        /* Transfer the `Envelop` request data into channel and let channel do next works */
        return channel.transferAsync(envelop);
    }

    /*
     * Http Web request entrance
     * 1 - Envelop: default zero data request that has been wrapper
     * 2 - Commercial: The default metadata that defined Api here.
     */
    static Future<Envelop> async(final Envelop envelop, final Commercial commercial,
                                 final JtMonitor monitor) {
        /*
         * Refer is only OK when Job
         * 1) Income / Component / Outcome Shared Refer
         * 2) In Api mode, it's not needed
         */
        return async(envelop, commercial, null, null, monitor);
    }

    private static Class<?> getChannel(final Commercial commercial) {
        /*
         * Channel class for current consumer thread
         */
        final Class<?> channelClass = commercial.channelComponent();
        final ChannelType channelType = commercial.channelType();
        /*
         * Super class definitions
         */
        if (ChannelType.DEFINE == channelType) {
            Fn.out(!Ut.isImplement(channelClass, JtChannel.class),
                _424ChannelDefineException.class, JtPandora.class,
                channelClass.getName());
        } else {
            /*
             * The channelClass must be in EXPECTED_MAP
             */
            Fn.out(!EXPECTED_MAP.containsValue(channelClass),
                _424ChannelDefinitionException.class, JtPandora.class,
                Ut.fromJoin(EXPECTED_MAP.values().stream().map(Class::getSimpleName).collect(Collectors.toSet())),
                channelClass);
            /*
             * The channel type must match the target class specification.
             */
            final Class<?> expectedClass = EXPECTED_MAP.get(channelType);
            Fn.out(expectedClass != channelClass,
                _424ChannelConflictException.class, JtPandora.class,
                channelClass.getName(), channelType);
        }
        /*
         * Channel class extract here.
         */
        return channelClass;
    }

}
