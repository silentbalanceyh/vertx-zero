package io.modello.dynamic.modular.metadata;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Schema;

import java.util.Set;

/**
 * 用于创建表以及更新表专用的发布器，Origin X中只带了抽象层的内容，它主要负责下边工作
 * 1. 根据Json文件创建表结构（创建过后实现的是同步/合并，如果元数据仓库中已经包含了这个模型的元数据，则采用合并模式，保留原始关联）
 * 2. 反向操作：清除
 * Builder接口在 Origin X 中是抽象层的定义，仅仅定义了接口部分的内容，实现在插件中，系统目前的插件主要支持：
 * MySQL / Oracle 两种
 */
public interface AoBuilder {
    /**
     * 同步表信息接口（添加/更新）
     */
    boolean synchron(Schema schema);

    /**
     * 删除表信息
     */
    boolean purge(Schema schema);

    /**
     * 删除单个表结构
     */
    boolean purge(String tableName);

    /**
     * 删除多个表结构
     */
    boolean purge(Set<String> tableNames);

    /**
     * 重命名单个表结构
     */
    boolean rename(final String tableName);

    /**
     * 元数据和真实数据库信息报告
     */
    JsonObject report(final Schema schema);
}
