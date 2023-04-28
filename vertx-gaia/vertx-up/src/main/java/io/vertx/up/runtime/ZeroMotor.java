package io.vertx.up.runtime;

import io.horizon.eon.em.container.ServerType;
import io.horizon.eon.info.VMessage;
import io.vertx.core.ClusterOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EnvelopCodec;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.VertxCallbackException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Start up tools shared in
 * Web Application & Rx Application
 */
public final class ZeroMotor {

    private static final Annal LOGGER = Annal.get(ZeroMotor.class);

    public static <T> void start(
        final Class<?> clazz,
        final Consumer<T> consumer,
        final Consumer<Consumer<T>> fnSingle,
        final BiConsumer<ClusterManager, Consumer<T>> fnCluster,
        final Annal logger) {
        if (null == consumer) {
            throw new VertxCallbackException(clazz);
        }
        // 1. Check if clustered mode
        final ClusterOptions cluster = ZeroGrid.getClusterOption();
        if (cluster.isEnabled()) {

            // 2.1. Clustered
            final ClusterManager manager = cluster.getManager();
            logger.info(VMessage.MOTOR_APP_CLUSTERD, manager.getClass().getName(),
                manager.getNodeId(), manager.isActive());
            fnCluster.accept(manager, consumer);
        } else {
            // 2.2. Standalone
            fnSingle.accept(consumer);
        }
    }

    public static void each(
        final BiConsumer<String, VertxOptions> consumer) {
        final ConcurrentMap<String, VertxOptions> vertxOptions
            = ZeroGrid.getVertxOptions();
        vertxOptions.forEach(consumer);
    }

    public static void codec(final EventBus eventBus) {
        eventBus.registerDefaultCodec(Envelop.class,
            Ut.singleton(EnvelopCodec.class));
    }

    /**
     * Agent calculation
     *
     * @param defaultAgents default agent classes array
     * @param internals     default internal agent class
     *
     * @return The map to stored agent class for each ServerType here
     */
    public static ConcurrentMap<ServerType, Class<?>> agents(
        final ServerType category,
        final Class<?>[] defaultAgents,
        final ConcurrentMap<ServerType, Class<?>> internals
    ) {
        final ConcurrentMap<ServerType, List<Class<?>>> agents =
            getMergedAgents(category, internals);
        final ConcurrentMap<ServerType, Boolean> defines =
            ZeroHelper.isAgentDefined(agents, defaultAgents);
        final ConcurrentMap<ServerType, Class<?>> ret =
            new ConcurrentHashMap<>();
        // Fix Boot
        // 1. If defined, use default
        Ut.itMap(agents, (type, list) -> {
            // 2. Defined -> You have defined
            Fn.runAt(defines.containsKey(type) && defines.get(type), LOGGER,
                () -> {
                    // Use user-defined Agent instead.
                    final Class<?> found = Ut.elementFind(list,
                        (item) -> internals.get(type) != item);
                    if (null != found) {
                        ret.put(type, found);
                    }
                }, () -> {
                    // Use internal defined ( system defaults )
                    final Class<?> found = Ut.elementFind(list,
                        (item) -> internals.get(type) == item);
                    if (null != found) {
                        LOGGER.info(VMessage.MOTOR_AGENT_DEFINED, found, type);
                        ret.put(type, found);
                    }
                });
        });
        // 2.Filter
        return filterAgents(ret);
    }

    private static ConcurrentMap<ServerType, Class<?>> filterAgents(
        final ConcurrentMap<ServerType, Class<?>> agents) {
        // Check Rpc Enabled
        if (ZeroGrid.getRpcOptions().isEmpty()) {
            agents.remove(ServerType.IPC);
        } else {
            LOGGER.info(VMessage.MOTOR_RPC_ENABLED);
        }
        return agents;
    }

    private static ConcurrentMap<ServerType, List<Class<?>>> getMergedAgents(
        final ServerType category,
        final ConcurrentMap<ServerType, Class<?>> internals
    ) {
        final ConcurrentMap<ServerType, List<Class<?>>> agents = ZeroAnno.getAgents();
        if (agents.isEmpty()) {
            // Inject ServerType by category input.
            agents.put(category, new ArrayList<>(internals.values()));
        }
        return agents;
    }
}
