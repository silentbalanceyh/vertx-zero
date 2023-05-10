package cn.originx.infix.oracle12;


import io.modello.specification.action.HDao;
import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.jdbc.DataConnection;
import io.vertx.tp.modular.jdbc.Pin;
import io.vertx.tp.modular.metadata.AoBuilder;
import io.vertx.up.commune.config.Database;

/**
 * MySQL统一接口
 */
public class OraclePin implements Pin {

    private AoConnection getConnection(final Database database) {
        return AoCache.CC_CONNECTION.pick(() -> (new DataConnection(database)), database.getJdbcUrl());
        // return Fn.po?l(AoCache.POOL_CONNECTION, database.getJdbcUrl(), () -> (new DataConnection(database)));
    }

    @Override
    public AoBuilder getBuilder(final Database database) {
        return AoCache.CC_BUILDER.pick(() -> new OracleBuilder(this.getConnection(database)));
        // return Fn.po?lThread(AoCache.POOL_T_BUILDER, () -> new OracleBuilder(this.getConnection(database)));
    }

    @Override
    public HDao getDao(final Database database) {
        return null;
    }
}
