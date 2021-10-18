package io.vertx.tp.optic.ambient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<String, AoRefine> REFINE_POOL
        = new ConcurrentHashMap<>();
}
