package io.vertx.mod.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.mod.workflow.atom.runtime.WRequest;
import io.vertx.mod.workflow.atom.runtime.WTransition;
import io.vertx.mod.workflow.uca.central.Behaviour;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Movement extends Behaviour {

    Future<WTransition> moveAsync(WRequest request);
}
