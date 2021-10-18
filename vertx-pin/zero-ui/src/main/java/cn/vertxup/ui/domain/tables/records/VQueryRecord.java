/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ui.domain.tables.records;


import cn.vertxup.ui.domain.tables.VQuery;
import cn.vertxup.ui.domain.tables.interfaces.IVQuery;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VQueryRecord extends UpdatableRecordImpl<VQueryRecord> implements VertxPojo, Record5<String, String, String, String, String>, IVQuery {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>DB_ETERNAL.V_QUERY.KEY</code>. 「key」- 选项主键
     */
    @Override
    public VQueryRecord setKey(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.V_QUERY.KEY</code>. 「key」- 选项主键
     */
    @Override
    public String getKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>DB_ETERNAL.V_QUERY.PROJECTION</code>. 「projection」-
     * query/projection:[], 默认列过滤项
     */
    @Override
    public VQueryRecord setProjection(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.V_QUERY.PROJECTION</code>. 「projection」-
     * query/projection:[], 默认列过滤项
     */
    @Override
    public String getProjection() {
        return (String) get(1);
    }

    /**
     * Setter for <code>DB_ETERNAL.V_QUERY.PAGER</code>. 「pager」-
     * query/pager:{}, 分页选项
     */
    @Override
    public VQueryRecord setPager(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.V_QUERY.PAGER</code>. 「pager」-
     * query/pager:{}, 分页选项
     */
    @Override
    public String getPager() {
        return (String) get(2);
    }

    /**
     * Setter for <code>DB_ETERNAL.V_QUERY.SORTER</code>. 「sorter」-
     * query/sorter:[], 排序选项
     */
    @Override
    public VQueryRecord setSorter(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.V_QUERY.SORTER</code>. 「sorter」-
     * query/sorter:[], 排序选项
     */
    @Override
    public String getSorter() {
        return (String) get(3);
    }

    /**
     * Setter for <code>DB_ETERNAL.V_QUERY.CRITERIA</code>. 「criteria」-
     * query/criteria:{}, 查询条件选项
     */
    @Override
    public VQueryRecord setCriteria(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.V_QUERY.CRITERIA</code>. 「criteria」-
     * query/criteria:{}, 查询条件选项
     */
    @Override
    public String getCriteria() {
        return (String) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, String, String, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<String, String, String, String, String> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return VQuery.V_QUERY.KEY;
    }

    @Override
    public Field<String> field2() {
        return VQuery.V_QUERY.PROJECTION;
    }

    @Override
    public Field<String> field3() {
        return VQuery.V_QUERY.PAGER;
    }

    @Override
    public Field<String> field4() {
        return VQuery.V_QUERY.SORTER;
    }

    @Override
    public Field<String> field5() {
        return VQuery.V_QUERY.CRITERIA;
    }

    @Override
    public String component1() {
        return getKey();
    }

    @Override
    public String component2() {
        return getProjection();
    }

    @Override
    public String component3() {
        return getPager();
    }

    @Override
    public String component4() {
        return getSorter();
    }

    @Override
    public String component5() {
        return getCriteria();
    }

    @Override
    public String value1() {
        return getKey();
    }

    @Override
    public String value2() {
        return getProjection();
    }

    @Override
    public String value3() {
        return getPager();
    }

    @Override
    public String value4() {
        return getSorter();
    }

    @Override
    public String value5() {
        return getCriteria();
    }

    @Override
    public VQueryRecord value1(String value) {
        setKey(value);
        return this;
    }

    @Override
    public VQueryRecord value2(String value) {
        setProjection(value);
        return this;
    }

    @Override
    public VQueryRecord value3(String value) {
        setPager(value);
        return this;
    }

    @Override
    public VQueryRecord value4(String value) {
        setSorter(value);
        return this;
    }

    @Override
    public VQueryRecord value5(String value) {
        setCriteria(value);
        return this;
    }

    @Override
    public VQueryRecord values(String value1, String value2, String value3, String value4, String value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IVQuery from) {
        setKey(from.getKey());
        setProjection(from.getProjection());
        setPager(from.getPager());
        setSorter(from.getSorter());
        setCriteria(from.getCriteria());
    }

    @Override
    public <E extends IVQuery> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached VQueryRecord
     */
    public VQueryRecord() {
        super(VQuery.V_QUERY);
    }

    /**
     * Create a detached, initialised VQueryRecord
     */
    public VQueryRecord(String key, String projection, String pager, String sorter, String criteria) {
        super(VQuery.V_QUERY);

        setKey(key);
        setProjection(projection);
        setPager(pager);
        setSorter(sorter);
        setCriteria(criteria);
    }

    /**
     * Create a detached, initialised VQueryRecord
     */
    public VQueryRecord(cn.vertxup.ui.domain.tables.pojos.VQuery value) {
        super(VQuery.V_QUERY);

        if (value != null) {
            setKey(value.getKey());
            setProjection(value.getProjection());
            setPager(value.getPager());
            setSorter(value.getSorter());
            setCriteria(value.getCriteria());
        }
    }

        public VQueryRecord(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }
}
