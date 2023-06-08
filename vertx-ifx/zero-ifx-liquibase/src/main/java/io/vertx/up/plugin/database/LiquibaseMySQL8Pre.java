package io.vertx.up.plugin.database;

import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomPreconditionErrorException;
import liquibase.exception.CustomPreconditionFailedException;
import liquibase.precondition.CustomPrecondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Objects;

public class LiquibaseMySQL8Pre implements CustomPrecondition {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiquibaseMySQL8Pre.class);
    /*
     * 该函数为 MySQL 升级8.0过后引起的约束问题执行临时解决方案
     * liquibase.exception.DatabaseException: The table does not comply with the requirements by an external plugin
     * Issue Link: https://github.com/liquibase/liquibase/issues/1918
     * 该问题还未关闭，上述问题是由于 DATABASECHANGELOG 没有主键引起的，所以此处需执行一个逻辑
     * - 1）检查DATABASECHANGELOG是否存在
     * - 2）检查该表是否存在主键，如果没有主键则创建主键
     * 执行日志如下
     * [INFO] Creating database history table with name: DATABASECHANGELOG
     * [INFO] Reading from DATABASECHANGELOG
     * <该代码的执行位置>
     */
    @Override
    public void check(final Database database)
            throws CustomPreconditionFailedException, CustomPreconditionErrorException {
        final String tableName = database.getDatabaseChangeLogTableName();
        // DATABASECHANGELOG 检查用户是否定制过该表方法
        try {
            // 1）检查表是否存在，不存在则跳过
            if(this.ifSkipTable(database, tableName)){
                return;
            }
            // 2) 检查表是否有主键，存在主键则跳过
            if(this.ifSkipPrimary(database, tableName)){
                return;
            }
            // 3）检查满足条件，修改表追加主键
            this.execFix(database, tableName);
        }catch (Throwable ex){
            // 检查未通过
            throw new CustomPreconditionFailedException(ex.getMessage());
        }
    }

    private void execFix(final Database database, final String tableName) throws SQLException{
        final String sqlTpl = "ALTER TABLE {0} ADD PRIMARY KEY(ID, AUTHOR, FILENAME)";
        final Connection conn = getConnection(database);
        final Statement stmt = conn.createStatement();

        LOGGER.info("[Zero] Liquibase Fix ? {}", sqlTpl);
        stmt.executeUpdate(MessageFormat.format(sqlTpl, tableName));
    }

    private boolean ifSkipTable(final Database database, final String tableName) throws SQLException {
        final String sqlTpl = "SELECT COUNT(*) AS RET FROM information_schema.TABLES WHERE TABLE_NAME=''{0}'' AND TABLE_SCHEMA=''{1}''";
        final Connection conn = getConnection(database);
        final Statement stmt = conn.createStatement();
        final String sql = MessageFormat.format(sqlTpl, tableName, database.getLiquibaseCatalogName());
        final ResultSet rs = stmt.executeQuery(sql);
        int counter = 0;
        while(rs.next()){
            counter = rs.getInt("RET");
        }
        final boolean result = counter == 0;
        LOGGER.info("[Zero] Liquibase table Missing = {}, SQL: {}", result, sql);
        return result;
    }

    private boolean ifSkipPrimary(final Database database, final String tableName) throws SQLException{
        final String sqlTpl = "SELECT col.COLUMN_NAME FROM" +
                " information_schema.TABLE_CONSTRAINTS tab," +
                " information_schema.KEY_COLUMN_USAGE col" +
                " WHERE col.CONSTRAINT_NAME = tab.CONSTRAINT_NAME" +
                " AND col.TABLE_NAME = tab.TABLE_NAME" +
                " AND tab.CONSTRAINT_TYPE = ''PRIMARY KEY''" +
                " AND col.TABLE_NAME = ''{0}''" +
                " AND tab.CONSTRAINT_SCHEMA = ''{1}''";
        final Connection conn = getConnection(database);
        final Statement stmt = conn.createStatement();
        final String sql = MessageFormat.format(sqlTpl, tableName, database.getLiquibaseCatalogName());
        final ResultSet rs = stmt.executeQuery(sql);
        String name = null;
        while(rs.next()){
            name = rs.getString("COLUMN_NAME");
        }
        final boolean result = Objects.nonNull(name);
        LOGGER.info("[Zero] Liquibase primary key Existed = {}, SQL: {}", result, sql);
        return result;
    }

    private Connection getConnection(final Database database){
        final JdbcConnection connection = (JdbcConnection) database.getConnection();
        return connection.getUnderlyingConnection();
    }
}
