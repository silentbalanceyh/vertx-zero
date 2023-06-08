package io.modello.specification.action;

import io.horizon.uca.qr.Criteria;
import io.modello.specification.HRecord;
import io.modello.specification.atom.HAtom;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * 数据库访问器
 */
public interface HDao extends
    HReader,       // 读取
    HWriter,       // 写入
    HSearcher,     // 搜索
    HBatch,        // 批量
    HAggregator,   // 聚集
    HPredicate     // 检查
{
    // ================ Direct =================

    /**
     * SQL语句直接执行，返回影响的行数，此处的执行是反向引用模式，每个 HDao 都可以调用静态方法实现原生SQL
     * 的执行过程，而真正在执行时其实内部走 static 静态调用。
     */
    int execute(String sql);

    /**
     * 挂载到元数据中，主要用于链接 metadata，理论上来讲，若直接使用线程池化模式下处理此处的 HDao 不会出现
     * 线程安全问题，但不排除在 mount 过程中由于模型本身的切换引起 "线程安全" 问题，现在整体模型的 HDao 操
     * 作行为通常会包含两级：
     * <pre><code>
     *     1. 模型级：HDao 的构造是按照模型的 namespace + identifier 的模式进行，这种模式下最少可以保证
     *        模型和模型之间的 Action 部分不产生任何冲突，但是无法处理模型变种。
     *     2. 模型变种通常是如：
     *        2.1. 标识规则变化：模型级和组件级
     *        2.2. 核心规则变化：集成系统处理
     *        上述两种变化通常会调用 `mount` 方法将当前组件捆绑到模型中，后续可在此处设置二级缓存来处理。
     * </code></pre>
     */
    HDao mount(HAtom atom);

}

/**
 * 内置接口：聚集函数
 */
interface HAggregator {

    Future<Long> countAsync(Criteria criteria);

    Future<Long> countAsync(JsonObject criteria);

    Long count(Criteria criteria);

    Long count(JsonObject criteria);
}

/**
 * 内置接口：批量执行
 */
@SuppressWarnings("unchecked")
interface HBatch {
    /* 批量插入 */
    Future<HRecord[]> insertAsync(HRecord... records);

    HRecord[] insert(HRecord... records);

    /* 按主键批量读取 */
    <ID> Future<HRecord[]> fetchByIdAsync(ID... ids);

    <ID> HRecord[] fetchById(ID... ids);

    /* 批量删除 */
    Future<Boolean> deleteAsync(HRecord... records);

    Boolean delete(HRecord... records);

    /* 批量更新 */
    Future<HRecord[]> updateAsync(HRecord... records);

    HRecord[] update(HRecord... records);

    /* 批量读取 */
    Future<HRecord[]> fetchAllAsync();

    HRecord[] fetchAll();
}

/**
 * 内置接口：检查器
 */
interface HPredicate {

    Future<Boolean> existAsync(Criteria criteria);

    Boolean exist(Criteria criteria);

    Future<Boolean> existAsync(JsonObject criteria);

    Boolean exist(JsonObject criteria);

    Future<Boolean> missAsync(Criteria criteria);

    Boolean miss(Criteria criteria);

    Future<Boolean> missAsync(JsonObject criteria);

    Boolean miss(JsonObject criteria);
}

/**
 * 内置接口：读取器
 */
interface HReader {
    /* 根据ID查找某条记录 */
    <ID> Future<HRecord> fetchByIdAsync(ID id);

    <ID> HRecord fetchById(ID id);

    /* 根据ID查找某条记录（多个ID）*/
    Future<HRecord> fetchOneAsync(Criteria criteria);

    HRecord fetchOne(Criteria criteria);

    Future<HRecord> fetchOneAsync(JsonObject criteria);

    HRecord fetchOne(JsonObject criteria);
}

/**
 * 内置接口：搜索器
 */
interface HSearcher {

    /* 搜索专用接口，生成对应的 Pagination */
    Future<JsonObject> searchAsync(final JsonObject filters);

    JsonObject search(final JsonObject filters);

    /* 直接搜索读取 */
    Future<HRecord[]> fetchAsync(final JsonObject criteria);

    HRecord[] fetch(final JsonObject criteria);
}

/**
 * 内置接口：写入器接口，会被OxDao继承，主要负责Record的写入操作，包括增删改
 */
interface HWriter {
    /* 插入单条记录 identifier已经包含在了Record中 */
    Future<HRecord> insertAsync(HRecord record);

    HRecord insert(HRecord record);

    /* 删除记录集 */
    Future<Boolean> deleteAsync(HRecord record);

    boolean delete(HRecord record);
    /* 更新单条记录集 */

    Future<HRecord> updateAsync(HRecord record);

    HRecord update(HRecord record);
}