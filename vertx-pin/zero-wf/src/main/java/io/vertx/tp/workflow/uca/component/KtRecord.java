package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.uca.modeling.ActionOn;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KtRecord {
    private final transient MetaInstance metadata;

    private KtRecord(final MetaInstance metadata) {
        this.metadata = metadata;
    }

    static KtRecord toolkit(final MetaInstance metadata) {
        return new KtRecord(metadata);
    }

    /*
     * {
     *     "record": "...",
     * }
     * - record: The json data of record
     * - The json data of todo is the major key=value
     */
    private JsonObject normalize(final JsonObject params, final boolean isNew) {
        JsonObject rData = params.getJsonObject(KName.RECORD, new JsonObject());
        if (Ut.notNil(rData)) {
            rData = rData.copy();
        }
        // Auditor Processing
        if (isNew) {
            Ut.ifJAssign(params,
                KName.CREATED_AT,
                KName.CREATED_BY
            ).apply(rData);
        }
        Ut.ifJAssign(params,
            KName.UPDATED_AT,
            KName.UPDATED_BY,
            KName.SIGMA,
            KName.LANGUAGE
        ).apply(rData);
        return rData;
    }

    Future<JsonObject> insertAsync(final JsonObject params) {
        Objects.requireNonNull(this.metadata);
        final ActionOn action = ActionOn.action(this.metadata.recordMode());
        final JsonObject recordData = this.normalize(params, true);
        // Generate the record serial number
        return this.metadata.serialR(recordData)
            .compose(processed -> action.createAsync(processed, this.metadata));
    }

    Future<JsonObject> updateAsync(final JsonObject params) {
        Objects.requireNonNull(this.metadata);
        final ActionOn action = ActionOn.action(this.metadata.recordMode());
        // Data Preparing
        final JsonObject recordData = this.normalize(params, false);
        final String key = this.metadata.recordKeyU(recordData);
        return action.updateAsync(key, recordData, this.metadata);
    }

    Future<JsonObject> saveAsync(final JsonObject params) {
        Objects.requireNonNull(this.metadata);
        final ActionOn action = ActionOn.action(this.metadata.recordMode());
        final JsonObject recordData = this.normalize(params, true);
        final String key = this.metadata.recordKeyU(recordData);
        Objects.requireNonNull(key);
        return action.fetchAsync(key, this.metadata).compose(queried -> {
            if (Ut.isNil(queried)) {
                // Create New
                return action.createAsync(recordData, this.metadata);
            } else {
                // Update New ( Skip createdAt, createdBy )
                recordData.remove(KName.CREATED_AT);
                recordData.remove(KName.CREATED_BY);
                return action.updateAsync(key, recordData, this.metadata);
            }
        });
    }
}
