package io.vertx.tp.modular.dao.internal;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.jooq.internal.Jq;
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
public class Searchor extends AbstractUtil<Searchor> {

    private Searchor() {
    }

    public static Searchor create() {
        return new Searchor();
    }

    public JsonObject search(final JsonObject qr) {
        final JsonObject criteria = Ut.sureJObject(qr);
        Ao.infoSQL(this.getLogger(), Ut.notNil(qr), "执行方法：Searcher.search: {0}", criteria.encode());
        return Jq.outP(this.irQr(Qr.create(criteria)), this.jooq::search);
    }

    public Record[] query(final JsonObject qr) {
        final JsonObject criteria = Ut.sureJObject(qr);
        Ao.infoSQL(this.getLogger(), Ut.notNil(qr), "执行方法：Searcher.query: {0}", criteria.encode());
        return Jq.outRs(this.irCond(Criteria.create(criteria)), this.jooq::query);
    }

    // ----------------------- Async ----------------------
    public Future<JsonObject> searchAsync(final JsonObject qr) {
        final JsonObject criteria = Ut.sureJObject(qr);
        Ao.infoSQL(this.getLogger(), Ut.notNil(qr), "执行方法：Searcher.searchAsync: {0}", criteria.encode());
        return Jq.outPAsync(this.irQr(Qr.create(criteria)), this.jooq::search);
    }

    public Future<Record[]> queryAsync(final JsonObject qr) {
        final JsonObject criteria = Ut.sureJObject(qr);
        Ao.infoSQL(this.getLogger(), Ut.notNil(qr), "执行方法：Searcher.queryAsync: {0}", criteria.encode());
        return Jq.outRsAsync(this.irCond(Criteria.create(criteria)), this.jooq::query);
    }
}
