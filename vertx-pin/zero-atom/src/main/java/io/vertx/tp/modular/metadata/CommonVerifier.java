package io.vertx.tp.modular.metadata;

import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.up.eon.bridge.Values;

public class CommonVerifier implements AoVerifier {

    private final transient AoConnection conn;
    private final transient AoSentence sentence;

    public CommonVerifier(final AoConnection conn, final AoSentence sentence) {
        this.conn = conn;
        this.sentence = sentence;
    }

    @Override
    public boolean verifyTable(final String tableName) {
        final String sql = this.sentence.expectTable(tableName);
        final Long counter = this.conn.count(sql);
        return Values.ZERO < counter;
    }

    @Override
    public boolean verifyColumn(final String tableName, final String columnName) {
        return false;
    }

    @Override
    public boolean verifyColumn(final String tableName, final String columnName, final String expectedType) {
        return false;
    }

    @Override
    public boolean verifyConstraint(final String tableName, final String constraintName) {
        return false;
    }
}
