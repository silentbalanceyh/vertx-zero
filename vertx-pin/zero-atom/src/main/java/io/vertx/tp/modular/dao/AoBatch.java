package io.vertx.tp.modular.dao;

import io.vertx.core.Future;
import io.vertx.up.commune.Record;

@SuppressWarnings("unchecked")
interface AoBatch {
    /* 批量插入 */
    Future<Record[]> insertAsync(Record... records);

    Record[] insert(Record... records);

    /* 按主键批量读取 */
    <ID> Future<Record[]> fetchByIdAsync(ID... ids);

    <ID> Record[] fetchById(ID... ids);

    /* 批量删除 */
    Future<Boolean> deleteAsync(Record... records);

    Boolean delete(Record... records);

    /* 批量更新 */
    Future<Record[]> updateAsync(Record... records);

    Record[] update(Record... records);

    /* 批量读取 */
    Future<Record[]> fetchAllAsync();

    Record[] fetchAll();
}
