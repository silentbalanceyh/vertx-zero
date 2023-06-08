package io.mature.extension.infix.oracle12.cv;

public interface OracleStatement {
    /* 检查表是否存在 */
    String E_TABLE = "SELECT COUNT(*) FROM ALL_TABLES WHERE OWNER=''{0}'' AND TABLE_NAME=''{1}''";

    /* 读取约束信息 */
    String R_CONSTRAINTS = "SELECT DISTINCT CONSTRAINT_NAME, CONSTRAINT_TYPE FROM USER_CONSTRAINTS WHERE OWNER=''{0}'' AND TABLE_NAME=''{1}'' ORDER BY CONSTRAINT_NAME";

    /* 读取列信息 */
    String R_COLUMNS = "SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE OWNER=''{0}'' AND TABLE_NAME=''{1}''";

    /* 读取列信息 */
    String R_COLUMNS_NULLABLE = "SELECT NULLABLE FROM DBA_TAB_COLUMNS WHERE OWNER=''{0}'' AND TABLE_NAME=''{1}'' AND COLUMN_NAME=''{2}''";

    /* oracle 约束变更 */
    String ATDC_CONSTRAINT = "ALTER TABLE {0} DROP CONSTRAINT {1};";

    /* oracle 列变更 */
    String ATMC_COLUMN = "ALTER TABLE {0} MODIFY {1};";
}
