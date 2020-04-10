package io.vertx.tp.modular.dao;

import io.vertx.core.Future;
import io.vertx.up.commune.Record;

/**
 * 内置接口：写入器接口，会被OxDao继承，主要负责Record的写入操作，包括增删改
 */
interface AoWriter {
    /* 插入单条记录 identifier已经包含在了Record中 */
    Future<Record> insertAsync(Record record);

    Record insert(Record record);

    /* 删除记录集 */
    Future<Boolean> deleteAsync(Record record);

    boolean delete(Record record);
    /* 更新单条记录集 */

    Future<Record> updateAsync(Record record);

    Record update(Record record);
}
