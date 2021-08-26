package io.vertx.up.uca.cosmic;

import io.vertx.core.http.HttpMethod;
import io.vertx.up.commune.config.Integration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

interface Pool {
    ConcurrentMap<Integer, Emitter> POOL_EMITTER = new ConcurrentHashMap<>();

    ConcurrentMap<Integer, Rotator> POOL_ROTATOR = new ConcurrentHashMap<>();

    ConcurrentMap<HttpMethod, Function<Integration, Rotator>> POOL_ROTATOR_FN =
        new ConcurrentHashMap<HttpMethod, Function<Integration, Rotator>>() {
            {
                this.put(HttpMethod.GET, GetRotator::new);
                this.put(HttpMethod.DELETE, DeleteRotator::new);
                this.put(HttpMethod.POST, PostRotator::new);
                this.put(HttpMethod.PUT, PutRotator::new);
            }
        };
}

interface Message {
    String HTTP_REQUEST = "Http request: uri = {0}, method = {1}, data = {2}";

    String HTTP_RESPONSE = "Http response: data = {0}";
}
