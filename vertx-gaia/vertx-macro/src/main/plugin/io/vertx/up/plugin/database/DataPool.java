package io.vertx.up.plugin.database;

import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.modello.atom.app.KDatabase;
import io.vertx.up.commune.config.Database;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;

import javax.sql.DataSource;
import java.util.Objects;

/*
 * Connection Pool of third pool, the default implementation is HikariCP, in current version the connection pool
 * is not changed and could not be configured here.
 * 1) Basic JDBC configuration
 * 2) Additional configuration
 */
public interface DataPool {

    Cc<String, DataPool> CC_POOL_DYNAMIC = Cc.open();

    static DataPool create() {
        return create(Database.getCurrent());
    }

    static DataPool create(final KDatabase database) {
        return CC_POOL_DYNAMIC.pick(() -> {
            final Annal logger = Annal.get(DataPool.class);
            final DataPool ds = new HikariDataPool(database);
            logger.info("[ DP ] Data Pool Hash : {0}, URL: {1}",
                String.valueOf(ds.hashCode()), database.getJdbcUrl());
            return ds;
        }, database.getJdbcUrl());
    }

    static DataPool createAuto(final KDatabase database) {
        final DataPool ds = create(database);
        return ds.switchTo();
    }

    default Configuration configuration() {
        // Initialized client
        final KDatabase database = this.getDatabase();
        final Configuration configuration = new DefaultConfiguration();
        SQLDialect dialect = Pool.DIALECT.get(database.getCategory());
        if (Objects.isNull(dialect)) {
            dialect = SQLDialect.DEFAULT;
        }
        configuration.set(dialect);
        /*
         * jOOQ uses a ConnectionProvider behind the scenes and always calls acquire() before running a statement and
         * release() after running it. Depending on how you have configured your ConnectionProvider, this may or may
         * not close your JDBC connection. For example, when you pass jOOQ a standalone Connection, then it is never
         * closed (DefaultConnectionProvider). When you pass it a DataSource, then the Connection is always closed
         * (DataSourceConnectionProvider).
         *
         * The old code is as:
         * final Connection connection = Fn.getJvm(() -> pool.getDataSource().getConnection());
         * final ConnectionProvider provider = new DefaultConnectionProvider(connection);
         *
         * Here the new version is as following:
         * final ConnectionProvider provider = new DataSourceConnectionProvider(pool.getDataSource());
         *
         * It means that use `DataSourceConnectionProvider` instead and the background thread will call
         * `acquire()` to close the connection directly.
         */
        final ConnectionProvider provider = new DataSourceConnectionProvider(this.getDataSource());
        // Initialized default configuration
        configuration.set(provider);
        return configuration;
    }

    DataPool switchTo();

    /*
     * Executor of Jooq ( Context )
     */
    DSLContext getExecutor();

    /*
     * Data Source here
     */
    DataSource getDataSource();

    /*
     * Database reference
     */
    KDatabase getDatabase();
}
