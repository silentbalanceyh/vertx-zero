package io.vertx.tp.ambient.refine;

import io.vertx.tp.optic.extension.Init;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, Init> INIT_POOL
        = new ConcurrentHashMap<>();
}
