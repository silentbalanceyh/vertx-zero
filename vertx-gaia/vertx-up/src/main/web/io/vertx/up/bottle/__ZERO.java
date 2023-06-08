package io.vertx.up.bottle;

import io.horizon.annotations.Memory;
import io.horizon.specification.boot.HAxis;
import io.horizon.uca.cache.Cc;
import io.vertx.ext.web.Router;

interface INFO {

    interface ZeroScheduler {

        String JOB_EMPTY = "Zero system detect no jobs, the scheduler will be stopped.";
        String JOB_CONFIG_NULL = "( Ignore ) Because there is no definition in `vertx-job.yml`, Job container is stop....";
        String JOB_MONITOR = "Zero system detect {0} jobs, the scheduler will begin....";
        String JOB_AGHA_SELECTED = "[ Job ] Agha = {0} has been selected for job {1} of type {2}";
        String JOB_STARTED = "[ Job ] All Job schedulers have been started!!!";
    }

    interface ZeroApiAgent {

        String API_GATEWAY = "( Api Gateway ) {0} (id = {1}) has deployed on {2}.";
        String API_LISTEN = "( Api Gateway ) {0} has been started successfully. " +
            "Endpoint: {1}.";
    }

    interface ZeroApiWorker {

        String REG_REFRESHED = "( Discovery ) Records ( added = {0}, updated = {1}, deleted = {2} ) have been refreshed! ";

        String REG_SUCCESS = "( Discovery ) Record : ( name = {2}, uri = {3} ) " +
            "key = {4}, id = {5}, status = {0}, type = {1} " +
            "has been refreshed in Zero system.";
        String REG_FAILURE = "( Discovery ) Action: {1}, Service Registration has met error: {0}.";
    }

    interface ZeroRegistry {
        String MICRO_REGISTRY_CONSUME = "{2} <--- ( Micro Worker ) {0} ( name = {1} ) " +
            "getNull data from internal address.";

    }

    interface ZeroRpcAgent {

        String RPC_LISTEN = "( Rpc Server ) Rpc Server has been started successfully. Channel: ---> grpc://{0}:{1}. ";
        String RPC_FAILURE = "( Rpc Server ) Rpc Server met handler: details = {0}.";
        String IPC_REGISTRY_SEND = "---> {2} ( Rpc Server ) {0} ( name = {1} ) " +
            "is sending data to internal address.";
        String ETCD_SUCCESS = "( Etcd Center ) Zero system detected configuration {0}, begin to singleton Etcd Center.";
    }

    interface ZeroHttpAgent {

        String HTTP_SERVERS = "( Http Server ) {0} (id = {1}) Agent has deployed HTTP Server on {2}.";

        String MAPPED_ROUTE = "( Uri Register ) \"{1}\" has been deployed by {0}, Method = {2}.";
        String HTTP_LISTEN = "( Http Server ) {0} Http Server has been started successfully. Endpoint: {1}.";
        String MICRO_REGISTRY_SEND = "---> {2} ( Http Server ) {0} ( name = {1} ) " +
            "is sending data to internal address.";
    }

    interface ZeroHttpWorker {


        String MSG_INVOKER = "( Invoker ) Zero system selected {0} as invoker," +
            "the metadata receipt hash code = {1}, invoker size = {2}.";
    }
}

interface CACHE {

    @Memory(HAxis.class)
    Cc<String, HAxis<Router>> CC_ROUTER = Cc.openThread();
}