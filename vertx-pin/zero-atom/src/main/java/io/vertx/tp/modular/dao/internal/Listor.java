package io.vertx.tp.modular.dao.internal;

import io.vertx.core.Future;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.up.commune.Record;

/**
 * 工具类
 * 1. 只支持集合结果，包括分页专用结果
 * 2. 支持 SELECT 返回搜索结果
 * 3. 连接查询引擎做细粒度查询
 * 4. 返回结果必须是固定格式：
 * [] 数组格式
 */
public class Listor extends AbstractUtil<Listor> {
    private Listor() {
    }

    public static Listor create() {
        return new Listor();
    }

    @SuppressWarnings("unchecked")
    public <ID> Record[] fetchByIds(final ID... ids) {
        Ao.infoSQL(this.getLogger(), "执行方法：Listor.fetchByIds");
        return Jq.outRecords(this.idInputs(ids), this.jooq::fetchByIds);
    }

    public Record[] fetchAll() {
        Ao.infoSQL(this.getLogger(), "执行方法：Listor.fetchAll");
        return Jq.outRecords(this.events(), this.jooq::fetchAll);
    }

    // ----------------------- Async ----------------------
    @SafeVarargs
    public final <ID> Future<Record[]> fetchByIdsAsync(final ID... ids) {
        Ao.infoSQL(this.getLogger(), "执行方法：Listor.fetchByIdsAsync");
        return Jq.outRecordsAsync(this.idInputs(ids), this.jooq::fetchByIds);
    }

    public Future<Record[]> fetchAllAsync() {
        Ao.infoSQL(this.getLogger(), "执行方法：Listor.fetchAllAsync");
        return Jq.outRecordsAsync(this.events(), this.jooq::fetchAll);
    }
}
