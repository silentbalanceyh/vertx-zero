package io.modello.dynamic.modular.sql;

import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MKey;
import io.horizon.eon.VValue;
import io.horizon.eon.em.typed.ChangeFlag;
import io.modello.dynamic.modular.metadata.AoReflector;
import io.modello.dynamic.modular.metadata.AoSentence;
import io.modello.dynamic.modular.metadata.FieldComparator;
import io.modello.eon.em.EmKey;
import io.vertx.mod.atom.cv.em.CheckResult;
import io.vertx.mod.atom.error.*;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class SqlDDLProvider {
    private static SqlDDLProvider INSTANCE;
    private transient AoSentence sentence;
    private transient AoReflector reflector;

    private SqlDDLProvider() {
    }

    public static SqlDDLProvider create() {
        synchronized (SqlDDLProvider.class) {
            if (null == INSTANCE) {
                INSTANCE = new SqlDDLProvider();
            }
            return INSTANCE;
        }
    }

    public SqlDDLProvider on(final AoSentence sentence) {
        this.sentence = sentence;
        return this;
    }

    public SqlDDLProvider on(final AoReflector reflector) {
        this.reflector = reflector;
        return this;
    }

    public List<String> prepareCreateLines(final Schema schema) {
        Fn.outWeb(null == this.sentence, _501AoSentenceNullException.class, this.getClass());
        final List<String> lines = new ArrayList<>();
        {
            /*
             * 字段定义信息
             * */
            final Set<MField> fields = new TreeSet<>(new FieldComparator());
            fields.addAll(Arrays.asList(schema.getFields()));
            fields.forEach(field -> {
                if (Objects.nonNull(field.getIsPrimary()) && field.getIsPrimary()) {
                    /*
                     * 追加逻辑防止MySQL自然连接问题
                     * natural join 要求同名主键通常是位于最前边，所以把主键定义放到最前边执行
                     */
                    final String pkLine = this.sentence.segmentField(field);
                    if (Ut.isNotNil(pkLine)) {
                        lines.add(0, pkLine);
                    }
                } else {
                    this.addLine(lines, this.sentence.segmentField(field));
                }
            });
        }
        {
            /* Unique/Primary 约束 */
            for (final MKey key : schema.getKeys()) {
                this.addLine(lines, this.sentence.segmentKey(key));
            }
        }
        return lines;
    }

    public List<String> prepareAlterLines(final Schema schema) {
        Fn.outWeb(null == this.sentence, _501AoSentenceNullException.class, this.getClass());
        Fn.outWeb(null == this.reflector, _501AoReflectorNullException.class, this.getClass());
        // 删除约束语句
        final List<String> lines = new ArrayList<>(this.prepareDropConstraints(schema));
        // 列状态处理
        final ConcurrentMap<ChangeFlag, Collection<String>> statusMap = this.getColumnStatus(schema);
        // DELETE COLUMN
        // 不删除实体表字段，采用rename的方式  2020/01/10 HuaRui
        //        lines.addAll(this.prepareDropColumns(schema, statusMap.get(ChangeFlag.DELETE)));
        lines.addAll(this.prepareDropRenameColumns(schema, statusMap.get(ChangeFlag.DELETE)));
        // ADD COLUMN
        lines.addAll(this.prepareAddColumns(schema, statusMap.get(ChangeFlag.ADD)));
        // ALTER COLUMN
        lines.addAll(this.prepareAlterColumns(schema, statusMap.get(ChangeFlag.UPDATE)));
        // 约束添加
        lines.addAll(this.prepareAddConstraints(schema));
        return lines;
    }

    private List<String> prepareDropColumns(final Schema schema, final Collection<String> columns) {
        final String table = schema.getTable();
        final List<String> segments = new ArrayList<>();
        columns.forEach(column -> this.addLine(segments,
            this.sentence.columnDrop(table, column)));
        return segments;
    }

    private List<String> prepareDropRenameColumns(final Schema schema, final Collection<String> columns) {
        final String table = schema.getTable();
        final List<String> segments = new ArrayList<>();
        // 数据库真实字段信息
        final List<ConcurrentMap<String, Object>> columnDetailList = this.reflector.getColumnDetail(table);
        columns.stream()
            // 过滤那些已删除的字段，防止它们二次处理
            .filter(column -> !column.endsWith(SqlDDLConstant.getDeleteFieldFlag()))
            .forEach(column -> {
                final ConcurrentMap<String, Object> columnDetail = this.reflector.getColumnDetails(column, columnDetailList);
                final String fieldType = this.reflector.getFieldType(columnDetail);
                this.addLine(segments, this.sentence.columnDropRename(table, column, SqlDDLConstant.combineNewName(column), fieldType));
            });
        return segments;
    }

    private List<String> prepareAddColumns(final Schema schema, final Collection<String> columns) {
        final String table = schema.getTable();
        final List<String> segments = new ArrayList<>();
        final long rows = this.reflector.getTotalRows(table);
        columns.forEach(column -> {
            final MField field = schema.getFieldByColumn(column);
            // NOT NULL，系统中有数据，不可添加非空字段
            Fn.outWeb(VValue.ZERO < rows && !field.getIsNullable(), _500NullableAddException.class, this.getClass(),
                /* ARG1：被修改的表名 */ table,
                /* ARG2：被修改的列名 */ column);
            final String sql = this.sentence.columnAdd(table, field);
            this.addLine(segments, sql);
        });
        return segments;
    }

    private List<String> prepareAlterColumns(final Schema schema, final Collection<String> columns) {
        final String table = schema.getTable();
        final List<String> segments = new ArrayList<>();
        final long rows = this.reflector.getTotalRows(table);
        // 数据库真实字段信息
        final List<ConcurrentMap<String, Object>> columnDetailList = this.reflector.getColumnDetail(table);
        columns.forEach(column -> {
            do {
                final MField field = schema.getFieldByColumn(column);
                final ConcurrentMap<String, Object> columnDetail = this.reflector.getColumnDetails(column, columnDetailList);
                final CheckResult checkResult = this.sentence.checkFieldType(field, columnDetail);
                // 检查结果为 SKIP 则直接跳过不生成语句，FAILED则报异常
                if (checkResult == CheckResult.SKIP) {
                    continue;
                } else if (checkResult == CheckResult.FAILED) {
                    Fn.outWeb(true, _500TypeAlterException.class, this.getClass(),
                        /* ARG1：被修改的表名 */ table,
                        /* ARG2：被修改的列名 */ column);
                }

                // 已存在NULL数据时，字段不可变更为 NOT NULL
                if (VValue.ZERO < rows) {
                    final long nullRows = this.reflector.getNullRows(table, this.sentence.columnDdl(column));
                    Fn.outWeb(VValue.ZERO < nullRows && !field.getIsNullable(), _500NullableAlterException.class, this.getClass(),
                        /* ARG1：被修改的表名 */ table,
                        /* ARG2：被修改的列名 */ column);
                }
                final String sql = this.sentence.columnAlter(table, field);
                this.addLine(segments, sql);
            } while (false);
        });
        return segments;
    }

    private ConcurrentMap<ChangeFlag, Collection<String>> getColumnStatus(final Schema schema) {
        final Set<String> oldColumns = new HashSet<>(this.reflector.getColumns(schema.getTable()));
        final Set<String> newColumns = schema.getColumnNames();
        final ConcurrentMap<ChangeFlag, Collection<String>> statusMap = new ConcurrentHashMap<>();
        /* ADD：新添加字段 **/
        statusMap.put(ChangeFlag.ADD, Ut.elementDiff(newColumns, oldColumns));
        /* DELETE：删除字段 **/
        statusMap.put(ChangeFlag.DELETE, Ut.elementDiff(oldColumns, newColumns));
        /* UPDATE：更新字段 **/
        statusMap.put(ChangeFlag.UPDATE, Ut.elementIntersect(oldColumns, newColumns));
        return statusMap;
    }

    private List<String> prepareDropConstraints(final Schema schema) {
        final String table = schema.getTable();
        final ConcurrentMap<String, EmKey.Type> constraints = this.reflector.getConstraints(table);
        final List<String> segments = new ArrayList<>();
        constraints.forEach((name, type) -> this.addLine(segments, this.sentence.constraintDrop(table, name)));
        return segments;
    }

    private List<String> prepareAddConstraints(final Schema schema) {
        final String table = schema.getTable();
        final List<String> segments = new ArrayList<>();
        for (final MKey key : schema.getKeys()) {
            this.addLine(segments, this.sentence.constraintAdd(table, key));
        }
        return segments;
    }

    private void addLine(final List<String> sqls, final String line) {
        if (Ut.isNotNil(line)) {
            sqls.add(line);
        }
    }
}
