package io.vertx.up.uca.rs.router;

import io.reactivex.Observable;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.ext.web.handler.ChainAuthHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.eon.Orders;
import io.vertx.up.eon.Values;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.secure.handler.Bolt;
import io.vertx.up.uca.rs.Axis;
import io.vertx.up.uca.web.failure.AuthenticateEndurer;

import java.util.Objects;
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
    private static final Set<Aegis> WALLS =
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
        this.bolt = Bolt.get();
    }

    @Override
    public void mount(final Router router) {
        /*
         * Wall mount for authorization
         */
        Pool.WALL_MAP.forEach((path, aegisSet) -> {
            // 1. Build Handler
            final AuthenticationHandler authorizer = this.handlerAuthorize(this.vertx, aegisSet);
            // 2. Path/Order to set Router
            if (null != authorizer) {
                router.route(path).order(Orders.SECURE).handler(authorizer)
                    // Shared Failure Handler
                    .failureHandler(AuthenticateEndurer.create());
            }
            // 3. Wall Advanced, For user data filling.
            final AuthorizationHandler accessor = this.handlerAccess(this.vertx, aegisSet);
            if (Objects.nonNull(accessor)) {
                router.route(path).order(Orders.SECURE_AUTHORIZATION).handler(accessor)
                    // Shared Failure Handler
                    .failureHandler(AuthenticateEndurer.create());
            }
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
     * @param aegisSet Cliff in zero system.
     *
     * @return Auth Handler that will be mount to vertx router.
     */
    private AuthenticationHandler handlerAuthorize(final Vertx vertx, final Set<Aegis> aegisSet) {
        AuthenticationHandler resultHandler = null;
        if (Values.ONE < aegisSet.size()) {
            // 1 < handler
            final ChainAuthHandler chain = ChainAuthHandler.all();
            Observable.fromIterable(aegisSet)
                .map(item -> this.bolt.authorize(vertx, item))
                .subscribe(chain::add).dispose();
            resultHandler = chain;
        } else {
            // 1 = handler
            if (!aegisSet.isEmpty()) {
                final Aegis aegis = aegisSet.iterator().next();
                resultHandler = this.bolt.authorize(vertx, aegis);
            }
        }
        return resultHandler;
    }

    private AuthorizationHandler handlerAccess(final Vertx vertx, final Set<Aegis> aegisSet) {
        /*
         * Find first not-null authorization handler
         */
        final Aegis aegis = aegisSet.stream()
            .filter(Aegis::okForAccess).findFirst().orElse(null);
        if (Objects.isNull(aegis)) {
            return null;
        }
        return this.bolt.access(vertx, aegis);
    }
}
