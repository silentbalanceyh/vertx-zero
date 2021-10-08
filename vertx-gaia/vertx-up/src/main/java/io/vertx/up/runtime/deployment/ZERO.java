package io.vertx.up.runtime.deployment;

interface Info {

    String INFO_ROTATE = "Zero container will select new DeployMode ( mode = {0} ).";

    String VTC_OPT = "( Verticle ) The deployment options has been captured: " +
        "instances = {0}, group = {1}, ha = {2}, content = {3}";
}

interface Key {

    String TYPE = "type";

    String INSTANCES = "instances";

    String GROUP = "group";

    String HA = "ha";
}
