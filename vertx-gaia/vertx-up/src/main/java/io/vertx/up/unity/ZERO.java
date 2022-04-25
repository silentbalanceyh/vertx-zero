package io.vertx.up.unity;

import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.jooq.UxJooq;

interface Info {


    String RPC_RESULT = "( Rpc -> thenRpc ) Client = {4}, Ipc ( {0},{1} ) with params {2}, response data is {3}.";

    String POOL_PUT = "( Shared ) key = {0}, value = {1} has been put into {2}.";
    String POOL_PUT_TIMER = "( Shared ) key = {0}, value = {1} has been put into {2} to keep {3} seconds";
    String POOL_REMOVE = "( Shared ) key = {0} has been removed from pool name = {2}.";
    String POOL_GET = "( Shared ) key = {0} has been picked from {1}, mode = {2}";
    String POOL_CLEAR = "( Shared ) pool = {0} has been cleared successfully.";

    String JOB_START = "( UxJob ) The job {0} has been started with timeId: {1}.";
    String JOB_STOP = "( UxJob ) The job {0} has been stopped and removed.";
    String JOB_RESUME = "( UxJob ) The job {0} will be resume.";
}

interface Cache {

    Cc<String, UxJooq> CC_JOOQ = Cc.openThread();
    Cc<String, UxJooq> CC_JOOQ_HIS = Cc.openThread();
    Cc<String, UxPool> CC_UX_POOL = Cc.open();
}
