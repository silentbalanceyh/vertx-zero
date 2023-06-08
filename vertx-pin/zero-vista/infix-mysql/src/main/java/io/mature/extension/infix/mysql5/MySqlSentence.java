package io.mature.extension.infix.mysql5;

import cn.vertxup.atom.domain.tables.pojos.MField;
import io.mature.extension.infix.mysql5.cv.MySqlStatement;
import io.mature.extension.infix.mysql5.cv.MySqlWord;
import io.modello.atom.app.KDatabase;
import io.modello.dynamic.modular.metadata.AbstractSentence;
import io.vertx.core.json.JsonArray;
import io.vertx.mod.atom.cv.em.CheckResult;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MySqlSentence extends AbstractSentence implements MySqlStatement, MySqlWord {
    /**
     * 带Pattern的列映射：PRECISION
     **/
    private static final ConcurrentMap<String, String> PRECISION_MAP = new ConcurrentHashMap<>();
    /**
     * 带Pattern的列映射：LENGTH
     **/
    private static final ConcurrentMap<String, String> LENGTH_MAP = new ConcurrentHashMap<>();


    /* 设置行映射 **/
    static {
        /* 精度映射 **/
        PRECISION_MAP.put(Type.DECIAML, Pattern.P_DECIMAL);
        PRECISION_MAP.put(Type.NUMERIC, Pattern.P_NUMERIC);
        /* 长度映射 **/
        LENGTH_MAP.put(Type.CHAR, Pattern.P_CHAR);
        LENGTH_MAP.put(Type.VARCHAR, Pattern.P_VARCHAR);
        LENGTH_MAP.put(Type.BINARY, Pattern.P_BINARY);
        LENGTH_MAP.put(Type.VARBINARY, Pattern.P_VARBINARY);
    }

    MySqlSentence(final KDatabase database) {
        super(database);
    }

    @Override
    public ConcurrentMap<String, String> getPrecisionMap() {
        return PRECISION_MAP;
    }

    @Override
    public ConcurrentMap<String, String> getLengthMap() {
        return LENGTH_MAP;
    }

    @Override
    public String expectTable(final String tableName) {
        return MessageFormat.format(MySqlStatement.E_TABLE, this.database.getInstance(), tableName);
    }

    @Override
    public String constraintDrop(final String tableName, final String constraintName) {
        return MessageFormat.format(MySqlStatement.ATDC_CONSTRAINT, tableName, constraintName);
    }

    @Override
    public String columnAlter(final String tableName, final MField field) {
        return MessageFormat.format(MySqlStatement.ATMC_COLUMN, tableName, this.segmentField(field));
    }

    @Override
    public String columnDropRename(final String tableName, final String column, final String newColumn, final String fieldType) {
        return MessageFormat.format(MySqlStatement.ATMR_COLUMN, tableName, column, newColumn, fieldType);
    }

    /**
     * 读取包装列的字符
     * NAME -> `NAME`：MySQL
     */
    @Override
    public String columnDdl(final String column) {
        return '`' + column + '`';
    }

    @Override
    public CheckResult checkFieldType(final MField field, final ConcurrentMap<String, Object> columnDetail) {
        if (null == columnDetail) {
            return CheckResult.FAILED;
        }
        // 前端传入待更新信息
        final String metaDBType = this.columnType(field);
        // 数据库真实信息
        final String DBType = columnDetail.get(Metadata.DATA_TYPE).toString();
        final String length = columnDetail.get(Metadata.CHARACTER_LENGTH).toString().equalsIgnoreCase("NULL") ? "0" : columnDetail.get(Metadata.CHARACTER_LENGTH).toString();
        final String precision = columnDetail.get(Metadata.NUMERIC_PRECISION).toString().equalsIgnoreCase("NULL") ? "0" : columnDetail.get(Metadata.NUMERIC_PRECISION).toString();
        final String scale = columnDetail.get(Metadata.NUMERIC_SCALE).toString().equalsIgnoreCase("NULL") ? "0" : columnDetail.get(Metadata.NUMERIC_SCALE).toString();

        //先检查字段定义是否变化，如果没有变化(类型，长度，精度)的情况，不生成语句
        // 1.类型不变的情况
        if (DBType.equalsIgnoreCase(metaDBType)) {
            // TEXT 类型在数据库会有默认的65525长度，所以特殊处理
            if (DBType.equalsIgnoreCase(Type.TEXT)) {
                return CheckResult.SKIP;
            }
            // 长度和精度都有的情况，数据库侧，长度存放在 precision 字段，精度存放在 scale 字段
            if (null != field.getLength() && null != field.getPrecision()) {
                if (field.getLength().equals(Integer.parseInt(precision)) && field.getPrecision().equals(Integer.parseInt(scale))) {
                    return CheckResult.SKIP;
                } else {
                    return CheckResult.PASS;
                }
            }
            // 长度有的情况，数据库侧，长度存放在 length 字段
            if (null != field.getLength()) {
                if (field.getLength().equals(Integer.parseInt(length))) {
                    return CheckResult.SKIP;
                } else {
                    return CheckResult.PASS;
                }
            } else {
                return CheckResult.SKIP;
            }
        }
        // 特殊情况，boolean 在数据库中可能为 BIT 也可能为tinyint
        if (metaDBType.equalsIgnoreCase(Type.BIT)) {
            if (DBType.equalsIgnoreCase(Type.BIT) || DBType.equalsIgnoreCase(Type.TINYINT)) {
                return CheckResult.SKIP;
            }
        }

        // 2.类型变化的情况
        // mapping check
        final JsonArray mappingList = this.mappingList(DBType.toUpperCase());
        if (!mappingList.contains(metaDBType.toUpperCase())) {
            return CheckResult.FAILED;
        }
        return CheckResult.PASS;
    }


}
