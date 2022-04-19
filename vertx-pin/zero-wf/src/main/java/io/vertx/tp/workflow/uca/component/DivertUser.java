package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.atom.WRequest;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DivertUser extends AbstractDivert {
    @Override
    public Future<WRecord> goOnAsync(final WRequest request, final WProcess process) {
        return null;
    }
}
