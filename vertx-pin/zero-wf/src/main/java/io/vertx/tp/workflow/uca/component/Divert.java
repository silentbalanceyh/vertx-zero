package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.tp.workflow.atom.WRequest;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Divert {

    Divert bind(ConcurrentMap<String, WMove> moveMap);

    /*
     *  Event Fire by Programming
     */
    Future<WRecord> goOnAsync(WRequest request, WProcess process);
}
