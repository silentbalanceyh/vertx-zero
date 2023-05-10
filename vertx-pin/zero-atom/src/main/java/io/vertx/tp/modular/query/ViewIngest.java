package io.vertx.tp.modular.query;

import io.horizon.uca.qr.Criteria;
import io.vertx.tp.atom.modeling.element.DataTpl;
import org.jooq.Condition;

class ViewIngest implements Ingest {
    @Override
    public Condition onCondition(final DataTpl tpl,
                                 final Criteria criteria) {
        return null;
    }
}
