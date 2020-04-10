package io.vertx.tp.jet.uca.valve;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, JtIn> IN_RULE = new ConcurrentHashMap<>();
    ConcurrentMap<String, JtIn> IN_MAPPING = new ConcurrentHashMap<>();
    ConcurrentMap<String, JtIn> IN_PLUG = new ConcurrentHashMap<>();
    ConcurrentMap<String, JtIn> IN_SCRIPT = new ConcurrentHashMap<>();
}
