package io.modello.dynamic.modular.id;

import cn.vertxup.atom.domain.tables.pojos.MJoin;
import io.modello.specification.HRecord;
import io.vertx.mod.atom.modeling.element.DataMatrix;
import io.vertx.mod.atom.refine.Ao;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

abstract class AbstractId implements AoId {

    @Override
    public void connect(final HRecord record,
                        final ConcurrentMap<String, DataMatrix> keys,
                        final ConcurrentMap<String, DataMatrix> matrix,
                        final Set<MJoin> joins) {
        /* 设置主键 */
        Ao.connect(record, keys, matrix, joins.stream()
            .map(MJoin::getEntityKey).collect(Collectors.toSet()));
    }
}
