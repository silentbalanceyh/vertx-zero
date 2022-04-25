package io.vertx.tp.modular.plugin;

import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.element.DataTpl;
import io.vertx.tp.modular.reference.AoRay;
import io.vertx.tp.modular.reference.RayBatch;
import io.vertx.tp.modular.reference.RaySingle;
import io.vertx.up.commune.Record;
import io.vertx.up.uca.cache.Cc;

import java.util.Objects;

/**
 * ## Workflow
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IoNerve implements IoHub {
    /**
     * The Pool of Tpl definition of `Ray`
     *
     * 1. The key is `identifier`.
     * 2. The value is `AoRay` reference with content `Record`.
     */
    private static final Cc<String, AoRay<Record>> CC_RAY = Cc.open();
    private static final Cc<String, AoRay<Record>> CC_RAY_ASYNC = Cc.open();
    /**
     * The Pool of Tpl definition of `Ray` ( Batch )
     *
     * 1. The key is `identifier`.
     * 2. The value is `AoRay` reference with content `Records`.
     */
    private static final Cc<String, AoRay<Record[]>> CC_RAY_BATCH = Cc.open();
    private static final Cc<String, AoRay<Record[]>> CC_RAY_BATCH_ASYNC = Cc.open();

    @Override
    public Record in(final Record record, final DataTpl tpl) {
        /* inComponent Before */
        IoArranger.runIn(record, IoArranger.pluginInBefore(tpl));
        /* inComponent */
        IoArranger.runIn(record, IoArranger.pluginIn(tpl));
        /* normalizer */
        IoArranger.runNorm(record, IoArranger.pluginNormalize(tpl));
        /* inComponent After */
        IoArranger.runIn(record, IoArranger.pluginInAfter(tpl));
        return record;
    }

    @Override
    public Record[] in(final Record[] records, final DataTpl tpl) {
        /* inComponent Before */
        IoArranger.runIn(records, IoArranger.pluginInBefore(tpl));
        /* inComponent */
        IoArranger.runIn(records, IoArranger.pluginIn(tpl));
        /* normalizer */
        IoArranger.runNorm(records, IoArranger.pluginNormalize(tpl));
        /* inComponent After */
        IoArranger.runIn(records, IoArranger.pluginInAfter(tpl));
        return records;
    }

    @Override
    public Record out(final Record record, final DataTpl tpl) {
        if (Objects.isNull(record))/* Null Record */ {
            return null;
        }
        this.runOut(record, tpl);
        /* reference */
        final AoRay<Record> ray = CC_RAY.pick(() -> new RaySingle().on(tpl), tpl.identifier());
        // Fn.po?l(POOL_RAY, tpl.identifier(), () -> new RaySingle().on(tpl));
        return ray.doRay(record);
    }

    @Override
    public Record[] out(final Record[] records, final DataTpl tpl) {
        this.runOut(records, tpl);
        /* reference */
        final AoRay<Record[]> ray = CC_RAY_BATCH.pick(() -> new RayBatch().on(tpl), tpl.identifier());
        // Fn.po?l(this.POOL_RAY_BATCH, tpl.identifier(), () -> new RayBatch().on(tpl));
        return ray.doRay(records);
    }

    @Override
    public Future<Record> outAsync(final Record record, final DataTpl tpl) {
        if (Objects.isNull(record))/* Null Record */ {
            return null;
        }
        this.runOut(record, tpl);
        /* reference */
        final AoRay<Record> ray = CC_RAY_ASYNC.pick(() -> new RaySingle().on(tpl), tpl.identifier());
        // Fn.po?l(POOL_RAY_ASYNC, tpl.identifier(), () -> new RaySingle().on(tpl));
        return ray.doRayAsync(record);
    }

    @Override
    public Future<Record[]> outAsync(final Record[] records, final DataTpl tpl) {
        this.runOut(records, tpl);
        /* reference */
        final AoRay<Record[]> ray = CC_RAY_BATCH_ASYNC.pick(() -> new RayBatch().on(tpl), tpl.identifier());
        // Fn.po?l(this.POOL_RAY_BATCH_ASYNC, tpl.identifier(), () -> new RayBatch().on(tpl));
        return ray.doRayAsync(records);
    }

    private void runOut(final Record record, final DataTpl tpl) {
        /* outComponent ( Before ) */
        IoArranger.runOut(record, IoArranger.pluginOutBefore(tpl));
        /* outComponent */
        IoArranger.runOut(record, IoArranger.pluginOut(tpl));
        /* expression */
        IoArranger.runExpr(record, IoArranger.pluginExpression(tpl));
        /* outComponent ( After ) */
        IoArranger.runOut(record, IoArranger.pluginOutAfter(tpl));
    }

    private void runOut(final Record[] records, final DataTpl tpl) {
        /* outComponent ( Before ) */
        IoArranger.runOut(records, IoArranger.pluginOutBefore(tpl));
        /* outComponent */
        IoArranger.runOut(records, IoArranger.pluginOut(tpl));
        /* expression */
        IoArranger.runExpr(records, IoArranger.pluginExpression(tpl));
        /* outComponent ( After ) */
        IoArranger.runOut(records, IoArranger.pluginOutAfter(tpl));
    }
}

