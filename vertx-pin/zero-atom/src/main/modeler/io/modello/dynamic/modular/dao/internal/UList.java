package io.modello.dynamic.modular.dao.internal;

import io.modello.specification.HRecord;
import io.vertx.mod.atom.modeling.data.DataEvent;

import static io.vertx.mod.atom.refine.Ao.LOG;

/**
 * 工具类
 * 1. 只支持集合结果，包括分页专用结果
 * 2. 支持 SELECT 返回搜索结果
 * 3. 连接查询引擎做细粒度查询
 * 4. 返回结果必须是固定格式：
 * [] 数组格式
 */
public class UList extends AbstractUtil<UList> {
    private UList() {
    }

    public static UList create() {
        return new UList();
    }

    @SuppressWarnings("unchecked")
    public <ID> HRecord[] fetchByIds(final ID... ids) {
        LOG.SQL.info(this.getLogger(), "执行方法：UList.fetchByIds");
        // Input
        final DataEvent input = this.irIDs(ids);
        // Output
        return this.output(input, this.jooq::fetchByIds, true);
    }

    public HRecord[] fetchAll() {
        LOG.SQL.info(this.getLogger(), "执行方法：UList.fetchAll");
        // Input
        final DataEvent input = this.events();
        // Output
        return this.output(input, this.jooq::fetchAll, true);
    }
}
