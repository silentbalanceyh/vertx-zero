package io.vertx.up.verticle;

import io.vertx.ext.web.Router;
import io.horizon.uca.cache.Cc;
import io.vertx.up.uca.rs.Axis;

interface Info {

    String HTTP_SERVERS = "( Http Server ) {0} (id = {1}) Agent has deployed HTTP Server on {2}.";

    String MAPPED_ROUTE = "( Uri Register ) \"{1}\" has been deployed by {0}, Options = {2}.";

    String HTTP_LISTEN = "( Http Server ) {0} Http Server has been started successfully. Endpoint: {1}.";

    String MICRO_REGISTRY_SEND = "---> {2} ( Http Server ) {0} ( name = {1} ) " +
        "is sending data to internal address.";

    String IPC_REGISTRY_SEND = "---> {2} ( Rpc Server ) {0} ( name = {1} ) " +
        "is sending data to internal address.";

    String MICRO_REGISTRY_CONSUME = "{2} <--- ( Micro Worker ) {0} ( name = {1} ) " +
        "getNull data from internal address.";

    String RPC_LISTEN = "( Rpc Server ) Rpc Server has been started successfully. Channel: ---> grpc://{0}:{1}. ";

    String RPC_FAILURE = "( Rpc Server ) Rpc Server met failure: details = {0}.";

    String ETCD_SUCCESS = "( Etcd Center ) Zero system detected configuration {0}, begin to singleton Etcd Center.";

    String REG_SUCCESS = "( Discovery ) Record : ( name = {2}, uri = {3} ) " +
        "key = {4}, id = {5}, status = {0}, type = {1} " +
        "has been refreshed in Zero system.";

    String REG_REFRESHED = "( Discovery ) Records ( added = {0}, updated = {1}, deleted = {2} ) have been refreshed! ";

    String REG_FAILURE = "( Discovery ) Action: {1}, Service Registration has met error: {0}.";

    String API_GATEWAY = "( Api Gateway ) {0} (id = {1}) has deployed on {2}.";

    String API_LISTEN = "( Api Gateway ) {0} has been started successfully. " +
        "Endpoint: {1}.";

    String MSG_INVOKER = "( Invoker ) Zero system selected {0} as invoker," +
        "the metadata receipt hash code = {1}, invoker size = {2}.";

    String JOB_EMPTY = "Zero system detect no jobs, the scheduler will be stopped.";

    String JOB_CONFIG_NULL = "( Ignore ) Because there is no definition in `vertx-job.yml`, Job container is stop....";

    String JOB_MONITOR = "Zero system detect {0} jobs, the scheduler will begin....";

    String JOB_AGHA_SELECTED = "[ Job ] Agha = {0} has been selected for job {1} of type {2}";

    String JOB_STARTED = "[ Job ] All Job schedulers have been started!!!";
}

interface Pool {

    Cc<String, Axis<Router>> CC_ROUTER = Cc.openThread();
}

interface Registry {

    String NAME = "name";

    String OPTIONS = "options";

    String URIS = "uris";
}