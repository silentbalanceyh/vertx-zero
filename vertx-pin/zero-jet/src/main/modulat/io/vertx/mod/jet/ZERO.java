package io.vertx.mod.jet;

import io.horizon.uca.cache.Cc;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.mod.jet.uca.aim.JtAim;

import java.util.Set;

interface Pool {

    Cc<String, JtAim> CC_AIM = Cc.openThread();

    // Address
    Set<Class<?>> WORKER_SET = new ConcurrentHashSet<>();
    Set<Class<?>> WORKER_JS_SET = new ConcurrentHashSet<>();
}
