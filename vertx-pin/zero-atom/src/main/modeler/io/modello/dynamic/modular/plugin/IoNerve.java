package io.modello.dynamic.modular.plugin;

import io.horizon.uca.cache.Cc;
import io.modello.dynamic.modular.reference.AoRay;
import io.modello.dynamic.modular.reference.RayBatch;
import io.modello.dynamic.modular.reference.RaySingle;
import io.modello.specification.HRecord;
import io.vertx.core.Future;
import io.vertx.mod.atom.modeling.element.DataTpl;

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
    private static final Cc<String, AoRay<HRecord>> CC_RAY = Cc.open();
    private static final Cc<String, AoRay<HRecord>> CC_RAY_ASYNC = Cc.open();
    /**
     * The Pool of Tpl definition of `Ray` ( Batch )
     *
     * 1. The key is `identifier`.
     * 2. The value is `AoRay` reference with content `Records`.
     */
    private static final Cc<String, AoRay<HRecord[]>> CC_RAY_BATCH = Cc.open();
    private static final Cc<String, AoRay<HRecord[]>> CC_RAY_BATCH_ASYNC = Cc.open();

    @Override
    public HRecord in(final HRecord record, final DataTpl tpl) {
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
    public HRecord[] in(final HRecord[] records, final DataTpl tpl) {
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
    public HRecord out(final HRecord record, final DataTpl tpl) {
        if (Objects.isNull(record))/* Null Record */ {
            return null;
        }
        this.runOut(record, tpl);
        /* reference */
        final AoRay<HRecord> ray = CC_RAY.pick(() -> new RaySingle().on(tpl), tpl.identifier());
        // Fn.po?l(POOL_RAY, tpl.identifier(), () -> new RaySingle().on(tpl));
        return ray.doRay(record);
    }

    @Override
    public HRecord[] out(final HRecord[] records, final DataTpl tpl) {
        this.runOut(records, tpl);
        /* reference */
        final AoRay<HRecord[]> ray = CC_RAY_BATCH.pick(() -> new RayBatch().on(tpl), tpl.identifier());
        // Fn.po?l(this.POOL_RAY_BATCH, tpl.identifier(), () -> new RayBatch().on(tpl));
        return ray.doRay(records);
    }

    @Override
    public Future<HRecord> outAsync(final HRecord record, final DataTpl tpl) {
        if (Objects.isNull(record))/* Null Record */ {
            return null;
        }
        this.runOut(record, tpl);
        /* reference */
        final AoRay<HRecord> ray = CC_RAY_ASYNC.pick(() -> new RaySingle().on(tpl), tpl.identifier());
        // Fn.po?l(POOL_RAY_ASYNC, tpl.identifier(), () -> new RaySingle().on(tpl));
        return ray.doRayAsync(record);
    }

    @Override
    public Future<HRecord[]> outAsync(final HRecord[] records, final DataTpl tpl) {
        this.runOut(records, tpl);
        /* reference */
        final AoRay<HRecord[]> ray = CC_RAY_BATCH_ASYNC.pick(() -> new RayBatch().on(tpl), tpl.identifier());
        // Fn.po?l(this.POOL_RAY_BATCH_ASYNC, tpl.identifier(), () -> new RayBatch().on(tpl));
        return ray.doRayAsync(records);
    }

    private void runOut(final HRecord record, final DataTpl tpl) {
        /* outComponent ( Before ) */
        IoArranger.runOut(record, IoArranger.pluginOutBefore(tpl));
        /* outComponent */
        IoArranger.runOut(record, IoArranger.pluginOut(tpl));
        /* expression */
        IoArranger.runExpr(record, IoArranger.pluginExpression(tpl));
        /* outComponent ( After ) */
        IoArranger.runOut(record, IoArranger.pluginOutAfter(tpl));
    }

    private void runOut(final HRecord[] records, final DataTpl tpl) {
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

