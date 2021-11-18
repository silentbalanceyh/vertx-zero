package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AclService implements AclStub {
    @Override
    public Future<JsonObject> authorize(final WProcess process, final WTodo todo, final String userId) {
        return Ux.futureJ();
    }
}
