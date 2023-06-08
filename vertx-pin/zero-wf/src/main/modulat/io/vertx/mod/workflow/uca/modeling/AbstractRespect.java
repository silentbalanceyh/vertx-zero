package io.vertx.mod.workflow.uca.modeling;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractRespect implements Respect {
    private final transient JsonObject query = new JsonObject();

    AbstractRespect(final JsonObject query) {
        this.query.mergeIn(query, true);
    }

    protected JsonObject queryTpl() {
        return this.query.copy();
    }

    protected JsonObject queryTpl(final WTicket ticket) {
        final JsonObject parameters = Ux.toJson(ticket);
        // Old Code: final JsonObject queryJ = this.query.copy();
        // Create new Object to avoid modify
        return Ut.fromExpression(this.query, parameters);
    }

    /*
     * Assign new fields from `params` here
     * 1. Create Workflow
     * - createdAt
     * - createdBy
     *
     * 2. Common Workflow Include ( Update )
     * - updatedAt
     * - updatedBy
     * - active
     * - language
     * - sigma
     */
    protected JsonArray syncPre(final JsonArray data, final JsonObject params, final WRecord record) {
        Ut.itJArray(data).forEach(json -> {
            if (!json.containsKey(KName.CREATED_BY)) {
                // Created new linkage
                // - createdAt, createdBy
                json.put(KName.CREATED_BY, params.getValue(KName.UPDATED_BY));
                json.put(KName.CREATED_AT, params.getValue(KName.UPDATED_AT));
            }

            // All information came from
            Ut.valueCopy(json, params,
                KName.UPDATED_BY,
                KName.UPDATED_AT,
                KName.ACTIVE,
                KName.LANGUAGE,
                KName.SIGMA
            );
            // Call additional Data Conversation
            this.syncPre(json, params, record);
        });
        return data;
    }

    protected void syncPre(final JsonObject data, final JsonObject params, final WRecord record) {
        // You can overwrite more in this method
    }
}
