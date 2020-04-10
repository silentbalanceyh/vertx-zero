package io.vertx.tp.plugin.history;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<String, TrashClient> CLIENT_POOL = new ConcurrentHashMap<>();


}
