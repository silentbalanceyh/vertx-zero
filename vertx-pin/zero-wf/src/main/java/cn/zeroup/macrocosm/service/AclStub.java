package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WProcess;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AclStub {

    Future<JsonObject> authorize(WProcess process, WTodo todo, String userId);
}
