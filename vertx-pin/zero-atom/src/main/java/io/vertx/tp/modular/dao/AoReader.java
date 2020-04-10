package io.vertx.tp.modular.dao;

import io.vertx.core.Future;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.commune.Record;

/**
 * 内置接口：读取器
 */
interface AoReader {
    /* 根据ID查找某条记录 */
    <ID> Future<Record> fetchByIdAsync(ID id);

    <ID> Record fetchById(ID id);

    /* 根据ID查找某条记录（多个ID）*/
    Future<Record> fetchOneAsync(Criteria criteria);

    Record fetchOne(Criteria criteria);
}
