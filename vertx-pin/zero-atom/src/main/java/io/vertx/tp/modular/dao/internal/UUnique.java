package io.vertx.tp.modular.dao.internal;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.refine.Ao;
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
public class UUnique extends AbstractUtil<UUnique> {

    private UUnique() {
    }

    public static UUnique create() {
        return new UUnique();
    }

    public <ID> Record fetchById(final ID id) {
        Ao.infoSQL(this.getLogger(), "执行方法：UUnique.fetchById, {0}", id);
        // Input
        final DataEvent input = this.idInput(id);
        // Output
        return this.output(input, this.jooq::fetchById, false);
    }

    public Record fetchOne(final Criteria criteria) {
        Ao.infoSQL(this.getLogger(), "执行方法：UUnique.fetchOne");
        // Input
        final DataEvent input = this.irCond(criteria);
        // Output
        return this.output(input, this.jooq::fetchOne, false);
    }

    // ----------------------- Private ----------------------
    /*
     * 起点：仅生成绑定了 ids 的 DataEvent
     */
    private <ID> DataEvent idInput(final ID ids) {
        return this.event().keys(ids);
    }
}
