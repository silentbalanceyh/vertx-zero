package io.vertx.tp.rbac.authorization;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, ScDetent> DETENT_POOL =
        new ConcurrentHashMap<>();
}

