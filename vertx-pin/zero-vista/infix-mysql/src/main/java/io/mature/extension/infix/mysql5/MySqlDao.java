package io.mature.extension.infix.mysql5;

import io.modello.dynamic.modular.dao.AbstractDao;
import io.modello.dynamic.modular.jdbc.AoConnection;
import io.modello.dynamic.modular.metadata.AoSentence;

public class MySqlDao extends AbstractDao {

    MySqlDao(final AoConnection connection) {
        super(connection);
    }

    @Override
    public AoSentence sentence() {
        return new MySqlSentence(this.conn.getDatabase());
    }
}
