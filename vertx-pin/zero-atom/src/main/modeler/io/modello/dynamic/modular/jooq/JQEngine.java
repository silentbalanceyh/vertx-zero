package io.modello.dynamic.modular.jooq;

import io.horizon.uca.cache.Cc;
import io.modello.dynamic.modular.metadata.AoSentence;
import io.vertx.mod.atom.modeling.data.DataEvent;
import io.vertx.mod.atom.refine.Ao;
import org.jooq.DSLContext;

/**
 * 使用Jooq处理
 */
public class JQEngine {
    private final static Cc<String, JQEngine> CC_ENGINE = Cc.openThread();
    private final transient JQInsert insertT;
    private final transient JQDelete deleteT;
    private final transient JQQuery queryT;
    private final transient JQUpdate updateT;
    private final transient JQAggregate aggrT;
    private final transient JQRead readT;

    private JQEngine(final DSLContext context) {
        // 设置数据库配置, SQL 调试
        final boolean isSql = Ao.isDebug();
        if (isSql) {
            context.settings().setDebugInfoOnStackTrace(Boolean.TRUE);
        }
        /* Insert, Delete, Update, Read */
        this.insertT = new JQInsert(context);
        this.deleteT = new JQDelete(context);
        this.updateT = new JQUpdate(context);
        this.readT = new JQRead(context);

        /* Aggregate, Query */
        this.aggrT = new JQAggregate(context);
        this.queryT = new JQQuery(context);
    }

    public static JQEngine create(final DSLContext context) {
        return CC_ENGINE.pick(() -> new JQEngine(context), String.valueOf(context.hashCode()));
        // return Fn.po?lThread(AoCache.POOL_ENGINES, () -> new JQEngine(context), String.valueOf(context.hashCode()));
    }

    public JQEngine bind(final AoSentence sentence) {
        /* JqTool */
        this.queryT.bind(sentence);
        return this;
    }

    // UPDATE
    public DataEvent update(final DataEvent event) {
        return this.updateT.update(event);
    }

    public DataEvent updateBatch(final DataEvent event) {
        return this.updateT.updateBatch(event);
    }

    // INSERT
    public DataEvent insert(final DataEvent event) {
        return this.insertT.insert(event);
    }

    public DataEvent insertBatch(final DataEvent events) {
        return this.insertT.insertBatch(events);
    }

    // SELECT
    public DataEvent fetchByIds(final DataEvent events) {
        return this.readT.fetchByIds(events);
    }

    public DataEvent fetchById(final DataEvent event) {
        return this.readT.fetchById(event);
    }


    // SELECT Query
    public DataEvent fetchOne(final DataEvent event) {
        return this.queryT.fetchOne(event);
    }

    public DataEvent fetchAll(final DataEvent event) {
        return this.queryT.fetchAll(event);
    }

    public DataEvent search(final DataEvent event) {
        return this.queryT.search(event);
    }

    public DataEvent query(final DataEvent event) {
        return this.queryT.query(event);
    }

    // DELETE
    public DataEvent delete(final DataEvent event) {
        return this.deleteT.delete(event);
    }

    public DataEvent deleteBatch(final DataEvent event) {
        return this.deleteT.deleteBatch(event);
    }


    public DataEvent count(final DataEvent event) {
        return this.aggrT.count(event);
    }
}
