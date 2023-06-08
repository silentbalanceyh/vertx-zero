package io.modello.dynamic.modular.jdbc;

import io.horizon.eon.VValue;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.modello.atom.app.KDatabase;
import io.modello.dynamic.modular.sql.SqlOutput;
import io.vertx.mod.atom.error._500EmptySQLException;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.database.DataPool;
import io.vertx.up.util.Ut;
import org.jooq.Record;
import org.jooq.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("all")
public class DataConnection implements AoConnection {
    private static final Cc<String, DataPool> CC_DS = Cc.open();
    private final transient DataPool dbPool;
    private final transient KDatabase database;

    /* 一旦调用connect方法证明连接切换到对应的数据库中 */
    @SuppressWarnings("all")
    public DataConnection(final KDatabase database) {
        synchronized (getClass()) {
            this.database = database;
            // 初始化dbPool连接池，每一个Jdbc Url保证一个连接池
            this.dbPool = CC_DS.pick(() -> DataPool.create(database), database.getJdbcUrl());
            // Fn.po?l(Pool.POOL, database.getJdbcUrl(), () -> DataPool.create(database));
        }
    }

    @Override
    public int execute(final String sql) {
        Fn.outWeb(Ut.isNil(sql), _500EmptySQLException.class, this.getClass());
        this.getLogger().debug("[DB] 执行SQL：{0}", sql);
        final DSLContext context = this.getDSL();
        final Query query = context.query(sql);
        final int ret = query.execute();
        return VValue.ZERO <= ret ? ret : io.horizon.eon.VValue.RC_FAILURE;
    }

    @Override
    public KDatabase getDatabase() {
        return this.database;
    }

    @Override
    public Connection getConnection() {
        final DataSource dataSource = this.dbPool.getDataSource();
        return Fn.failOr(dataSource::getConnection, dataSource);
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
