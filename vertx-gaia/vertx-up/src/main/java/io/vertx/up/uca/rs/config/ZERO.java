package io.vertx.up.uca.rs.config;

import com.google.inject.Injector;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroAnno;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Info {

    String AGENT_HIT = "( Agent ) The standard verticle " +
        "{0} will be deployed.";

    String WORKER_HIT = "( Worker ) The worker verticl " +
        "{0} will be deployed.";

    String ADDRESS_IN = "Vert.x zero has found {0} " +
        "incoming address from the system. Incoming address list as below: ";

    String ADDRESS_ITEM = "       Addr : {0}";

    String METHOD_IGNORE = "Method name = {0} has not annotated with " +
        "javax.ws.rs.[@GET,@POST,@PUT,@DELETE,@OPTIONS,@PATCH,@HEAD], ignored resolving.";

    String METHOD_MODIFIER = "( Ignored ) Method name = {0} access scope is invalid, " +
        "the scope must be public non-static.";
}

interface Key {

    String TYPE = "type";
}

class Component {
    private static final ConcurrentMap<String, Object> SINGLETON = new ConcurrentHashMap<>();

    @SuppressWarnings("all")
    static <T> T get(final Class<?> clazz) {
        return (T) Fn.pool(SINGLETON, clazz.getName(), () -> {
            final Injector di = ZeroAnno.getDi();
            return di.getInstance(clazz);
        });
    }
}
