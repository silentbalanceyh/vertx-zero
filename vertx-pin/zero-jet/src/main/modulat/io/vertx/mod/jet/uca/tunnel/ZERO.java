package io.vertx.mod.jet.uca.tunnel;

import io.horizon.spi.component.Dictionary;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<Integer, Dictionary> POOL_DICT_SERVICE =
        new ConcurrentHashMap<>();
}
