package io.vertx.tp.jet;

import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.tp.jet.uca.aim.JtAim;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<Class<?>, JetThanatos> ENSURERS = new ConcurrentHashMap<>();

    ConcurrentMap<String, JtAim> AIM_ENGINE_HUBS = new ConcurrentHashMap<>();
    ConcurrentMap<String, JtAim> AIM_IN_HUBS = new ConcurrentHashMap<>();
    ConcurrentMap<String, JtAim> AIM_SEND_HUBS = new ConcurrentHashMap<>();
    ConcurrentMap<String, JtAim> AIM_PRE_HUBS = new ConcurrentHashMap<>();

    // Address
    Set<Class<?>> WORKER_SET = new ConcurrentHashSet<>();
    Set<Class<?>> WORKER_JS_SET = new ConcurrentHashSet<>();
}
