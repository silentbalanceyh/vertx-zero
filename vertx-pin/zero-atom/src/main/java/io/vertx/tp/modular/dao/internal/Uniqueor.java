package io.vertx.tp.modular.dao.internal;

import io.vertx.core.Future;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.commune.Record;

/**
 * 工具类
 * 1. 只支持单记录结果
 * 2. 支持 SELECT 返回结果
 * 3. 连接查询引擎做细粒度查询
 * 只返回唯一数据集：
 * {
 * * field1: xx
 * * field2: xx
 * }
 */
public class Uniqueor extends AbstractUtil<Uniqueor> {

    private Uniqueor() {
    }

    public static Uniqueor create() {
        return new Uniqueor();
    }

    public <ID> Record fetchById(final ID id) {
        Ao.infoSQL(this.getLogger(), "执行方法：Uniqueor.fetchById, {0}", id);

        return Jq.outR(this.idInput(id), this.jooq::fetchById);
    }

    public Record fetchOne(final Criteria criteria) {
        Ao.infoSQL(this.getLogger(), "执行方法：Uniqueor.fetchOne");
        return Jq.outR(this.irCond(criteria), this.jooq::fetchOne);
    }

    // ----------------------- Async ----------------------
    public <ID> Future<Record> fetchByIdAsync(final ID id) {
        Ao.infoSQL(this.getLogger(), "执行方法：Uniqueor.fetchByIdAsync, {0}", id);
        return Jq.outRAsync(this.idInput(id), this.jooq::fetchById);
    }

    public Future<Record> fetchOneAsync(final Criteria criteria) {
        Ao.infoSQL(this.getLogger(), "执行方法：Uniqueor.fetchOneAsync");
        return Jq.outRAsync(this.irCond(criteria), this.jooq::fetchOne);
    }
}
