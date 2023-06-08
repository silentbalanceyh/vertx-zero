package io.modello.dynamic.modular.metadata;

import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MKey;
import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.modello.atom.app.KDatabase;
import io.modello.dynamic.modular.sql.SqlDDLBuilder;
import io.modello.dynamic.modular.sql.SqlTypeProvider;
import io.modello.eon.em.EmKey;
import io.vertx.core.json.JsonArray;
import io.vertx.mod.atom.cv.em.CheckResult;
import io.vertx.mod.atom.cv.sql.SqlStatement;
import io.vertx.mod.atom.cv.sql.SqlWord;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractSentence implements AoSentence, SqlStatement {
    protected final transient KDatabase database;
    protected final transient SqlTypeProvider typeProvider;
    private final transient SqlDDLBuilder builder = SqlDDLBuilder.create();

    public AbstractSentence(final KDatabase database) {
        this.database = database;
        this.typeProvider = SqlTypeProvider.create(database);
    }

    // 精度类型表
    protected abstract ConcurrentMap<String, String> getPrecisionMap();

    // 长度类型表
    protected abstract ConcurrentMap<String, String> getLengthMap();

    /**
     * 生成列定义的SQL语句：`NAME` VARCHAR(255) NOT NULL
     */
    @Override
    public String segmentField(final MField field) {
        final StringBuilder segment = new StringBuilder(this.defineColumn(field));
        // 是否为空检查
        if (!field.getIsNullable() || field.getIsPrimary()) {
            segment.append(SqlWord.Comparator.NOT).append(" ").append(SqlWord.Comparator.NULL);
        }
        // 添加Comments部分
        segment.append(" ").append(SqlWord.Assistant.COMMENT).append(" '");
        if (Ut.isNotNil(field.getComments())) {
            segment.append(field.getComments()).append(VString.COMMA);
        }
        segment.append(field.getName()).append(VString.COMMA).append(field.getColumnName()).append("'");
        return segment.toString();
    }

    @Override
    public String segmentKey(final MKey key) {
        final StringBuilder segment = new StringBuilder();
        // 列处理
        final JsonArray columns = new JsonArray(key.getColumns());
        final List<String> columnList = new ArrayList<>();
        columns.forEach(column -> columnList.add("`" + column + "`"));
        final String columnStr = Ut.fromJoin(columnList, VString.COMMA);
        // 类型处理
        final EmKey.Type typeKey = Ut.toEnum(key.getType(), EmKey.Type.class);
        if (EmKey.Type.PRIMARY == typeKey) {
            segment.append(MessageFormat.format(SqlStatement.CONSTRAINT_PK, key.getName(), columnStr));
        } else if (EmKey.Type.UNIQUE == typeKey) {
            segment.append(MessageFormat.format(SqlStatement.CONSTRAINT_UK, key.getName(), columnStr));
        }
        return segment.toString();
    }


    @Override
    public String constraintDrop(final String tableName, final String constraintName) {
        return this.builder.buildDropConstraint(tableName, constraintName);
    }

    @Override
    public String constraintAdd(final String tableName, final MKey key) {
        return this.builder.buildAddConstraint(tableName, this.segmentKey(key));
    }

    @Override
    public String columnDrop(final String tableName, final String column) {
        return this.builder.buildDropColumn(tableName, column);
    }

    @Override
    public String columnDropRename(final String tableName, final String column, final String newColumn, final String fieldType) {
        return this.builder.buildDropRenameColumn(tableName, column, newColumn);
    }

    @Override
    public String columnAdd(final String tableName, final MField field) {
        return this.builder.buildAddColumn(tableName, this.segmentField(field));
    }

    @Override
    public String columnAlter(final String tableName, final MField field) {
        return this.builder.buildAlterColumn(tableName, this.segmentField(field));
    }

    @Override
    public String columnType(final MField field) {
        return this.typeProvider.getColumnType(field.getColumnType());
    }

    @Override
    public JsonArray mappingList(final String key) {
        return this.typeProvider.getMappingList(key);
    }

    @Override
    public CheckResult checkFieldType(final MField field, final ConcurrentMap<String, Object> columnDetail) {
        return CheckResult.PASS;
    }


    /**
     * 类型定义
     */
    protected String getType(final MField field) {
        final StringBuilder type = new StringBuilder();
        final String rawType = this.typeProvider.getColumnType(field.getColumnType());
        // 判断当前类型是否包含了括号
        String actualType = rawType;
        // 虽然(MAX)是SQL必须的，但对其他数据库不会产生任何影响
        if (rawType.contains("(") && !rawType.contains("(MAX)")) {
            actualType = rawType.split("\\(")[VValue.IDX];
        }
        // 后缀处理 (12)，(12,2)的基本格式，包括长度
        type.append(this.defineSuffix(field, actualType));
        return type.toString();
    }

    protected String defineSuffix(final MField field, final String actualType) {
        final StringBuilder type = new StringBuilder();
        // 是否有 Precision
        if (null != field.getPrecision()) {
            if (this.getPrecisionMap().containsKey(actualType)) {
                type.append(MessageFormat.format(this.getPrecisionMap().get(actualType), String.valueOf(field.getLength()),
                    field.getPrecision()));
            }
        } else if (this.getLengthMap().containsKey(actualType)) {
            type.append(MessageFormat.format(this.getLengthMap().get(actualType), String.valueOf(field.getLength())));
        } else {
            type.append(actualType);
        }
        return type.toString();
    }

    protected String defineColumn(final MField field) {
        final StringBuilder segment = new StringBuilder();
        // 原始类型
        final String type = this.getType(field);
        // 添加字段名
        segment.append(this.columnDdl(field.getColumnName()))
            .append(" ")
            .append(type).append(" ");
        return segment.toString();
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
