package io.vertx.tp.plugin.jooq;

import io.horizon.uca.log.Annal;
import io.vertx.core.Vertx;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.commune.config.Database;
import io.vertx.up.exception.booting.JooqConfigurationException;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.Infix;
import io.vertx.up.runtime.ZeroYml;
import org.jooq.Configuration;
import org.jooq.DSLContext;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Plugin
@SuppressWarnings("unchecked")
public class JooqInfix implements Infix {

    private static final Annal LOGGER = Annal.get(JooqInfix.class);

    private static final ConcurrentMap<String, Configuration> CONFIGURATION
        = new ConcurrentHashMap<>();

    private static Vertx vertxRef;

    /*
     * 1. Step 1:
     *
     * Infix architecture in zero framework
     * This method will be called when container booting
     * First Step to initialize the whole jooq configuration
     */
    public static void init(final Vertx vertx) {
        vertxRef = vertx;
        /*
         * Here initialize the static jooq configuration only
         * If there exist dynamic pool, it will process in `delay` loading processing
         */
        CONFIGURATION.putAll(Infix.init(ZeroYml.inject.jooq, JooqPin::initConfiguration, JooqInfix.class));
    }

    private static Configuration configDelay(final DataPool pool) {
        final Database database = pool.getDatabase();
        final String configurationKey = Objects.requireNonNull(database).getJdbcUrl();
        if (CONFIGURATION.containsKey(configurationKey)) {
            return configSafe(configurationKey);
        } else {
            final Configuration configuration = pool.configuration();
            CONFIGURATION.put(configurationKey, configuration);
            return configuration;
        }
    }

    private static Configuration configSafe(final String key) {
        Objects.requireNonNull(key);
        final Configuration configuration = CONFIGURATION.get(key);
        Fn.outBoot(Objects.isNull(configuration), LOGGER, JooqConfigurationException.class, JooqInfix.class);
        return configuration;
    }

    public static <T> JooqDsl getDao(final Class<T> clazz) {
        final Configuration configuration = configSafe(ZeroYml.lime.jooq.provider);
        return JooqDsl.init(vertxRef, configuration, clazz);
    }

    public static <T> JooqDsl getDao(final Class<T> clazz, final String key) {
        final Configuration configuration = configSafe(key);
        return JooqDsl.init(vertxRef, configuration, clazz);
    }

    public static <T> JooqDsl getDao(final Class<T> clazz, final DataPool pool) {
        final Configuration configuration = configDelay(pool);
        return JooqDsl.init(vertxRef, configuration, clazz);
    }

    /*
     * For secondary
     * DSLContext of three method:
     * 1. Static: vertx-jooq.yml -> provider
     * 2. Static/Configured: vertx-jooq.yml -> ( by Key )
     */
    public static DSLContext contextTrash() {
        return configSafe(ZeroYml.lime.jooq.orbit).dsl();
    }


    @Override
    public Configuration get() {
        return this.get(ZeroYml.lime.jooq.provider);
    }

    public Configuration get(final String key) {
        return CONFIGURATION.get(key);
    }
}
