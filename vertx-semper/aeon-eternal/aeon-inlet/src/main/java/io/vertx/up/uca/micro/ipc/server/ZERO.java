package io.vertx.up.uca.micro.ipc.server;

interface Info {

    String NODE_FINAL = "--> ( Terminator ) found, will provide response. method {0} of {1}";

    String NODE_MIDDLE = "--> ( Coordinator ) found, will transfer -->. method {0} of {1}";

    String NODE_RESPONSE = "-> Final response data = {0}";
}
