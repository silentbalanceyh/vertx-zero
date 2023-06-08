package io.modello.dynamic.modular.dao.internal;

import io.horizon.uca.qr.Criteria;
import io.horizon.uca.qr.syntax.Ir;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.data.DataEvent;
import io.vertx.up.util.Ut;

import static io.vertx.mod.atom.refine.Ao.LOG;

/**
 * 工具类
 * 1. 只支持集合结果，包括分页专用结果
 * 2. 支持 SELECT 返回搜索结果
 * 3. 连接查询引擎做细粒度查询
 * 4. 返回结果必须是固定格式：
 * {
 * * "count":XX
 * * "data":[
 * * ]
 * }
 */
public class USearch extends AbstractUtil<USearch> {

    private USearch() {
    }

    public static USearch create() {
        return new USearch();
    }

    public JsonObject search(final JsonObject qr) {
        final JsonObject criteria = Ut.valueJObject(qr);
        LOG.SQL.info(Ut.isNotNil(qr), this.getLogger(), "执行方法：USearch.search: {0}", criteria.encode());
        // Input
        final DataEvent input = this.irQr(criteria);
        // Output
        final DataEvent output = this.jooq.search(input);
        return output.dataP();
    }

    public HRecord[] query(final JsonObject qr) {
        final JsonObject criteria = Ut.valueJObject(qr);
        LOG.SQL.info(Ut.isNotNil(qr), this.getLogger(), "执行方法：USearch.query: {0}", criteria.encode());
        // Input
        final DataEvent input = this.irCond(Criteria.create(criteria));
        // Output
        return this.output(input, this.jooq::query, true);
    }

    // ----------------------- Private ----------------------
    /*
     * 构造JsonObject中的 criteria
     */
    private DataEvent irQr(final JsonObject criteria) {
        final Ir qr = Ir.create(criteria);
        return this.event().qr(qr);
    }
}
