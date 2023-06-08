package io.vertx.mod.workflow.uca.ticket;

import cn.vertxup.workflow.domain.tables.pojos.WTicket;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.mod.workflow.atom.runtime.WRecord;

interface Pool {
    Cc<String, Sync> POOL_SYNC = Cc.openThread();
}

public interface Sync {

    static Sync ticket(final MetaInstance instance) {
        return Pool.POOL_SYNC.pick(() -> new SyncTicket(instance), SyncTicket.class.getName());
    }

    static Sync extension(final MetaInstance instance) {
        return Pool.POOL_SYNC.pick(() -> new SyncExtension(instance), SyncExtension.class.getName());
    }

    static Sync task(final MetaInstance instance) {
        return Pool.POOL_SYNC.pick(() -> new SyncTask(instance), SyncTask.class.getName());
    }

    Future<WRecord> treatAsync(JsonObject requestJ, WRecord recordRef, WTicket ticketIn);

    default Future<WRecord> treatAsync(final JsonObject requestJ, final WRecord recordRef) {
        return this.treatAsync(requestJ, recordRef, null);
    }
}
