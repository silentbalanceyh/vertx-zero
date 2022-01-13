package cn.originx.infix.mysql5;

import io.vertx.tp.modular.dao.AbstractDao;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.metadata.AoSentence;

public class MySqlDao extends AbstractDao {

    MySqlDao(final AoConnection connection) {
        super(connection);
    }

    @Override
    public AoSentence sentence() {
        return new MySqlSentence(this.conn.getDatabase());
    }
}
