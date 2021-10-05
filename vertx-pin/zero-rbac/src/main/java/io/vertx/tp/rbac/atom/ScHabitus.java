package io.vertx.tp.rbac.atom;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.eon.Constants;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxPool;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * `habitus` mapping here, the structure should be:
 * 1. session id -> habitus ( session id may be upgraded )
 *      when upgraded, the pool should be clear
 * 2. token -> habitus
 */
public class ScHabitus {
    /*
     * name = HABITUS_CACHE ( Logged User )
     *      2.1. Fixed name for `habitus` storage
     *           The name is that each habitus key should be:
     *               "HABITUS-" + <habitus> + "-SESSION"
     * Each habitus keep a pool
     */
    private static final Annal LOGGER = Annal.get(ScHabitus.class);
    private static final ConcurrentMap<String, ScHabitus> POOLS =
        new ConcurrentHashMap<>();
    private final transient UxPool pool;
    private final transient String habitus;

    private ScHabitus(final String habitus) {
        Sc.infoResource(ScHabitus.LOGGER, AuthMsg.POOL_RESOURCE, Constants.Pool.HABITUS, habitus);
        this.habitus = habitus;
        this.pool = Ux.Pool.on(Constants.Pool.HABITUS);
    }

    public static ScHabitus initialize(final String habitus) {
        return Fn.pool(ScHabitus.POOLS, habitus, () -> new ScHabitus(habitus));
    }

    /*
     * Monitor for current ScHabitus
     */
    public static Set<String> habitus() {
        return POOLS.keySet();
    }

    /*
     * Pool should be initialized by pool name above
     */
    @SuppressWarnings("unchecked")
    public <T> Future<T> get(final String dataKey) {
        return this.pool.<String, JsonObject>get(this.habitus)
            .compose(item -> {
                /* To avoid Null Pointer Issue */
                if (Objects.isNull(item)) {
                    return Future.succeededFuture(null);
                } else {
                    return Ux.future((T) item.getValue(dataKey));
                }
            });
    }

    public <T> Future<T> set(final String dataKey, final T value) {
        return this.pool.<String, JsonObject>get(this.habitus)
            .compose(stored -> {
                if (Ut.isNil(stored)) {
                    stored = new JsonObject();
                }
                /*
                 * Store dataKey = value
                 */
                final JsonObject updated = stored.copy();
                updated.put(dataKey, value);
                return this.pool.put(this.habitus, updated)
                    .compose(nil -> Ux.future(value));
            });
    }

    public Future<Boolean> clear() {
        /*
         * Remove reference pool for `habitus`
         */
        POOLS.remove(this.habitus);
        return this.pool.remove(this.habitus)
            /*
             * Remove current habitus from pool ( Pool Structure )
             */
            .compose(kv -> Future.succeededFuture(Boolean.TRUE));
    }
}
