package io.vertx.tp.workflow.uca.modeling;

import cn.zeroup.macrocosm.cv.WfPool;
import cn.zeroup.macrocosm.cv.em.TodoCase;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.up.fn.Fn;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ActionOn {

    static ActionOn action(final TodoCase caseType) {
        final Supplier<ActionOn> supplier = T.POOL_SUPPLIER.get(caseType);
        Objects.requireNonNull(supplier);
        return Fn.poolThread(WfPool.POOL_ACTION, supplier, caseType.name());
    }

    <T> Future<JsonObject> createAsync(JsonObject params, ConfigTodo config);

    <T> Future<JsonObject> updateAsync(String key, JsonObject params, ConfigTodo config);
}

interface T {
    ConcurrentMap<TodoCase, Supplier<ActionOn>> POOL_SUPPLIER = new ConcurrentHashMap<>() {
        {
            this.put(TodoCase.CASE, ActionCase::new);
            this.put(TodoCase.DAO, ActionDao::new);
            this.put(TodoCase.ATOM, ActionDynamic::new);
        }
    };
}
