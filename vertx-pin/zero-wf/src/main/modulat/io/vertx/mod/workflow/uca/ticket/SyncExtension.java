package io.vertx.mod.workflow.uca.ticket;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.mod.workflow.uca.toolkit.UTL;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

class SyncExtension extends AbstractSync {
    SyncExtension(final MetaInstance metadata) {
        super(metadata);
    }

    /*
     * {
     *     "children": {
     *         "dao": "xxx",
     *         "fields": [
     *             "xxx"
     *         ],
     *         "auditor": [
     *         ]
     *     }
     * }
     */
    @Override
    public Future<WRecord> treatAsync(final JsonObject requestJ, final WRecord recordRef, final WTicket ticketIn) {
        /*
         * children -> dao
         *
         * 1. When the extension Dao has not been configured, ignore this step
         * 2. The configuration is as above
         */
        final UxJooq tJq = this.metadata.childDao();
        if (Objects.isNull(tJq)) {
            return Ux.future(recordRef);
        }


        /*
         * children -> fields
         *
         * Here the data will contain `children` fields only
         * Extract the data based on fields.
         */
        // JsonObject data for child
        final JsonObject data = this.metadata.childIn(requestJ);


        /*
         * The WTicket and childJ shared `key` between ticket / child ticket
         * W_TICKET JOIN W_XXX ON W_TICKET.KEY = W_XXX.KEY
         */
        final WTicket ticket = recordRef.ticket();
        data.put(KName.KEY, ticket.getKey());
        return tJq.fetchJOneAsync(KName.KEY, ticket.getKey()).compose(queryJ -> Ux.future(queryJ)

            // Before
            .compose(ticketJ -> UTL.beforeUpdate(ticketJ, recordRef))

            // Sync
            .compose(nil -> {
                /*
                 * CombineJ contains:
                 * 1. data          -> Input JsonObject ( Data )
                 * 2. queryJ        -> Original JsonObject
                 * The direction is as:
                 * queryJ <- data
                 * The data will overwrite the queryJ here.
                 */
                final JsonObject combineJ = queryJ.copy().mergeIn(data, true);
                if (Ut.isNil(queryJ)) {
                    // Does not Exist
                    return tJq.insertJAsync(combineJ);
                } else {
                    // Existing
                    return tJq.updateJAsync(ticket.getKey(), combineJ);
                }
            })

            // ChildOut
            .compose(updated -> Ux.future(this.metadata.childOut(updated)))

            // After
            .compose(updateJ -> UTL.afterUpdate(updateJ, recordRef))
        );
    }
}
