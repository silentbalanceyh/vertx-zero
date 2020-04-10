package io.vertx.up.secure.provider;

interface Info {

    String MAP_INITED = "( Auth ) The async shared map has been initialized: name = {0}";

    String MAP_HIT = "( Auth ) The async shared map cache has been hitted by key = {0}, value = {1}";

    String MAP_MISSING = "( Auth ) The async shared map cache has not been hitted by key = {0}";

    String MAP_PUT = "( Auth ) The async shared map cache has been put with key = {0}, value = {1}";

    String FLOW_NULL = "( Auth ) No authorization cached: token = {0}";
}
