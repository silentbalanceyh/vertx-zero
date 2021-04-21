package io.vertx.tp.modular.plugin;

import io.vertx.tp.atom.modeling.element.DataTpl;
import io.vertx.tp.modular.reference.AoRay;
import io.vertx.tp.modular.reference.RayBatch;
import io.vertx.tp.modular.reference.RaySingle;
import io.vertx.up.commune.Record;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IoNerve implements IoHub {
    @Override
    public Record in(final Record record, final DataTpl tpl) {
        return record;
    }

    @Override
    public Record[] in(final Record[] records, final DataTpl tpl) {
        return records;
    }

    @Override
    public Record out(final Record record, final DataTpl tpl) {
        /*
         * Reference
         */
        final AoRay<Record> ray = new RaySingle().on(tpl);
        return ray.attach(record);
    }

    @Override
    public Record[] out(final Record[] records, final DataTpl tpl) {
        /*
         * Reference
         */
        final AoRay<Record[]> ray = new RayBatch().on(tpl);
        return ray.attach(records);
    }
}
