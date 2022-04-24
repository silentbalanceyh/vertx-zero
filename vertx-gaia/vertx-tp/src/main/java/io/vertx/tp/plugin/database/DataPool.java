package io.vertx.tp.plugin.database;

import io.vertx.up.commune.config.Database;
import io.vertx.up.log.Annal;
import org.jooq.DSLContext;

import javax.sql.DataSource;

/*
 * Connection Pool of third pool, the default implementation is HikariCP, in current version the connection pool
 * is not changed and could not be configured here.
 * 1) Basic JDBC configuration
 * 2) Additional configuration
 */
public interface DataPool {
    static DataPool create() {
        return create(Database.getCurrent());
    }

    static DataPool create(final Database database) {
        return Pool.CC_POOL_DYNAMIC.pick(() -> {
            final Annal logger = Annal.get(DataPool.class);
            final DataPool ds = new HikariDataPool(database);
            logger.info("[ DP ] Data Pool Hash : {0}, URL: {1}",
                String.valueOf(ds.hashCode()), database.getJdbcUrl());
            return ds;
        }, database.getJdbcUrl());
    }

    static DataPool createAuto(final Database database) {
        final DataPool ds = create(database);
        return ds.switchTo();
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
    Database getDatabase();
}
