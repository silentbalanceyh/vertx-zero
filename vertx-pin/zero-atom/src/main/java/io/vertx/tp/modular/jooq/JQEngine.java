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
    private final transient JQInsert insertTool;
    private final transient JQDelete deleteTool;
    private final transient JQQuery queryTool;
    private final transient JQUpdate updateTool;
    private final transient JQAggregate aggregateTool;

    private JQEngine(final DSLContext context) {
        // 设置数据库配置
        this.initSetting(context);
        /* Insert */
        this.insertTool = new JQInsert(context);
        /* Delete */
        this.deleteTool = new JQDelete(context);
        /* JqTool */
        this.queryTool = new JQQuery(context);
        /* Update */
        this.updateTool = new JQUpdate(context);
        /* Aggregate */
        this.aggregateTool = new JQAggregate(context);
    }

    public static JQEngine create(final DSLContext context) {
        return Fn.pool(AoCache.POOL_ENGINES, context.hashCode(), () -> new JQEngine(context));
    }

    public JQEngine bind(final AoSentence sentence) {
        /* JqTool */
        this.queryTool.bind(sentence);
        return this;
    }

    private void initSetting(final DSLContext context) {
        // SQL 调试
        final boolean isSql = Ao.isDebug();
        if (isSql) {
            context.settings().setDebugInfoOnStackTrace(Boolean.TRUE);
        }
    }

    // UPDATE
    public DataEvent update(final DataEvent event) {
        return this.updateTool.update(event);
    }

    public DataEvent updateBatch(final DataEvent event) {
        return this.updateTool.updateBatch(event);
    }

    // INSERT
    public DataEvent insert(final DataEvent event) {
        return this.insertTool.insert(event);
    }

    public DataEvent insertBatch(final DataEvent events) {
        return this.insertTool.insertBatch(events);
    }

    // SELECT
    public DataEvent fetchByIds(final DataEvent events) {
        return this.queryTool.fetchByIds(events);
    }

    public DataEvent fetchById(final DataEvent event) {
        return this.queryTool.fetchById(event);
    }

    public DataEvent fetchOne(final DataEvent event) {
        return this.queryTool.fetchOne(event);
    }

    public DataEvent fetchAll(final DataEvent event) {
        return this.queryTool.fetchAll(event);
    }

    // SEARCH
    public DataEvent search(final DataEvent event) {
        return this.queryTool.search(event);
    }

    public DataEvent query(final DataEvent event) {
        return this.queryTool.query(event);
    }

    // DELETE
    public DataEvent delete(final DataEvent event) {
        return this.deleteTool.delete(event);
    }

    public DataEvent deleteBatch(final DataEvent event) {
        return this.deleteTool.deleteBatch(event);
    }

    // COUNT
    public DataEvent count(final DataEvent event) {
        return this.aggregateTool.count(event);
    }
}
