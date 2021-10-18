package io.vertx.tp.modular.apply;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, AoDefault> DEFAULT_POOL =
        new ConcurrentHashMap<>();
}
