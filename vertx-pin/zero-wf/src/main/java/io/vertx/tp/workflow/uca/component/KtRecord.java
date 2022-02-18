package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.workflow.atom.ConfigRecord;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.uca.modeling.ActionOn;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class KtRecord {
    private final transient ConfigRecord configRecord;

    KtRecord(final ConfigRecord recordConfig) {
        this.configRecord = recordConfig;
    }

    ChangeFlag mode() {
        return this.configRecord.getFlag();
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
            KName.SIGMA
        ).apply(rData);
        return rData;
    }

    Future<JsonObject> insertAsync(final JsonObject params, final ConfigTodo configTodo) {
        Objects.requireNonNull(this.configRecord);
        final ActionOn action = ActionOn.action(this.configRecord.getMode());
        final JsonObject recordData = this.normalize(params, true);
        return Ke.umIndent(recordData, this.configRecord.getIndent())
            .compose(processed -> action.createAsync(processed, configTodo));
    }

    Future<JsonObject> updateAsync(final JsonObject params, final ConfigTodo config) {
        Objects.requireNonNull(this.configRecord);
        final ActionOn action = ActionOn.action(this.configRecord.getMode());
        // Data Preparing
        final JsonObject recordData = this.normalize(params, false);
        final String key = this.configRecord.unique(recordData);
        Objects.requireNonNull(key);
        return action.updateAsync(key, recordData, config);
    }

    Future<JsonObject> saveAsync(final JsonObject params, final ConfigTodo config) {
        Objects.requireNonNull(this.configRecord);
        final ActionOn action = ActionOn.action(this.configRecord.getMode());
        final JsonObject recordData = this.normalize(params, true);
        final String key = this.configRecord.unique(recordData);
        Objects.requireNonNull(key);
        return action.fetchAsync(key, config).compose(queried -> {
            if (Objects.isNull(queried)) {
                // Create New
                return action.createAsync(recordData, config);
            } else {
                // Update New ( Skip createdAt, createdBy )
                recordData.remove(KName.CREATED_AT);
                recordData.remove(KName.CREATED_BY);
                return action.updateAsync(key, recordData, config);
            }
        });
    }
}
