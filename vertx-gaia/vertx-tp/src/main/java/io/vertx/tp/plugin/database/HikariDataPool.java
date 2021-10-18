package io.vertx.tp.plugin.database;

import com.zaxxer.hikari.HikariDataSource;
import io.vertx.tp.error.DataSourceException;
import io.vertx.up.commune.config.Database;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultConnectionProvider;

import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HikariDataPool implements DataPool {
    private static final Annal LOGGER = Annal.get(HikariDataPool.class);
    private static final String OPT_AUTO_COMMIT = "hikari.auto.commit";
    private static final String OPT_CONNECTION_TIMEOUT = "hikari.connection.timeout";
    private static final String OPT_IDLE_TIMEOUT = "hikari.idle.timeout";
    private static final String OPT_MAX_LIFETIME = "hikari.max.lifetime";
    private static final String OPT_MINIMUM_IDLE = "hikari.minimum.idle";
    private static final String OPT_MAXIMUM_POOL_SIZE = "hikari.maximum.pool.size";
    private static final String OPT_POOL_NAME = "hikari.name";
    private static final ConcurrentMap<String, DataPool> POOL_SWITCH = new ConcurrentHashMap<>();
    /*
     * Database Options for HikariDataBase, for current version add new parameter for KikariDatabase
     * performance turning, it's recommend by
     * https://stackoverflow.com/questions/50014066/spring-boot-2-hikari-connection-pool-optimization
     * Please refer above link for more details.
     */
    // Statement Related
    private static final String OPT_STATEMENT_CACHED = "hikari.statement.cached";
    private static final String OPT_STATEMENT_CACHE_SIZE = "hikari.statement.cache.size";
    private static final String OPT_STATEMENT_CACHE_SQL_LIMIT = "hikari.statement.cache.sqllimit";
    // Use Related
    private static final String OPT_USE_SERVER_PREP_STMT = "hikari.use.server.statement";
    private static final String OPT_USE_LOCAL_SESSION_STATE = "hikari.use.local.session";
    private static final String OPT_USE_LOCAL_TRANSACTION_STATE = "hikari.use.local.transaction";
    private static final String OPT_USE_NEW_IO = "hikari.use.new.io";
    private static final String OPT_USE_COMPRESSION = "hikari.use.compression";
    // Advanced
    private static final String OPT_REWRITE_BATCHED_STMT = "hikari.rewrite.batched.statement";
    private static final String OPT_CACHE_METADATA = "hikari.cache.resultset.metadata";
    private static final String OPT_CACHE_SERVER_CONFIG = "hikari.cache.server.configuration";
    private static final String OPT_ELIDE_COMMIT = "hikari.elideset.autocommit";
    private static final String OPT_MAINTAIN_TIMESTAT = "hikari.maintain.timestat";
    private final transient Database database;
    /* Each jdbc url has one Pool here **/
    private transient DSLContext context;
    private transient HikariDataSource dataSource;

    HikariDataPool(final Database database) {
        this.database = database;
    }

    private void initPool() {
        if (Objects.nonNull(this.database)) {
            // Default configuration, 300 s as connection timeout for long time working
            final Boolean autoCommit = this.database.getOption(OPT_AUTO_COMMIT, Boolean.TRUE);

            this.dataSource.setAutoCommit(autoCommit);
            this.dataSource.setConnectionTimeout(this.database.getOption(OPT_CONNECTION_TIMEOUT, 300000L));
            this.dataSource.setIdleTimeout(this.database.getOption(OPT_IDLE_TIMEOUT, 600000L));
            this.dataSource.setMaxLifetime(this.database.getOption(OPT_MAX_LIFETIME, 25600000L));
            this.dataSource.setMinimumIdle(this.database.getOption(OPT_MINIMUM_IDLE, 256));
            this.dataSource.setMaximumPoolSize(this.database.getOption(OPT_MAXIMUM_POOL_SIZE, 512));

            // Default attributes
            this.dataSource.addDataSourceProperty("cachePrepStmts", this.database.getOption(OPT_STATEMENT_CACHED, "true"));
            this.dataSource.addDataSourceProperty("prepStmtCacheSize", this.database.getOption(OPT_STATEMENT_CACHE_SIZE, "2048"));
            this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", this.database.getOption(OPT_STATEMENT_CACHE_SQL_LIMIT, "4096"));

            // Use Related
            this.dataSource.addDataSourceProperty("useServerPrepStmts", this.database.getOption(OPT_USE_SERVER_PREP_STMT, "true"));
            this.dataSource.addDataSourceProperty("useLocalSessionState", this.database.getOption(OPT_USE_LOCAL_SESSION_STATE, "true"));
            this.dataSource.addDataSourceProperty("useLocalTransactionState", this.database.getOption(OPT_USE_LOCAL_TRANSACTION_STATE, "true"));
            this.dataSource.addDataSourceProperty("useNewIO", this.database.getOption(OPT_USE_NEW_IO, "true"));
            this.dataSource.addDataSourceProperty("useCompression", this.database.getOption(OPT_USE_COMPRESSION, "true"));

            // Advanced Configuration
            this.dataSource.addDataSourceProperty("rewriteBatchedStatements", this.database.getOption(OPT_REWRITE_BATCHED_STMT, "true"));
            this.dataSource.addDataSourceProperty("cacheResultSetMetadata", this.database.getOption(OPT_CACHE_METADATA, "true"));
            this.dataSource.addDataSourceProperty("cacheServerConfiguration", this.database.getOption(OPT_CACHE_SERVER_CONFIG, "true"));
            // this.dataSource.addDataSourceProperty("elideSetAutoCommits", this.database.getOption(OPT_ELIDE_COMMIT, "true"));
            this.dataSource.addDataSourceProperty("maintainTimeStats", this.database.getOption(OPT_MAINTAIN_TIMESTAT, "false"));
            // Data pool name
            this.dataSource.setPoolName(this.database.getOption(OPT_POOL_NAME, "ZERO-POOL-DATA"));
        }
    }

    private void initDelay() {
        /*
         * Initializing data source
         */
        this.initJdbc();
        /*
         * Initializing data source pool
         */
        this.initPool();
        /*
         * Initializing data source of jooq
         */
        this.initJooq();
    }

    @Override
    public DataPool switchTo() {
        return Fn.pool(POOL_SWITCH, this.database.getJdbcUrl(), () -> {
            final Database database = new Database();
            database.fromJson(this.database.toJson());
            /*
             * Tun-Off auto commit to switch auto
             */
            database.getOptions().remove(OPT_AUTO_COMMIT);
            final DataPool ds = new HikariDataPool(database);
            final Annal logger = Annal.get(this.getClass());
            logger.info("[ DP ] Data Pool Hash : {0}", ds.hashCode());
            return ds;
        });
    }

    @Override
    public DSLContext getExecutor() {
        if (Objects.isNull(this.context)) {
            this.initDelay();
        }
        return this.context;
    }

    @Override
    public HikariDataSource getDataSource() {
        if (Objects.isNull(this.dataSource)) {
            this.initDelay();
        }
        return this.dataSource;
    }

    @Override
    public Database getDatabase() {
        return this.database;
    }

    private void initJooq() {
        if (null == this.context) {
            try {
                /* Init Jooq configuration */
                final Configuration configuration = new DefaultConfiguration();
                final ConnectionProvider provider = new DefaultConnectionProvider(this.dataSource.getConnection());
                configuration.set(provider);
                /* Dialect selected */
                final SQLDialect dialect = Pool.DIALECT.get(this.database.getCategory());
                LOGGER.debug("Jooq Database ：Dialect = {0}, Database = {1}, ", dialect, this.database.toJson().encodePrettily());
                configuration.set(dialect);
                this.context = DSL.using(configuration);
            } catch (final SQLException ex) {
                // LOGGER.jvm(ex);
                throw new DataSourceException(this.getClass(), ex, this.database.getJdbcUrl());
            }
        }
    }

    private void initJdbc() {
        if (null == this.dataSource) {
            /*
             * Very important here, here are unique pool of old code with singleton
             * this.dataSource = Ut.singleton(HikariDataSource.class);
             *
             * Fix Issue: IllegalState The configuration of the pool is sealed once started
             * Root Cause: Here we could not use singleton pool, must create new one of DataSource.
             * If you use singleton design pattern here, when you want to switch datasource or reuse
             * the old data source, above issue will re-produce.
             *
             * Here are some background: once HikariCPPool started, you should not change the jdbcUrl.
             * If you want to change the jdbcUrl, above exception will throw out.
             *
             * The pre-condition is that you have set jdbcUrl before, and the HikariCPPool did not
             * distinguish whether it's the same data source by `jdbcUrl`, instead by java reference.
             * If we used singleton design pattern here, it means that you get previous reference each time
             * `old == new` will be true, when you set jdbcUrl again, above issue will throw out.
             *
             */
            this.dataSource = new HikariDataSource();
            /*
             * Ignore driverClass after jdbc4
             * But jdbc4 may caused issue of 'no suitable driver' when deployment.
             */
            this.dataSource.setJdbcUrl(this.database.getJdbcUrl());
            this.dataSource.setUsername(this.database.getUsername());
            this.dataSource.setPassword(this.database.getPassword());
            /*
             * Fix bug for 'no suitable driver'
             */
            this.dataSource.setDriverClassName(this.database.getDriverClassName());
            // dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        }
    }
}
