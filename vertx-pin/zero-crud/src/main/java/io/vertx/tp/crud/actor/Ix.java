package io.vertx.tp.crud.actor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<String, IxActor> ACTOR_MAP =
        new ConcurrentHashMap<>();
}
