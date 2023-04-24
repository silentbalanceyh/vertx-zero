package io.vertx.tp.modular.id;

import io.horizon.specification.modeler.Record;
import io.vertx.tp.atom.modeling.Model;

class JoinCollectionId extends AbstractId {

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
