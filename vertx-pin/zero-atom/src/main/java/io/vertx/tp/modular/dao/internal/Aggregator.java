package io.vertx.tp.modular.dao.internal;

import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.up.atom.query.Criteria;

/**
 * 工具类
 * 1. 聚集函数处理
 * 2. 复杂的分组统计类
 */
public class Aggregator extends AbstractUtil<Aggregator> {

    private Aggregator() {
    }

    public static Aggregator create() {
        return new Aggregator();
    }

    public Long count(final Criteria criteria) {
        Ao.infoSQL(this.getLogger(), "执行方法：Aggregator.count");
        final Long counter = Jq.onCount(this.irCond(criteria), this.jooq::count);
        Ao.infoSQL(this.getLogger(), "结果集：{0}", counter);
        return counter;
    }

    public Boolean existing(final Criteria criteria) {
        Ao.infoSQL(this.getLogger(), "执行方法：Aggregator.existing");
        final Long counter = Jq.onCount(this.irCond(criteria), this.jooq::count);
        Ao.infoSQL(this.getLogger(), "结果集：{0}", counter);
        return 0 < counter; // 存在就 true， 不存在 false
    }

    public Boolean missing(final Criteria criteria) {
        Ao.infoSQL(this.getLogger(), "执行方法：Aggregator.missing");
        final Long counter = Jq.onCount(this.irCond(criteria), this.jooq::count);
        Ao.infoSQL(this.getLogger(), "结果集：{0}", counter);
        return 0 == counter; // 不存在 true，存在 false
    }
}
