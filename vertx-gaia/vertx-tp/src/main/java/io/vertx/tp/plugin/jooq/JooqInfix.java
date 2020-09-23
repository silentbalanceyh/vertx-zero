package io.vertx.tp.plugin.jooq;

import io.vertx.core.Vertx;
import io.vertx.tp.plugin.cache.Harp;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.Plugins;
import io.vertx.up.exception.zero.JooqConfigurationException;
import io.vertx.up.exception.zero.JooqVertxNullException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.plugin.Infix;
import io.vertx.up.util.Ut;
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

    public static void init(final Vertx vertx) {
        vertxRef = vertx;
        final ConcurrentMap<String, Configuration> inited =
                Infix.init(Plugins.Infix.JOOQ, JooqPin::initConfiguration, JooqInfix.class);
        CONFIGURATION.putAll(inited);
        /*
         * Check L1 cache enabled
         */
        Harp.init(vertx);
    }

    public static <T> T getDao(final Class<T> clazz) {
        return getDao(clazz, Constants.DEFAULT_JOOQ);
    }

    public static <T> T getDao(final Class<T> clazz, final String key) {
        return getDao(clazz, CONFIGURATION.get(key));
    }

    public static <T> T getDao(final Class<T> clazz, final DataPool pool) {
        final Database database = pool.getDatabase();
        final Configuration configuration;
        if (CONFIGURATION.containsKey(database.getJdbcUrl())) {
            configuration = CONFIGURATION.get(database.getJdbcUrl());
        } else {
            configuration = JooqPin.initConfig(pool);
            CONFIGURATION.put(database.getJdbcUrl(), configuration);
        }
        return getDao(clazz, configuration);
    }

    private static <T> T getDao(final Class<T> clazz, final Configuration configuration) {
        Fn.outUp(null == vertxRef, LOGGER, JooqVertxNullException.class, clazz);
        final T dao = Ut.instance(clazz, configuration);
        Ut.invoke(dao, "setVertx", vertxRef);
        return dao;
    }

    /*
     * DSLContext of three method:
     * 1. Static: vertx-jooq.yml -> provider
     * 2. Static/Configured: vertx-jooq.yml -> ( by Key )
     */
    public static DSLContext getDSL(final String key) {
        final Configuration configuration = CONFIGURATION.get(key);
        Fn.outUp(null == configuration, LOGGER, JooqConfigurationException.class, JooqInfix.class);
        return Objects.isNull(configuration) ? null : configuration.dsl();
    }

    public static DSLContext getDSL() {
        return getDSL(Constants.DEFAULT_JOOQ);
    }

    @Override
    public Configuration get() {
        return this.get(Constants.DEFAULT_JOOQ);
    }

    public Configuration get(final String key) {
        return CONFIGURATION.get(key);
    }
}
