package io.vertx.up.uca.cosmic;

import io.horizon.annotations.Memory;
import io.horizon.uca.cache.Cc;
import io.vertx.core.http.HttpMethod;
import io.vertx.up.commune.config.Integration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

interface CACHE {

    @Memory(Emitter.class)
    Cc<Integer, Emitter> CC_EMITTER = Cc.open();

    @Memory(Rotator.class)
    Cc<Integer, Rotator> CC_ROTATOR = Cc.open();

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

interface INFO {
    String HTTP_REQUEST = "Http request: uri = {0}, method = {1}, data = {2}";

    String HTTP_RESPONSE = "Http response: data = {0}";
}
