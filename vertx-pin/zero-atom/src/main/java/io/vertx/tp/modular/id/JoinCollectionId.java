package io.vertx.tp.modular.id;

import io.modello.specification.HRecord;
import io.vertx.tp.atom.modeling.Model;

class JoinCollectionId extends AbstractId {

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
