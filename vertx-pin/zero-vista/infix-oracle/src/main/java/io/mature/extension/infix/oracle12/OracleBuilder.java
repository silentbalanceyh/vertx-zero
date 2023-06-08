package io.mature.extension.infix.oracle12;


import io.modello.dynamic.modular.jdbc.AoConnection;
import io.modello.dynamic.modular.metadata.AbstractBuilder;
import io.modello.dynamic.modular.metadata.AoReflector;
import io.modello.dynamic.modular.metadata.AoSentence;

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
