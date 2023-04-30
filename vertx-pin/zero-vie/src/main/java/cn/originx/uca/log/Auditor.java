package cn.originx.uca.log;

import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.eon.KName;

import java.util.Queue;
import java.util.Set;

/**
 * 记录专用
 *
 * 1. AuditorTodo（待确认单构造）
 * 2. AuditorTrue（变更历史 active = true）
 * 3. AuditorTrace（变更历史 active = false）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Auditor {

    static Auditor history(final DataAtom atom, final JsonObject options) {
        return AuditorHistory.CC_AUDITOR.pick(() -> new AuditorHistory(options).bind(atom), atom.atomKey(options));
        // Fn.po?l(AuditorHistory.POOL_HISTORY, atom.atomKey(options), () -> new AuditorHistory(options).bind(atom));
    }

    Auditor bind(DataAtom atom);

    Future<JsonObject> trackAsync(JsonObject recordN, JsonObject recordO,
                                  String serial, Set<String> ignoreSet);

    default Future<JsonObject> trackAsync(final JsonObject twins,
                                          final String serial, final Set<String> ignoreSet) {
        return this.trackAsync(
            twins.getJsonObject(KName.__.NEW), twins.getJsonObject(KName.__.OLD),
            serial, ignoreSet
        );
    }

    /*
     * 默认不开放批量行为
     */
    default Future<JsonArray> trackAsync(final JsonArray recordN, final JsonArray recordO,
                                         final Queue<String> serial, final Set<String> ignoreSet) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
