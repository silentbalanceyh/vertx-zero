package io.vertx.up.secure.provider;

interface Info {

    String MAP_INITED = "( Auth ) The async shared map has been initialized: name = {0}";

    String MAP_HIT = "( Auth ) Authorized success in Cache ( value = {1} ), by key = {0}";

    String MAP_MISSING = "( Auth ) Authorized failure in Cache, by key = {0}";

    String MAP_PUT = "( Auth ) Authorized cache refreshed ( value = {1} ), key = {0}";

    String FLOW_NULL = "( Auth ) No authorization cached: token = {0}";
}
