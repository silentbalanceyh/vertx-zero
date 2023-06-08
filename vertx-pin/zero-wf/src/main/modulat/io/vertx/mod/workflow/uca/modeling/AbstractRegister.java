package io.vertx.mod.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractRegister implements Register {

    protected JsonArray normalize(final JsonObject params, final JsonArray rData, final boolean isNew) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(rData).forEach(record -> normalized.add(this.normalize(params, record, isNew)));
        return normalized;
    }

    /*
     * {
     *     "record": "...",
     * }
     * - record: The json data of record
     * - The json data of todo is the major key=value
     */
    protected JsonObject normalize(final JsonObject params, final JsonObject rData, final boolean isNew) {
        // Auditor Processing
        if (isNew) {
            if (params.containsKey(KName.CREATED_AT)) {
                Ut.valueCopy(rData, params,
                    KName.CREATED_AT,
                    KName.CREATED_BY
                );
            } else {
                rData.put(KName.CREATED_BY, params.getValue(KName.UPDATED_BY));
                rData.put(KName.CREATED_AT, params.getValue(KName.UPDATED_AT));
            }
        }
        Ut.valueCopy(rData, params,
            KName.UPDATED_AT,
            KName.UPDATED_BY,
            KName.SIGMA,
            KName.LANGUAGE
        );
        // Zero Specification
        if (!rData.containsKey(KName.ACTIVE)) {
            rData.put(KName.ACTIVE, Boolean.TRUE);
        }
        // Identifier Processing
        rData.put(KName.IDENTIFIER, params.getValue(KName.MODEL_ID));
        return rData;
    }

    protected Future<JsonObject> outputAsync(final JsonObject params, final JsonArray record) {
        // Callback Operation On Record
        return Ux.future(params.put(KName.RECORD, record));
    }

    protected Future<JsonObject> outputAsync(final JsonObject params, final JsonObject record) {
        // Callback Operation On Record
        if (Ut.isNotNil(record)) {
            params.put(KName.RECORD, record);
        }
        return Ux.future(params);
    }
}
