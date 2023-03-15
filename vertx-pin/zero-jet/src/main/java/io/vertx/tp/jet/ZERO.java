package io.vertx.tp.jet;

import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.tp.jet.uca.aim.JtAim;
import io.vertx.up.uca.cache.Cc;

import java.util.Set;

interface Pool {

    Cc<String, JtAim> CC_AIM = Cc.openThread();

    // Address
    Set<Class<?>> WORKER_SET = new ConcurrentHashSet<>();
    Set<Class<?>> WORKER_JS_SET = new ConcurrentHashSet<>();
}
