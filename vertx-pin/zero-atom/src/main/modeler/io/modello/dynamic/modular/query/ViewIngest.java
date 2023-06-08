package io.modello.dynamic.modular.query;

import io.horizon.uca.qr.Criteria;
import io.vertx.mod.atom.modeling.element.DataTpl;
import org.jooq.Condition;

class ViewIngest implements Ingest {
    @Override
    public Condition onCondition(final DataTpl tpl,
                                 final Criteria criteria) {
        return null;
    }
}
