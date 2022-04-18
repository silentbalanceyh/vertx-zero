package cn.originx.infix.mysql5;

import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.jdbc.DataConnection;
import io.vertx.tp.modular.jdbc.Pin;
import io.vertx.tp.modular.metadata.AoBuilder;
import io.vertx.up.commune.config.Database;
import io.vertx.up.experiment.mixture.HDao;
import io.vertx.up.fn.Fn;

/**
 * MySQL统一接口
 */
public class MySqlPin implements Pin {

    private AoConnection getConnection(final Database database) {
        return Fn.pool(AoCache.POOL_CONNECTION, database.getJdbcUrl(), () -> (new DataConnection(database)));
    }

    @Override
    public AoBuilder getBuilder(final Database database) {
        return Fn.poolThread(AoCache.POOL_T_BUILDER, () -> new MySqlBuilder(this.getConnection(database)));
    }

    @Override
    public HDao getDao(final Database database) {
        // return Fn.poolThread(AoCache.POOL_T_DAO, () -> new MySqlDao(this.getConnection(database)));
        /* 共享连接 */
        return new MySqlDao(this.getConnection(database));
    }
}
