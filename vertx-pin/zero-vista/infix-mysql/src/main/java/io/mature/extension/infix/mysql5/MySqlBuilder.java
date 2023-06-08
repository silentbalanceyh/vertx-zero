package io.mature.extension.infix.mysql5;

import io.modello.dynamic.modular.jdbc.AoConnection;
import io.modello.dynamic.modular.metadata.AbstractBuilder;
import io.modello.dynamic.modular.metadata.AoReflector;
import io.modello.dynamic.modular.metadata.AoSentence;

public class MySqlBuilder extends AbstractBuilder {
    /* 隐藏实现，外部不可初始化 */
    MySqlBuilder(final AoConnection conn) {
        super(conn);
    }

    @Override
    public AoSentence getSentence() {
        return new MySqlSentence(this.conn.getDatabase());
    }

    @Override
    public AoReflector getReflector() {
        return new MySqlReflector(this.conn);
    }
}
