package io.vertx.tp.modular.io;

import io.vertx.tp.error._417EventTypeConflictException;
import io.vertx.tp.modular.plugin.IoHub;
import io.vertx.up.commune.Record;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;

public class IoSingle extends AbstractIo {

    private void ensure(final Integer length) {
        Fn.outWeb(1 < length, _417EventTypeConflictException.class, this.getClass());
    }

    @Override
    @SafeVarargs
    public final <ID> AoIo keys(final ID... keys) {
        /* keys长度 */
        this.ensure(keys.length);

        return this.saveRow(() -> this.newRow().setKey(keys[Values.IDX]));
    }

    @Override
    public AoIo records(final Record... records) {
        /* records长度 */
        this.ensure(records.length);
        /* Record */
        final Record record = records[Values.IDX];
        final IoHub hub = IoHub.instance();
        final Record processed = hub.in(record, this.tpl());
        return this.saveRow(() -> this.newRow().request(processed));
    }
}
