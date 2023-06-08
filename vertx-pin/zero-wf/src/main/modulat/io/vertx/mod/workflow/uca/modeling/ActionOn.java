package io.vertx.mod.workflow.uca.modeling;

import cn.vertxup.workflow.cv.WfPool;
import cn.vertxup.workflow.cv.em.RecordMode;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface ActionOn {

    static ActionOn action(final RecordMode caseType) {
        final Supplier<ActionOn> supplier = T.POOL_SUPPLIER.get(caseType);
        Objects.requireNonNull(supplier);
        return WfPool.CC_ACTION.pick(supplier, caseType.name());
        // Fn.po?lThread(WfPool.POOL_ACTION, supplier, caseType.name());
    }

    // -------------------- Single ---------------
    <T> Future<JsonObject> createAsync(JsonObject params, MetaInstance metadata);

    <T> Future<JsonObject> updateAsync(String key, JsonObject params, MetaInstance metadata);

    <T> Future<JsonObject> fetchAsync(String key, String identifier, MetaInstance metadata);

    // -------------------- Batch ----------------

    <T> Future<JsonArray> createAsync(JsonArray params, MetaInstance instance);

    <T> Future<JsonArray> updateAsync(Set<String> keys, JsonArray params, MetaInstance metadata);

    <T> Future<JsonArray> fetchAsync(Set<String> keys, String identifier, MetaInstance metadata);
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
