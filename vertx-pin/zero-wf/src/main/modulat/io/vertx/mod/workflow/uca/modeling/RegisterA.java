package io.vertx.mod.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.up.eon.KName;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RegisterA extends AbstractRegister {
    @Override
    public Future<JsonObject> insertAsync(final JsonObject params, final MetaInstance metadata) {
        final JsonArray rData = params.getJsonArray(KName.RECORD, new JsonArray());

        final ActionOn action = ActionOn.action(metadata.recordMode());
        final JsonArray recordData = this.normalize(params, rData, true);

        // Generate the record serial number
        return Ke.umIndent(recordData, metadata.recordIndent())
            .compose(processed -> action.createAsync(processed, metadata))
            .compose(record -> this.outputAsync(params, record));
    }

    @Override
    public Future<JsonObject> updateAsync(final JsonObject params, final MetaInstance metadata) {
        final JsonArray rData = params.getJsonArray(KName.RECORD, new JsonArray());

        final ActionOn action = ActionOn.action(metadata.recordMode());
        // Data Preparing
        final JsonArray recordData = this.normalize(params, rData, false);
        final Set<String> keys = metadata.recordKeyU(recordData);
        return action.updateAsync(keys, recordData, metadata)
            .compose(record -> this.outputAsync(params, record));
    }

    @Override
    public Future<JsonObject> saveAsync(final JsonObject params, final MetaInstance metadata) {
        return this.updateAsync(params, metadata);
    }
}
