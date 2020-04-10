package io.vertx.tp.modular.id;

import io.vertx.tp.atom.modeling.Model;
import io.vertx.up.commune.Record;

class CollectionId extends AbstractId {

    @Override
    public <ID> ID key(final Record record,
                       final Model model) {

        return null;
    }

    @Override
    public <ID> void key(final Record record,
                         final Model model,
                         final ID id) {

    }
}
