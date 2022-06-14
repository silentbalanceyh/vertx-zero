package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.tp.workflow.uca.central.Behaviour;

/**
 * Todo Generation
 * 1. Start Component
 * 2. Generate Component
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Transfer extends Behaviour {

    Future<WRecord> moveAsync(WRequest request, WTransition wTransition);
}
