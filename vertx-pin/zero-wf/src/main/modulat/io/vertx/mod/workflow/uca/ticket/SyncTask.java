package io.vertx.mod.workflow.uca.ticket;

import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.mod.workflow.atom.runtime.WRecord;
import io.vertx.mod.workflow.uca.toolkit.URequest;
import io.vertx.mod.workflow.uca.toolkit.UTL;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

public class SyncTask extends AbstractSync {
    SyncTask(final MetaInstance metadata) {
        super(metadata);
    }

    @Override
    public Future<WRecord> treatAsync(final JsonObject requestJ, final WRecord recordRef, final WTicket ticketIn) {
        final UxJooq tJq = Ux.Jooq.on(WTodoDao.class);
        final String key = requestJ.getString(KName.KEY);
        return tJq.<WTodo>fetchByIdAsync(key).compose(todoIn -> Ux.future(todoIn)

            // Before
            .compose(todo -> UTL.beforeUpdate(todo, recordRef))

            // Sync
            .compose(recordQ -> {
                final JsonObject todoJ = requestJ.copy();
                // Compress Json
                URequest.reduceJ(todoJ);
                final WTodo updated = Ux.updateT(todoIn, todoJ);
                return tJq.updateAsync(updated);
            })

            // After
            .compose(todo -> UTL.afterUpdate(todo, recordRef))
        );
    }
}
