package io.vertx.up.backbone.config;

interface INFO {

    String AGENT_HIT = "( Agent ) The standard bottle " +
        "{0} will be deployed.";

    String WORKER_HIT = "( Worker ) The worker verticl " +
        "{0} will be deployed.";

    String ADDRESS_IN = "Vert.x zero has found {0} " +
        "incoming address from the system. Incoming address list as below: ";

    String ADDRESS_ITEM = "       Addr : {0}";

    String METHOD_IGNORE = "Method name = {0} has not annotated with " +
        "jakarta.ws.rs.[@GET,@POST,@PUT,@DELETE,@OPTIONS,@PATCH,@HEAD], ignored resolving.";

    String METHOD_MODIFIER = "( Ignored ) Method name = {0} access scope is invalid, " +
        "the scope must be public non-static.";
}
