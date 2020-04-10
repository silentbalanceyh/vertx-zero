package io.vertx.tp.jet.uca.micro;

import io.vertx.tp.optic.jet.JtConsumer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, JtConsumer> CONSUMER_CLS = new ConcurrentHashMap<>();
}
