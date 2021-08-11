package io.vertx.tp.modular.jdbc;

import io.vertx.tp.error._500EmptySQLException;
import io.vertx.up.eon.KResult;
import io.vertx.tp.modular.sql.SqlOutput;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.jooq.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("all")
public class DataConnection implements AoConnection {
    private final transient DataPool dbPool;
    private final transient Database database;

    /* 一旦调用connect方法证明连接切换到对应的数据库中 */
    @SuppressWarnings("all")
    public DataConnection(final Database database) {
        synchronized (getClass()) {
            this.database = database;
            // 初始化dbPool连接池，每一个Jdbc Url保证一个连接池
            this.dbPool = Fn.pool(Pool.POOL, database.getJdbcUrl(),
                    () -> DataPool.create(database));
        }
    }

    @Override
    public int execute(final String sql) {
        Fn.outWeb(Ut.isNil(sql), _500EmptySQLException.class, this.getClass());
        this.getLogger().debug("[DB] 执行SQL：{0}", sql);
        final DSLContext context = this.getDSL();
        final Query query = context.query(sql);
        final int ret = query.execute();
        return Values.ZERO <= ret ? ret : KResult.RC_FAILURE;
    }

    @Override
    public Database getDatabase() {
        return this.database;
    }

    @Override
    public Connection getConnection() {
        final DataSource dataSource = this.dbPool.getDataSource();
        return Fn.getJvm(dataSource::getConnection, dataSource);
    }

    @Override
    public DSLContext getDSL() {
        return this.dbPool.getExecutor();
    }

    @Override
    public List<ConcurrentMap<String, Object>> select(final String sql,
                                                      final String[] columns) {
        final Result queries = this.fetch(sql);
        return SqlOutput.toMatrix(queries, columns);
    }

    @Override
    public <T> List<T> select(final String sql,
                              final String column) {
        final Result queries = this.fetch(sql);
        return SqlOutput.toList(queries, column);
    }

    private Result fetch(final String sql) {
        Fn.outWeb(Ut.isNil(sql), _500EmptySQLException.class, this.getClass());
        this.getLogger().debug("[DB] 执行SQL select：{0}", sql);
        final DSLContext context = this.getDSL();
        final ResultQuery<Record> query = context.resultQuery(sql);
        return query.fetch();
    }

    @Override
    public Long count(final String sql) {
        Fn.outWeb(Ut.isNil(sql), _500EmptySQLException.class, this.getClass());
        this.getLogger().debug("[DB] 执行SQL count：{0}", sql);
        final DSLContext context = this.getDSL();
        final ResultQuery<Record> query = context.resultQuery(sql);
        final Record record = query.fetchOne();
        return record.getValue(0, Long.class);
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
