package io.vertx.tp.workflow.uca.modeling;

import cn.zeroup.macrocosm.cv.WfPool;
import cn.zeroup.macrocosm.cv.em.RecordMode;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.up.fn.Fn;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ActionOn {

    static ActionOn action(final RecordMode caseType) {
        final Supplier<ActionOn> supplier = T.POOL_SUPPLIER.get(caseType);
        Objects.requireNonNull(supplier);
        return Fn.poolThread(WfPool.POOL_ACTION, supplier, caseType.name());
    }

    <T> Future<JsonObject> createAsync(JsonObject params, MetaInstance metadata);

    <T> Future<JsonObject> updateAsync(String key, JsonObject params, MetaInstance metadata);

    <T> Future<JsonObject> fetchAsync(String key, MetaInstance metadata);
}

interface T {
    ConcurrentMap<RecordMode, Supplier<ActionOn>> POOL_SUPPLIER = new ConcurrentHashMap<>() {
        {
            this.put(RecordMode.CASE, ActionCase::new);
            this.put(RecordMode.DAO, ActionDao::new);
            this.put(RecordMode.ATOM, ActionDynamic::new);
        }
    };
}
