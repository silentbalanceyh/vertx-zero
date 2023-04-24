package io.vertx.tp.modular.io;

import io.horizon.specification.modeler.HRecord;
import io.vertx.tp.atom.modeling.element.DataRow;
import io.vertx.tp.modular.plugin.IoHub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IoBatch extends AbstractIo {
    @Override
    @SafeVarargs
    public final <ID> AoIo keys(final ID... keys) {
        return this.saveRows(() -> {
            final List<DataRow> rows = new ArrayList<>();
            Arrays.stream(keys)
                .map(key -> this.newRow().setKey(key))
                .forEach(rows::add);
            return rows;
        });
    }

    @Override
    public AoIo records(final HRecord... records) {
        final IoHub hub = IoHub.instance();
        final HRecord[] processed = hub.in(records, this.tpl());

        return this.saveRows(() -> {
            final List<DataRow> rows = new ArrayList<>();
            Arrays.stream(processed)
                .map(record -> this.newRow().request(record))
                .forEach(rows::add);
            return rows;
        });
    }
}
