package io.modello.dynamic.modular.id;

import io.modello.specification.HRecord;
import io.vertx.mod.atom.modeling.Model;

class CollectionId extends AbstractId {

    @Override
    public <ID> ID key(final HRecord record,
                       final Model model) {

        return null;
    }

    @Override
    public <ID> void key(final HRecord record,
                         final Model model,
                         final ID id) {

    }
}
