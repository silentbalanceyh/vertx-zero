package io.vertx.tp.plugin.jooq;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.Constants;
import io.vertx.up.exception.zero.JooqConfigurationException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Debugger;
import io.vertx.up.util.Ut;
import org.jooq.Configuration;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JooqPin {

    private static final Annal LOGGER = Annal.get(JooqPin.class);

    private static DataPool initPool(final JsonObject databaseJson) {
        final Database database = new Database();
        database.fromJson(databaseJson);
        return DataPool.create(database);
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
                    final DataPool pool = initPool(options);
                    final Configuration configuration = pool.configuration();
                    configurationMap.put(key, configuration);
                    /*
                     * Process Jooq password by `debug` mode turn on
                     */
                    final JsonObject populated = options.copy();
                    if (Debugger.offPasswordHidden()) {
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
