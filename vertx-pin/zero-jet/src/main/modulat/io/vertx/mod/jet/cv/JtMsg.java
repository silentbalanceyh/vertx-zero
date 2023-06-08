package io.vertx.mod.jet.cv;

public interface JtMsg {
    String AGENT_CONFIG = "Jet agent will begin `Routing System` with additional config = {0}";

    String WEB_ENGINE = "Web request: `{0} {1}`, params: {2}";
    String WEB_SEND = "Send data `{0}` to address = `{1}`";

    String WORKER_DEPLOY = "Workers will be deployed in background async ......";
    String WORKER_FAILURE = "AmbientOld XHeader booting error, initialized handler";
    String WORKER_DEPLOYING = "Worker instance = {0}, class = {1}";
    String WORKER_DEPLOYED = "Worker `{0}` has been deployed successfully!!! ( instance = {1} )";

    String CONSUME_MESSAGE = "Api interface: id = {0}, method = {1}, path = {2}";
    String CONSUME_API = "---> Api Json: {0}";
    String CONSUME_SERVICE = "---> Service Json: {0}";
    String CONSUME_WORKER = "---> Worker Json: {0}";

    String CHANNEL_SELECT = "Channel selected: class = {0}";
    String COMPONENT_SELECT = "Component = {0}, Record = {1}";
}
