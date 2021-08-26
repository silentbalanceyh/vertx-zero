package io.vertx.up.uca.rs.router;

import io.reactivex.Observable;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.ChainAuthHandler;
import io.vertx.up.atom.secure.Cliff;
import io.vertx.up.eon.Orders;
import io.vertx.up.eon.Values;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.uca.rs.secure.Bolt;
import io.vertx.up.uca.web.failure.AuthenticateEndurer;

import java.util.Set;
import java.util.TreeSet;

/**
 * Secure mount
 * 401 for authorization only
 */
public class WallAxis implements Axis<Router> {

    /**
     * Extract all walls that will be generated route.
     */
    private static final Set<Cliff> WALLS =
        ZeroAnno.getWalls();

    static {
        WALLS.forEach(wall -> {
            // Initialize cliff set
            if (!Pool.WALL_MAP.containsKey(wall.getPath())) {
                Pool.WALL_MAP.put(wall.getPath(), new TreeSet<>());
            }
            // Add cliff instance by path
            Pool.WALL_MAP.get(wall.getPath()).add(wall);
        });
    }

    private transient final Vertx vertx;
    private transient final Bolt bolt;

    public WallAxis(final Vertx vertx) {
        this.vertx = vertx;
        bolt = Bolt.get();
    }

    @Override
    public void mount(final Router router) {
        /*
         * Wall mount for authorization
         */
        Pool.WALL_MAP.forEach((path, cliffes) -> {
            // 1. Build Handler
            final AuthHandler handler = create(vertx, cliffes);
            // 2. Path/Order to set Router
            if (null != handler) {
                router.route(path).order(Orders.SECURE).handler(handler)
                    // Shared Failure Handler
                    .failureHandler(AuthenticateEndurer.create());
            }
            // 3. Wall Advanced, For user data filling.
            /*
             * New design for 403 access issue here to implement RBAC mode
             * This design is optional plugin into zero system, you can enable this feature.
             * 403 access handler must be as following
             * 1. The uri is the same as 401, it means that the request must be passed to 401 handler first
             * 2. The order must be after 401 Orders.SECURE
             */
        });
    }

    /**
     * Two mode for handler supported.
     *
     * @param cliffes Cliff in zero system.
     *
     * @return Auth Handler that will be mount to vertx router.
     */
    private AuthHandler create(final Vertx vertx, final Set<Cliff> cliffes) {
        AuthHandler resultHandler = null;
        if (Values.ONE < cliffes.size()) {
            // 1 < handler
            final ChainAuthHandler chain = ChainAuthHandler.create();
            Observable.fromIterable(cliffes)
                .map(item -> bolt.mount(vertx, item))
                .subscribe(chain::append).dispose();
            resultHandler = chain;
        } else {
            // 1 = handler
            if (!cliffes.isEmpty()) {
                final Cliff cliff = cliffes.iterator().next();
                resultHandler = bolt.mount(vertx, cliff);
            }
        }
        return resultHandler;
    }
}
