package io.vertx.up.uca.sectio;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.horizon.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;

import java.util.Set;

/**
 * Validation for plug-in api, you can configure the validation
 * component for api validation in zero-crud module instead of `codex` ( Development )
 *
 * 1. It's only for crud part instead of `Before/After`
 * 2. It will be called
 * -- Before Actor in zero-crud
 * -- After Actor in zero-crud
 *
 * Before component for
 * -- 1. Validation
 * -- 2. Filter
 * -- 3. Transform
 *
 * After component for
 * -- 1. Start new Job
 * -- 2. Callback
 * -- 3. Notification
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Around extends Before, After {

    @Override
    Set<ChangeFlag> types();

    /*
     * Before Operation
     */
    @Override
    default Future<JsonObject> beforeAsync(final JsonObject data, final JsonObject config) {
        return Ux.future(data);
    }

    @Override
    default Future<JsonArray> beforeAsync(final JsonArray data, final JsonObject config) {
        return Ux.future(data);
    }

    /*
     * After Operation
     */
    @Override
    default Future<JsonObject> afterAsync(final JsonObject data, final JsonObject config) {
        return Ux.future(data);
    }

    @Override
    default Future<JsonArray> afterAsync(final JsonArray data, final JsonObject config) {
        return Ux.future(data);
    }
}
