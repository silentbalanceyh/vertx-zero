package io.vertx.tp.jet.uca.tunnel;

import io.vertx.tp.optic.component.Dictionary;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<Integer, Dictionary> POOL_DICT_SERVICE =
            new ConcurrentHashMap<>();
}
