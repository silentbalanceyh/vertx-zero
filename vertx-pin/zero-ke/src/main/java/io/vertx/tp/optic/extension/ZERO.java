package io.vertx.tp.optic.extension;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<String, Init> INIT_POOL
        = new ConcurrentHashMap<>();

    ConcurrentMap<String, Prerequisite> PREREQUISITE_POOL
        = new ConcurrentHashMap<>();
}
