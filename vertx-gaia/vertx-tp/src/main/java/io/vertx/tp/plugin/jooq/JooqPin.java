package io.vertx.tp.plugin.jooq;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.em.DatabaseType;
import io.vertx.up.exception.zero.JooqConfigurationException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Debugger;
import io.vertx.up.util.Ut;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultConnectionProvider;

import java.sql.Connection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class JooqPin {

    private static final Annal LOGGER = Annal.get(JooqPin.class);
    private static final ConcurrentMap<DatabaseType, SQLDialect> DIALECT =
            new ConcurrentHashMap<DatabaseType, SQLDialect>() {
                {
                    this.put(DatabaseType.MYSQL, SQLDialect.MYSQL);
                    this.put(DatabaseType.MYSQL5, SQLDialect.MYSQL_5_7);
                    this.put(DatabaseType.MYSQL8, SQLDialect.MYSQL_8_0);
                }
            };

    private static DataPool initPool(final JsonObject databaseJson) {
        final Database database = new Database();
        database.fromJson(databaseJson);
        return DataPool.create(database);
    }

    static Configuration initConfig(final DataPool pool) {
        // Initialized client
        final Database database = pool.getDatabase();
        final Configuration configuration = new DefaultConfiguration();
        SQLDialect dialect = DIALECT.get(database.getCategory());
        if (Objects.isNull(dialect)) {
            dialect = SQLDialect.DEFAULT;
        }
        configuration.set(dialect);
        final Connection connection = Fn.getJvm(() -> pool.getDataSource().getConnection());
        // Database object it bind to jooq configuration
        final ConnectionProvider provider = new DefaultConnectionProvider(connection);
        // Initialized default configuration
        configuration.set(provider);
        return configuration;
    }

    private static Configuration initConfig(final JsonObject options) {
        final DataPool pool = initPool(options);
        return initConfig(pool);
    }

    static ConcurrentMap<String, Configuration> initConfiguration(final JsonObject config) {
        /*
         * config should be configured in vertx-jooq.yml
         * jooq:
         *    # Standard configuration
         *    provider:
         *    # History configuration
         *    orbit:
         */
        final ConcurrentMap<String, Configuration> configurationMap =
                new ConcurrentHashMap<>();

        Fn.outUp(Ut.isNil(config) || !config.containsKey(Constants.DEFAULT_JOOQ),
                LOGGER, JooqConfigurationException.class, JooqPin.class);

        if (Ut.notNil(config)) {
            config.fieldNames().stream()
                    .filter(key -> Objects.nonNull(config.getValue(key)))
                    .filter(key -> config.getValue(key) instanceof JsonObject)
                    .forEach(key -> {
                        final JsonObject options = config.getJsonObject(key);
                        final Configuration configuration = initConfig(options);
                        configurationMap.put(key, configuration);
                        /*
                         * Process Jooq password by `debug` mode turn on
                         */
                        final JsonObject populated = options.copy();
                        if (!Debugger.onJooqPassword()) {
                            /*
                             * Hidden Password
                             */
                            populated.remove("password");
                        }
                        LOGGER.info("Jooq options: \n{0}", populated.encodePrettily());
                    });
        }
        return configurationMap;
    }
}
