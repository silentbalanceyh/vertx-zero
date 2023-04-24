package io.vertx.tp.modular.dao;

import io.horizon.specification.modeler.HAtom;
import io.horizon.specification.modeler.HDao;
import io.horizon.specification.modeler.Record;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.modular.dao.internal.*;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.metadata.AoSentence;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * 数据库核心操作，操作名称命名为：
 * 同步：operation
 * 异步：operationAsync
 * 针对所有的数据库操作统一处理
 */
public abstract class AbstractDao implements HDao {

    protected final transient AoConnection conn;
    /*
     * 插入/删除专用
     */
    private final transient UFlush flush = UFlush.create();
    /*
     * 唯一记录读取，返回 Record
     */
    private final transient UUnique unique = UUnique.create();
    /*
     * 列表读取，返回 Record[]
     */
    private final transient UList list = UList.create();
    /*
     * 分页搜索读取，返回 JsonObject
     * {
     *     "list",
     *     "count",
     * }
     */
    private final transient USearch search = USearch.create();

    private final transient UAggr aggr = UAggr.create();

    /*
     * 本类中的挂载有两个方法：
     * 1. 构造挂载：
     *   - AoSentence
     *   - AoConnection
     * 2. 延迟挂载：
     *   - DataAtom  （ 参考下边的 mount 方法 ）
     */
    public AbstractDao(final AoConnection conn) {
        this.conn = conn;
        // 前期绑定
        this.flush.on(this.conn).on(this.sentence());
        this.unique.on(this.conn).on(this.sentence());
        this.list.on(this.conn).on(this.sentence());
        this.search.on(this.conn).on(this.sentence());
        this.aggr.on(this.conn).on(this.sentence());
    }

    @Override
    public HDao mount(final HAtom atom) {
        // 读取器直接穿透，让读取器挂载在元数据上
        this.unique.on(atom);     // Uniqueor 挂载
        this.flush.on(atom);     // Partakor 挂载
        this.list.on(atom);       // Listor挂载
        this.search.on(atom);     // Searchor 挂载
        this.aggr.on(atom);   // Aggregator 挂载
        return this;
    }

    // AoDao
    /*
     * 直接执行 sql 语句的专用方法，同步执行相关 SQL 语句
     */
    @Override
    public int execute(final String sql) {
        return this.conn.execute(sql);
    }

    // AoAggregator / AoPredicate
    @Override
    public Long count(final Criteria criteria) {
        return Fn.orNull((long) Values.RANGE, () -> this.aggr.count(criteria), criteria);
    }

    @Override
    public Future<Long> countAsync(final Criteria criteria) {
        return Ux.future(this.count(criteria));
        // return Fn.getNull(Ux.future((long) Values.RANGE), () -> this.aggr.countAsync(criteria), criteria);
    }

    @Override
    public Long count(final JsonObject criteria) {
        return this.count(Criteria.create(criteria));
    }

    @Override
    public Future<Long> countAsync(final JsonObject criteria) {
        return this.countAsync(Criteria.create(criteria));
    }

    @Override
    public Boolean exist(final Criteria criteria) {
        return Fn.orNull(Boolean.FALSE, () -> this.aggr.existing(criteria), criteria);
    }

    @Override
    public Future<Boolean> existAsync(final Criteria criteria) {
        return Ux.future(this.exist(criteria));
        // return Fn.getNull(Ux.future(Boolean.FALSE), () -> this.aggr.existingAsync(criteria), criteria);
    }

    @Override
    public Boolean miss(final Criteria criteria) {
        return Fn.orNull(Boolean.FALSE, () -> this.aggr.missing(criteria), criteria);
    }

    @Override
    public Future<Boolean> missAsync(final Criteria criteria) {
        return Ux.future(this.miss(criteria));
        // return Fn.getNull(Ux.future(Boolean.FALSE), () -> this.aggr.missingAsync(criteria), criteria);
    }

    @Override
    public Future<Boolean> existAsync(final JsonObject criteria) {
        return this.existAsync(Criteria.create(criteria));
    }

    @Override
    public Boolean exist(final JsonObject criteria) {
        return this.exist(Criteria.create(criteria));
    }

    @Override
    public Future<Boolean> missAsync(final JsonObject criteria) {
        return this.missAsync(Criteria.create(criteria));
    }

    @Override
    public Boolean miss(final JsonObject criteria) {
        return this.miss(Criteria.create(criteria));
    }

    // AoWriter
    /*
     * 1. 说明
     * 单条数据插入数据专用方法，同时支持同步和异步操作
     * 异步操作：insertAsync
     * 同步操作：insert
     *
     * 2. 语句
     * 生成最终的SQL语句如：
     * INSERT INTO <TABLE>
     *     (<COLUMN1>,<COLUMN2>,<COLUMN3>,... )
     * VALUES
     *     (<VALUE1>,<VALUE2>,<VALUE3>,... )
     * 单条记录的插入操作
     *
     * 3. 参数和异常
     * 参数信息：
     *     <TABLE>：表名
     *     <COLUMN>：表中的数据列
     *     <VALUE>：数据列待插入的数据
     * 异常信息：
     *     如果出现重复记录：
     *     ERR-100017：cn.vertxup.tp.error._417DataTransactionException
     *         「DAO」数据访问操作遇到了问题，请联系管理员，检查后台数据，错误详情：...
     */
    @Override
    public Future<Record> insertAsync(final Record record) {
        return Ux.future(this.insert(record));
        // return Fn.getNull(Ux.future(), () -> this.flush.insertAsync(record), record);
    }

    @Override
    public Record insert(final Record record) {
        return Fn.orNull(null, () -> this.flush.insert(record), record);
    }

    @Override
    public Record[] insert(final Record... records) {
        if (Objects.isNull(records)) {
            return new Record[]{};
        }
        return this.flush.insert(records);
    }

    @Override
    public Future<Record[]> insertAsync(final Record... records) {
        return Ux.future(this.insert(records));
/*        if (Objects.isNull(records)) {
            return Ux.future(new Record[]{});
        }
        return this.flush.insertAsync(records);*/
    }

    /*
     *
     */
    /*
     * 1. 说明
     * 单条数据插入数据专用方法，同时支持同步和异步操作
     * 异步操作：updateAsync
     * 同步操作：update
     *
     * 2. 语句
     * 生成最终的SQL语句如：
     * UPDATE <TABLE>
     * SET (<COLUMN1> = <VALUE1>, <COLUMN2> = <VALUE2>, ... )
     * WHERE ID = ?
     * 单条记录的更新操作
     *
     * 3. 参数和异常
     * 参数信息：
     *     <TABLE>：表名
     *     <COLUMN>：表中的数据列
     *     <VALUE>：数据列待插入的数据
     * 异常信息：
     *     ERR-100017：cn.vertxup.tp.error._417DataTransactionException
     *         「DAO」数据访问操作遇到了问题，请联系管理员，检查后台数据，错误详情：...
     */

    @Override
    public Future<Record[]> updateAsync(final Record... records) {
        return Ux.future(this.update(records));
        /*if (Objects.isNull(records)) {
            return Ux.future(new Record[]{});
        }
        return this.flush.updateAsync(records);*/
    }

    @Override
    public Record[] update(final Record... records) {
        if (Objects.isNull(records)) {
            return new Record[]{};
        }
        return this.flush.update(records);
    }

    @Override
    public Future<Record> updateAsync(final Record record) {
        return Ux.future(this.update(record));
        // return Fn.getNull(Ux.future(), () -> this.flush.updateAsync(record), record);
    }

    @Override
    public Record update(final Record record) {
        return Fn.orNull(null, () -> this.flush.update(record), record);
    }

    /*
     * 1. 说明
     * 单条记录的数据库记录读取操作，同时支持同步和异步操作
     * 异步操作：fetchByIdAsync
     * 同步操作：fetchById
     *
     * 2. 语句
     * 生成最终的SQL语句如：
     * SELECT * FROM <TABLE> WHERE <ID>=<VALUE>
     *
     * 3. 参数和异常
     * 参数信息：
     *     <TABLE>：表名
     *     <ID>：主键列
     *     <VALUE>：主键列传入的值
     * 异常信息：
     *     （无），如果查询不到则直接返回空记录 {}。
     */
    @Override
    public <ID> Record fetchById(final ID id) {
        return Fn.orNull(null, () -> this.unique.fetchById(id), id);
    }

    @Override
    public <ID> Future<Record> fetchByIdAsync(final ID id) {
        return Ux.future(this.fetchById(id));
        // return Fn.getNull(Ux.future(), () -> this.unique.fetchByIdAsync(id), id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ID> Record[] fetchById(final ID... ids) {
        if (Objects.isNull(ids)) {
            return new Record[]{};
        }
        return this.list.fetchByIds(ids);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ID> Future<Record[]> fetchByIdAsync(final ID... ids) {
        return Ux.future(this.fetchById(ids));
/*        if (Objects.isNull(ids)) {
            return Ux.future(new Record[]{});
        }
        return this.list.fetchByIdsAsync(ids);*/
    }

    @Override
    public Record[] fetchAll() {
        return this.list.fetchAll();
    }

    @Override
    public Future<Record[]> fetchAllAsync() {
        return Ux.future(this.fetchAll());
        // return this.list.fetchAllAsync();
    }

    /*
     * 1. 说明
     * 单条记录的数据库记录读取操作，同时支持同步和异步操作
     * 异步操作：fetchOneAsync
     * 同步操作：fetchOne
     *
     * 2. 语句
     * 生成最终的SQL语句如：
     * SELECT * FROM <TABLE> WHERE <COL1>=<VALUE1> AND | OR <COL2>=<VALUE2>
     *
     * 3. 参数和异常
     * 参数信息：
     *     <TABLE>：表名
     *     <COLx>：字段列
     *     <VALUEx>：主键列传入的值
     * 异常信息：
     *     （无），如果查询不到则直接返回空记录 {}。
     */
    @Override
    public Record fetchOne(final Criteria criteria) {
        return Fn.orNull(null, () -> this.unique.fetchOne(criteria), criteria);
    }

    @Override
    public Future<Record> fetchOneAsync(final Criteria criteria) {
        return Ux.future(this.fetchOne(criteria));
        // return Fn.getNull(Ux.future(), () -> this.unique.fetchOneAsync(criteria), criteria);
    }

    @Override
    public Future<Record> fetchOneAsync(final JsonObject criteria) {
        return this.fetchOneAsync(Criteria.create(criteria));
    }

    @Override
    public Record fetchOne(final JsonObject criteria) {
        return this.fetchOne(Criteria.create(criteria));
    }

    @Override
    public JsonObject search(final JsonObject query) {
        return Fn.orNull(Ux.pageData(), () -> this.search.search(query), query);
    }

    @Override
    public Record[] fetch(final JsonObject criteria) {
        return Fn.orNull(new Record[]{}, () -> this.search.query(criteria), criteria);
    }


    @Override
    public Future<JsonObject> searchAsync(final JsonObject query) {
        return Ux.future(this.search(query));
        // return Fn.getNull(Ux.futureJ(), () -> this.search.searchAsync(query), query);
    }

    @Override
    public Future<Record[]> fetchAsync(final JsonObject criteria) {
        return Ux.future(this.fetch(criteria));
        // return Fn.getNull(Ux.future(new Record[]{}), () -> this.search.queryAsync(criteria), criteria);
    }

    /*
     * 1. 说明
     * 单条记录的数据库记录删除操作，同时支持同步和异步操作
     * 异步操作：deleteAsync
     * 同步操作：delete
     *
     * 2. 语句
     * 生成最终的SQL语句如：
     * DELETE FROM <TABLE> WHERE <ID>=<VALUE>

     * 3. 参数和异常
     * 参数信息：
     *     <TABLE>：表名
     *     <ID>：主键列
     *     <VALUE>：主键列传入的值
     * 异常信息：
     *     （无），无副作用的方法，即使删除的记录不存在，也会返回 Boolean 中的 TRUE
     */
    @Override
    public Future<Boolean> deleteAsync(final Record record) {
        return Ux.future(this.delete(record));
        // return Fn.getNull(Ux.future(Boolean.FALSE), () -> this.flush.deleteAsync(record), record);
    }

    @Override
    public boolean delete(final Record record) {
        return Fn.orNull(Boolean.FALSE, () -> this.flush.delete(record), record);
    }

    @Override
    public Future<Boolean> deleteAsync(final Record... records) {
        return Ux.future(this.delete(records));
/*        if (Objects.isNull(records)) {
            return Ux.future(Boolean.FALSE);
        }
        return this.flush.deleteAsync(records);*/
    }

    @Override
    public Boolean delete(final Record... records) {
        if (Objects.isNull(records)) {
            return Boolean.FALSE;
        }
        return this.flush.delete(records);
    }

    // Logger
    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    /* 行语句构造器 */
    public abstract AoSentence sentence();
}
