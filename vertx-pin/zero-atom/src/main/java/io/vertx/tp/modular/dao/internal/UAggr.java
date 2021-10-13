package io.vertx.tp.modular.dao.internal;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.atom.query.Criteria;

/**
 * 工具类
 * 1. 聚集函数处理
 * 2. 复杂的分组统计类
 */
public class UAggr extends AbstractUtil<UAggr> {

    private UAggr() {
    }

    public static UAggr create() {
        return new UAggr();
    }

    public Long count(final Criteria criteria) {
        Ao.infoSQL(this.getLogger(), "执行方法：UAggr.count");
        final Long counter = this.countSInternal(criteria);
        Ao.infoSQL(this.getLogger(), "结果集：{0}", counter);
        return counter;
    }

    public Boolean existing(final Criteria criteria) {
        Ao.infoSQL(this.getLogger(), "执行方法：UAggr.existing");
        final Long counter = this.countSInternal(criteria);
        Ao.infoSQL(this.getLogger(), "结果集：{0}", counter);
        return 0 < counter; // 存在就 true， 不存在 false
    }

    public Boolean missing(final Criteria criteria) {
        Ao.infoSQL(this.getLogger(), "执行方法：UAggr.missing");
        final Long counter = this.countSInternal(criteria);
        Ao.infoSQL(this.getLogger(), "结果集：{0}", counter);
        return 0 == counter; // 不存在 true，存在 false
    }

    private Long countSInternal(final Criteria criteria) {
        // Input
        final DataEvent input = this.irCond(criteria);
        // Output
        final DataEvent output = this.jooq.count(input);
        // 计数器
        return output.getCounter();
    }
}
