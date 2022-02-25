package io.vertx.tp.modular.dao.internal;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.Record;
import io.vertx.up.util.Ut;

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
        Ao.infoSQL(this.getLogger(), Ut.notNil(qr), "执行方法：USearch.search: {0}", criteria.encode());
        // Input
        final DataEvent input = this.irQr(criteria);
        // Output
        final DataEvent output = this.jooq.search(input);
        return output.dataP();
    }

    public Record[] query(final JsonObject qr) {
        final JsonObject criteria = Ut.valueJObject(qr);
        Ao.infoSQL(this.getLogger(), Ut.notNil(qr), "执行方法：USearch.query: {0}", criteria.encode());
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
        final Qr qr = Qr.create(criteria);
        return this.event().qr(qr);
    }
}
