package io.vertx.up.unity;

import io.horizon.uca.cache.Cc;
import io.vertx.up.plugin.shared.UxPool;
import io.vertx.up.uca.jooq.UxJooq;

interface INFO {


    String RPC_RESULT = "( Rpc -> thenRpc ) Client = {4}, Ipc ( {0},{1} ) with params {2}, response data is {3}.";

    interface UxJob {

        String JOB_START = "( UxJob ) The job {0} has been started with timeId: {1}.";
        String JOB_STOP = "( UxJob ) The job {0} has been stopped and removed.";
        String JOB_RESUME = "( UxJob ) The job {0} will be resume.";
    }
}

interface CACHE {

    Cc<String, UxJooq> CC_JOOQ = Cc.openThread();
    Cc<String, UxPool> CC_UX_POOL = Cc.open();
    Cc<String, UxLdap> CC_LDAP = Cc.openThread();
}
