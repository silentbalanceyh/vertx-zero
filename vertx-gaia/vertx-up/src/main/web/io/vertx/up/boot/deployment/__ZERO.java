package io.vertx.up.boot.deployment;

interface INFO {

    String INFO_ROTATE = "Zero container will select new DeployMode ( mode = {0} ).";

    String VTC_OPT = "( Verticle ) The deployment options has been captured: " +
        "instances = {0}, ha = {1}, content = {2}";
}

interface NAME {

    String TYPE = "type";

    String INSTANCES = "instances";

    String GROUP = "group";

    String HA = "ha";
}
