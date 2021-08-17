package io.vertx.tp.modular.dao;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.dao.internal.*;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.metadata.AoSentence;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.commune.Record;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

/**
 * 数据库核心操作，操作名称命名为：
 * 同步：operation
 * 异步：operationAsync
 * 针对所有的数据库操作统一处理
 */
public abstract class AbstractDao implements AoDao {

    protected final transient AoConnection conn;
    /*
     * 插入/删除专用
     */
    private final transient Partakor partakor = Partakor.create();
    /*
     * 唯一记录读取，返回 Record
     */
    private final transient Uniqueor uniqueor = Uniqueor.create();
    /*
     * 列表读取，返回 Record[]
     */
    private final transient Listor listor = Listor.create();
    /*
     * 分页搜索读取，返回 JsonObject
     * {
     *     "list",
     *     "count",
     * }
     */
    private final transient Searchor searchor = Searchor.create();

    private final transient Aggregator aggregator = Aggregator.create();

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
        this.partakor.on(this.conn).on(this.sentence());
        this.uniqueor.on(this.conn).on(this.sentence());
        this.listor.on(this.conn).on(this.sentence());
        this.searchor.on(this.conn).on(this.sentence());
        this.aggregator.on(this.conn).on(this.sentence());
    }

    @Override
    public AoDao mount(final DataAtom atom) {
        // 读取器直接穿透，让读取器挂载在元数据上
        this.uniqueor.on(atom);     // Uniqueor 挂载
        this.partakor.on(atom);     // Partakor 挂载
        this.listor.on(atom);       // Listor挂载
        this.searchor.on(atom);     // Searchor 挂载
        this.aggregator.on(atom);   // Aggregator 挂载
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
        return Ao.doCount(this.logger(), this.aggregator::count).apply(criteria);
    }

    @Override
    public Future<Long> countAsync(final Criteria criteria) {
        return Ux.future(this.count(criteria));
    }

    @Override
    public Boolean exist(final Criteria criteria) {
        return Ao.doBoolean(this.logger(), this.aggregator::existing).apply(criteria);
    }

    @Override
    public Future<Boolean> existAsync(final Criteria criteria) {
        return Ux.future(this.exist(criteria));
    }

    @Override
    public Boolean miss(final Criteria criteria) {
        return Ao.doBoolean(this.logger(), this.aggregator::missing).apply(criteria);
    }

    @Override
    public Future<Boolean> missAsync(final Criteria criteria) {
        return Ux.future(this.miss(criteria));
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
    }

    @Override
    public Record insert(final Record record) {
        return Ao.<Record>doFluent(this.logger(), this.partakor::insert).apply(record); // 防 null
    }

    @Override
    public Record[] insert(final Record... records) {
        return Ao.<Record[]>doFluent(this.logger(), this.partakor::insert).apply(records);
    }

    @Override
    public Future<Record[]> insertAsync(final Record... records) {
        return Ux.future(this.insert(records));
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
    }

    @Override
    public Record[] update(final Record... records) {
        return Ao.<Record[]>doFluent(this.logger(), this.partakor::update).apply(records);
    }

    @Override
    public Future<Record> updateAsync(final Record record) {
        return Ux.future(this.update(record));
    }

    @Override
    public Record update(final Record record) {
        return Ao.<Record>doFluent(this.logger(), this.partakor::update).apply(record); // 防 null
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
        return Ao.<ID, Record>doStandard(this.logger(), this.uniqueor::fetchById).apply(id);// 防 null
    }

    @Override
    public <ID> Future<Record> fetchByIdAsync(final ID id) {
        return Ao.<ID, Future<Record>>doStandard(this.logger(), this.uniqueor::fetchByIdAsync).apply(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ID> Record[] fetchById(final ID... ids) {
        return Ao.<ID[], Record[]>doStandard(this.logger(), this.listor::fetchByIds).apply(ids);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ID> Future<Record[]> fetchByIdAsync(final ID... ids) {
        return Ao.<ID[], Future<Record[]>>doStandard(this.logger(), this.listor::fetchByIdsAsync).apply(ids);
    }

    @Override
    public Record[] fetchAll() {
        return Ao.doSupplier(this.logger(), this.listor::fetchAll).get();
    }

    @Override
    public Future<Record[]> fetchAllAsync() {
        return Ao.doSupplier(this.logger(), this.listor::fetchAllAsync).get();
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
        return Ao.doStandard(this.logger(), this.uniqueor::fetchOne).apply(criteria);
    }

    @Override
    public JsonObject search(final JsonObject query) {
        return Ao.doStandard(this.logger(), this.searchor::search).apply(query);
    }

    @Override
    public Record[] fetch(final JsonObject criteria) {
        return Ao.doStandard(this.logger(), this.searchor::query).apply(criteria);
    }

    @Override
    public Future<Record> fetchOneAsync(final Criteria criteria) {
        return Ao.doStandard(this.logger(), this.uniqueor::fetchOneAsync).apply(criteria);
    }

    @Override
    public Future<JsonObject> searchAsync(final JsonObject query) {
        return Ao.doStandard(this.logger(), this.searchor::searchAsync).apply(query);
    }

    @Override
    public Future<Record[]> fetchAsync(final JsonObject criteria) {
        return Ao.doStandard(this.logger(), this.searchor::queryAsync).apply(criteria);
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
    }

    @Override
    public boolean delete(final Record record) {
        return Ao.<Record>doBoolean(this.logger(), this.partakor::delete).apply(record); // 防 null
    }

    @Override
    public Future<Boolean> deleteAsync(final Record... records) {
        return Ux.future(this.delete(records));
    }

    @Override
    public Boolean delete(final Record... records) {
        return Ao.<Record[]>doBoolean(this.logger(), this.partakor::delete).apply(records); // 防 null
    }

    // Logger
    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    /* 行语句构造器 */
    public abstract AoSentence sentence();
}
