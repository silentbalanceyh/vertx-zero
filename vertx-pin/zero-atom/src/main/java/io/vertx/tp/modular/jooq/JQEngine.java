package io.vertx.tp.modular.jooq;

import io.vertx.core.Future;
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

    // -------------------- Async -------------------------

    // UPDATE
    public Future<DataEvent> updateAsync(final DataEvent event) {
        return this.updateT.updateAsync(event);
    }

    public Future<DataEvent> updateBatchAsync(final DataEvent event) {
        return this.updateT.updateBatchAsync(event);
    }

    // INSERT
    public Future<DataEvent> insertAsync(final DataEvent event) {
        return this.insertT.insertAsync(event);
    }

    public Future<DataEvent> insertBatchAsync(final DataEvent event) {
        return this.insertT.insertBatchAsync(event);
    }

    // SELECT
    public Future<DataEvent> fetchByIdsAsync(final DataEvent event) {
        return this.readT.fetchByIdsAsync(event);
    }

    public Future<DataEvent> fetchByIdAsync(final DataEvent event) {
        return this.readT.fetchByIdAsync(event);
    }

    // SELECT Query

    public Future<DataEvent> fetchOneAsync(final DataEvent event) {
        return this.queryT.fetchOneAsync(event);
    }

    public Future<DataEvent> fetchAllAsync(final DataEvent event) {
        return this.queryT.fetchAllAsync(event);
    }

    public Future<DataEvent> searchAsync(final DataEvent event) {
        return this.queryT.searchAsync(event);
    }

    public Future<DataEvent> queryAsync(final DataEvent event) {
        return this.queryT.queryAsync(event);
    }

    // DELETE
    public Future<DataEvent> deleteAsync(final DataEvent event) {
        return this.deleteT.deleteAsync(event);
    }

    public Future<DataEvent> deleteBatchAsync(final DataEvent event) {
        return this.deleteT.deleteBatchAsync(event);
    }

    // COUNT
    public Future<DataEvent> countAsync(final DataEvent event) {
        return this.aggrT.countAsync(event);
    }

    public DataEvent count(final DataEvent event) {
        return this.aggrT.count(event);
    }
}
