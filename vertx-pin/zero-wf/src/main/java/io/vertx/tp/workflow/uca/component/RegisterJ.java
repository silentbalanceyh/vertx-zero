package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.uca.modeling.ActionOn;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RegisterJ extends AbstractRecord {
    @Override
    public Future<JsonObject> insertAsync(final JsonObject params, final MetaInstance metadata) {
        final JsonObject rData = params.getJsonObject(KName.RECORD, new JsonObject());

        final ActionOn action = ActionOn.action(metadata.recordMode());
        final JsonObject recordData = this.normalize(params, rData, true);

        // Generate the record serial number
        return Ke.umIndent(recordData, metadata.recordIndent())
            .compose(processed -> action.createAsync(processed, metadata))
            .compose(record -> this.outputAsync(params, record));
    }

    @Override
    public Future<JsonObject> updateAsync(final JsonObject params, final MetaInstance metadata) {
        final JsonObject rData = params.getJsonObject(KName.RECORD, new JsonObject());

        final ActionOn action = ActionOn.action(metadata.recordMode());
        // Data Preparing
        final JsonObject recordData = this.normalize(params, rData, false);
        final String key = metadata.recordKeyU(recordData);
        return action.updateAsync(key, recordData, metadata)
            .compose(record -> this.outputAsync(params, record));
    }

    @Override
    public Future<JsonObject> saveAsync(final JsonObject params, final MetaInstance metadata) {
        final JsonObject rData = params.getJsonObject(KName.RECORD, new JsonObject());

        final ActionOn action = ActionOn.action(metadata.recordMode());
        final JsonObject recordData = this.normalize(params, rData, true);
        final String key = metadata.recordKeyU(recordData);
        Objects.requireNonNull(key);
        return action.fetchAsync(key, metadata).compose(queried -> {
            if (Ut.isNil(queried)) {
                // Create New
                return action.createAsync(recordData, metadata);
            } else {
                // Update New ( Skip createdAt, createdBy )
                recordData.remove(KName.CREATED_AT);
                recordData.remove(KName.CREATED_BY);
                return action.updateAsync(key, recordData, metadata);
            }
        }).compose(record -> this.outputAsync(params, record));
    }
}
