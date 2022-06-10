package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.tp.workflow.uca.canal.Behaviour;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Movement extends Behaviour {

    Future<WProcess> moveAsync(WRequest request);
}
