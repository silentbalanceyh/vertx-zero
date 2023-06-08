package io.vertx.mod.workflow.uca.ticket;

import cn.vertxup.workflow.domain.tables.daos.WTicketDao;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.mod.workflow.uca.toolkit.URequest;
import io.vertx.mod.workflow.uca.toolkit.UTL;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

class SyncTicket extends AbstractSync {

    SyncTicket(final MetaInstance metadata) {
        super(metadata);
    }

    @Override
    public Future<WRecord> treatAsync(final JsonObject requestJ, final WRecord recordIn, final WTicket ticketIn) {
        return Ux.future(ticketIn)

            // Before
            .compose(ticket -> UTL.beforeUpdate(ticket, recordIn))

            // Sync
            .compose(recordRef -> {
                final UxJooq tJq = Ux.Jooq.on(WTicketDao.class);
                final JsonObject ticketJ = requestJ.copy();
                /*
                 * Key Point for attachment linkage here, the linkage must contain
                 * serial part in params instead of distinguish between ADD / EDIT
                 */
                if (!requestJ.containsKey(KName.SERIAL)) {
                    requestJ.put(KName.SERIAL, ticketIn.getSerial());
                }

                // Compress Json
                URequest.reduceJ(ticketJ);
                final WTicket combine = Ux.updateT(ticketIn, ticketJ);
                return tJq.updateAsync(combine);
            })

            // After
            .compose(updated -> UTL.afterUpdate(updated, recordIn));
    }
}
