package io.vertx.tp.modular.plugin;

import io.vertx.tp.atom.modeling.element.DataTpl;
import io.vertx.tp.modular.reference.AoRay;
import io.vertx.tp.modular.reference.RayBatch;
import io.vertx.tp.modular.reference.RaySingle;
import io.vertx.up.commune.Record;
import io.vertx.up.commune.element.JComponent;
import io.vertx.up.fn.Fn;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
    private static final ConcurrentMap<String, AoRay<Record>> POOL_RAY = new ConcurrentHashMap<>();
    /**
     * The Pool of Tpl definition of `Ray` ( Batch )
     *
     * 1. The key is `identifier`.
     * 2. The value is `AoRay` reference with content `Records`.
     */
    private final ConcurrentMap<String, AoRay<Record[]>> POOL_RAY_BATCH = new ConcurrentHashMap<>();

    @Override
    public Record in(final Record record, final DataTpl tpl) {
        /*
         * inComponent
         */
        final ConcurrentMap<String, JComponent> inComponent = IoArranger.pluginIn(tpl);
        
        return record;
    }

    @Override
    public Record[] in(final Record[] records, final DataTpl tpl) {

        return records;
    }

    @Override
    public Record out(final Record record, final DataTpl tpl) {
        if (Objects.isNull(record))/* Null Record */ return null;

        /* Reference */
        final AoRay<Record> ray = Fn.pool(POOL_RAY, tpl.identifier(), () -> new RaySingle().on(tpl));
        return ray.attach(record);
    }

    @Override
    public Record[] out(final Record[] records, final DataTpl tpl) {
        /* Reference */
        final AoRay<Record[]> ray = Fn.pool(this.POOL_RAY_BATCH, tpl.identifier(), () -> new RayBatch().on(tpl));
        return ray.attach(records);
    }
}

