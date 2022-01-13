package cn.originx.infix.mysql5;

import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.metadata.AbstractBuilder;
import io.vertx.tp.modular.metadata.AoReflector;
import io.vertx.tp.modular.metadata.AoSentence;

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
