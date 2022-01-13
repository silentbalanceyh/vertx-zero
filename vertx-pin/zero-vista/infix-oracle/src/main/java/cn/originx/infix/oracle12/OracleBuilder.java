package cn.originx.infix.oracle12;


import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.metadata.AbstractBuilder;
import io.vertx.tp.modular.metadata.AoReflector;
import io.vertx.tp.modular.metadata.AoSentence;

public class OracleBuilder extends AbstractBuilder {
    /* 隐藏实现，外部不可初始化 */
    OracleBuilder(final AoConnection conn) {
        super(conn);
    }

    @Override
    public AoSentence getSentence() {
        return new OracleSentence(this.conn.getDatabase());
    }

    @Override
    public AoReflector getReflector() {
        return new OracleReflector(this.conn);
    }
}
