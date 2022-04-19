package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.unity.Ux;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Divert {

    Divert bind(ConcurrentMap<String, WMove> moveMap);

    /*
     *  Event Fire by Programming
     */
    default Future<WRecord> transferAsync(final WRequest request, final WProcess process) {
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }

    default Future<WProcess> moveAsync(final WRequest request, final WProcess process) {
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }
}
