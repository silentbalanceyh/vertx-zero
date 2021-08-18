package io.vertx.tp.modular.jooq;

import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.metadata.AoSentence;
import io.vertx.up.fn.Fn;
import org.jooq.DSLContext;

/**
 * 使用Jooq处理
 */
public class JQEngine {
    private final transient JQInsert insertT;
    private final transient JQDelete deleteT;
    private final transient JQQuery queryT;
    private final transient JQUpdate updateT;
    private final transient JQAggregate aggrT;

    private JQEngine(final DSLContext context) {
        // 设置数据库配置, SQL 调试
        final boolean isSql = Ao.isDebug();
        if (isSql) {
            context.settings().setDebugInfoOnStackTrace(Boolean.TRUE);
        }
        /* Insert */
        this.insertT = new JQInsert(context);
        /* Delete */
        this.deleteT = new JQDelete(context);
        /* JqTool */
        this.queryT = new JQQuery(context);
        /* Update */
        this.updateT = new JQUpdate(context);
        /* Aggregate */
        this.aggrT = new JQAggregate(context);
    }

    public static JQEngine create(final DSLContext context) {
        return Fn.poolThread(AoCache.POOL_ENGINES, () -> new JQEngine(context), String.valueOf(context.hashCode()));
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

    public DataEvent insertAsync(final DataEvent event) {
        return null;
    }

    public DataEvent insertBatch(final DataEvent events) {
        return this.insertT.insertBatch(events);
    }

    // SELECT
    public DataEvent fetchByIds(final DataEvent events) {
        return this.queryT.fetchByIds(events);
    }

    public DataEvent fetchById(final DataEvent event) {
        return this.queryT.fetchById(event);
    }

    public DataEvent fetchOne(final DataEvent event) {
        return this.queryT.fetchOne(event);
    }

    public DataEvent fetchAll(final DataEvent event) {
        return this.queryT.fetchAll(event);
    }

    // SEARCH
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

    // COUNT
    public DataEvent count(final DataEvent event) {
        return this.aggrT.count(event);
    }
}
