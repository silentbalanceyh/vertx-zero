package io.vertx.tp.modular.io;

import io.vertx.tp.atom.modeling.element.DataRow;
import io.vertx.up.commune.Record;

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
    public AoIo records(final Record... records) {
        return this.saveRows(() -> {
            final List<DataRow> rows = new ArrayList<>();
            Arrays.stream(records)
                    .map(record -> this.newRow().setData(record))
                    .forEach(rows::add);
            return rows;
        });
    }
}
