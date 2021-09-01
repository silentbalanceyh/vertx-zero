package io.vertx.tp.modular.metadata;

import cn.vertxup.atom.domain.tables.pojos.MField;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.em.CheckResult;
import io.vertx.tp.atom.cv.sql.SqlStatement;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.sql.SqlDDLBuilder;
import io.vertx.tp.modular.sql.SqlDDLProvider;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KValue;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractBuilder implements AoBuilder, SqlStatement {

    protected final transient AoConnection conn;
    private transient final SqlDDLBuilder builder = SqlDDLBuilder.create();
    private transient final SqlDDLProvider provider = SqlDDLProvider.create();

    public AbstractBuilder(final AoConnection conn) {
        this.conn = conn;
    }

    /* 行语句构造器 */
    public abstract AoSentence getSentence();

    /* 反向生成器 */
    public abstract AoReflector getReflector();

    /* 验证器 */
    public AoVerifier getVerifier() {
        return new CommonVerifier(this.conn, this.getSentence());
    }

    @Override
    public boolean synchron(final Schema schema) {
        // 检查表是否存在
        final String tableName = schema.getTable();
        final boolean exist = this.getVerifier().verifyTable(tableName);
        this.getLogger().info("[ Ox ] 检查表信息 table = {0}，是否存在？exist = {1}", tableName, exist);
        final List<String> lines = new ArrayList<>();
        final String sql;
        if (exist) {
            // 如果存在则执行ALTER语句
            lines.addAll(this.provider
                .on(this.getSentence())
                .on(this.getReflector())
                .prepareAlterLines(schema));
            sql = Ut.fromJoin(lines, "");
        } else {
            final Set<String> fields = schema.getFieldNames();
            if (0 == fields.size()) {
                return Boolean.TRUE;
            }
            // 表不存在，而且有表字段信息时，则执行CREATE语句
            lines.addAll(this.provider
                .on(this.getSentence())
                .prepareCreateLines(schema));
            sql = this.builder.buildCreateTable(schema.getTable(), lines);
        }
        final String[] segments = sql.split(";");
        this.getLogger().info("[ Ox ] sql: {0}", sql);
        int respCode = KValue.RC_SUCCESS;
        for (final String segment : segments) {
            if (Ut.notNil(segment)) {
                respCode = this.conn.execute(segment);
            }
        }
        return KValue.RC_SUCCESS == respCode;
    }

    @Override
    public boolean purge(final Schema schema) {
        return false;
    }

    @Override
    public boolean purge(final String tableName) {
        return false;
    }

    @Override
    public JsonObject report(final Schema schema) {
        final String table = schema.getTable();
        final AoReflector reflector = this.getReflector();
        final AoSentence sentence = this.getSentence();
        final JsonObject resultObj = new JsonObject();
        final JsonArray resultList = new JsonArray();

        // 数据库真实字段信息
        final List<ConcurrentMap<String, Object>> columnDetailList = reflector.getColumnDetail(table);
        // 根据 meta 数据库中的字段为基来生成报告
        schema.getColumnNames().forEach(column -> {
            final MField field = schema.getFieldByColumn(column);
            final ConcurrentMap<String, Object> columnDetail = reflector.getColumnDetails(column, columnDetailList);
            final CheckResult checkResult = sentence.checkFieldType(field, columnDetail);
            final JsonObject fieldResult = new JsonObject()
                // 对比结果为 SKIP 时，代表一致
                .put("same", checkResult == CheckResult.SKIP)
                .put("name", field.getName()).put("columnName", field.getColumnName())
                .put("type", sentence.columnType(field).toUpperCase()).put("oldType", columnDetail.get(reflector.getDataTypeWord()).toString().toUpperCase())
                .put("length", null != field.getLength() ? field.getLength() : 0).put("oldLength", columnDetail.get(reflector.getLengthWord()).toString().equalsIgnoreCase("NULL") ? "0" : columnDetail.get(reflector.getLengthWord()).toString());
            resultList.add(fieldResult);
        });
        return resultObj
            .put(KName.IDENTIFIER, schema.getEntity().getIdentifier())
            .put("details", resultList);
    }

    @Override
    public boolean rename(final String tableName) {
        final boolean exist = this.getVerifier().verifyTable(tableName);
        this.getLogger().info("[ Ox ] 检查表信息 table = {0}，是否存在？exist = {1}", tableName, exist);
        final String sql;
        if (exist) {
            sql = this.builder.buildRenameTable(tableName);
        } else {
            return Boolean.TRUE;
        }
        this.getLogger().info("[ Ox ] sql: {0}", sql);
        int respCode = KValue.RC_SUCCESS;
        if (Ut.notNil(sql)) {
            respCode = this.conn.execute(sql);
        }
        return KValue.RC_SUCCESS == respCode;
    }

    @Override
    public boolean purge(final Set<String> tableNames) {
        return false;
    }

    private Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
