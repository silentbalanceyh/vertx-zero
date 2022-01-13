package cn.originx.infix.oracle12;


import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.modular.dao.AoDao;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.jdbc.DataConnection;
import io.vertx.tp.modular.jdbc.Pin;
import io.vertx.tp.modular.metadata.AoBuilder;
import io.vertx.up.commune.config.Database;
import io.vertx.up.fn.Fn;

/**
 * MySQL统一接口
 */
public class OraclePin implements Pin {

    private AoConnection getConnection(final Database database) {
        return Fn.pool(AoCache.POOL_CONNECTION, database.getJdbcUrl(), () -> (new DataConnection(database)));
    }

    @Override
    public AoBuilder getBuilder(final Database database) {
        return Fn.poolThread(AoCache.POOL_T_BUILDER, () -> new OracleBuilder(this.getConnection(database)));
    }

    @Override
    public AoDao getDao(final Database database) {
        return null;
    }
}
