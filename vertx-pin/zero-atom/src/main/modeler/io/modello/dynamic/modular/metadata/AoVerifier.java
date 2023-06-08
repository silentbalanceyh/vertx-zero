package io.modello.dynamic.modular.metadata;

public interface AoVerifier {
    /* 验证表是否存在 */
    boolean verifyTable(String tableName);

    /* 验证表中的列是否存在 */
    boolean verifyColumn(String tableName, String columnName);

    /* 验证表中的列是否存在（类型是否预期） */
    boolean verifyColumn(String tableName, String columnName, String expectedType);

    /* 验证表中的约束是否存在 */
    boolean verifyConstraint(String tableName, String constraintName);
}
