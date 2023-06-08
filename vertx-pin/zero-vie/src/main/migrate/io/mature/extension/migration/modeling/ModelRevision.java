package io.mature.extension.migration.modeling;

import cn.vertxup.atom.domain.tables.pojos.MModel;

public class ModelRevision extends AbstractRevision {

    public ModelRevision() {
        super(MModel.class);
    }
}
