package io.vertx.up.backbone.router;

import io.horizon.eon.VValue;
import io.horizon.specification.boot.HAxis;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.ext.web.handler.ChainAuthHandler;
import io.vertx.up.boot.handler.AuthenticateEndurer;
import io.vertx.up.commune.secure.Aegis;
import io.vertx.up.eon.KWeb;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.secure.bridge.Bolt;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

/**
 * Secure mount
 * 401 for authorization only
 */
public class WallAxis implements HAxis<Router> {
    private static final Cc<String, Set<Aegis>> CC_WALLS = ZeroAnno.getWalls();

    private transient final Vertx vertx;
    private transient final Bolt bolt;

    public WallAxis(final Vertx vertx) {
        this.vertx = vertx;
        this.bolt = Bolt.get();
    }

    @Override
    public void mount(final Router router) {
        /*
         * Wall mounting for authorization
         * Here create order set to remove duplicated order and re-generate the value.
         *
         * Default order: 0
         *
         * The matrix of wall
         *
         *      Wall 1           Wall 2             Wall 3
         *      0                0                  0
         *      1                1                  1
         */
        final ConcurrentMap<String, Set<Aegis>> store = CC_WALLS.store();
        store.forEach((path, aegisSet) -> {
            if (!aegisSet.isEmpty()) {
                /*
                 * The handler of 401 of each group should be
                 * 1. The path is the same
                 * 2. If the `order = 0`, re-calculate to be sure not the same ( Orders.SECURE+ )
                 * 3. Here are the set of Bolt
                 * -- Empty: return null and skip
                 * -- 1 size：return the single handler
                 * -- n size: return the handler collection of ChainAuthHandler ( all )
                 */
                this.mountAuthenticate(router, path, aegisSet);
                /*
                 * New design for 403 access issue here to implement RBAC mode
                 * This design is optional plugin into zero system, you can enable this feature.
                 * 403 access handler must be as following
                 * 1. The uri is the same as 401, it means that the request must be passed to 401 handler first
                 * 2. The order must be after 401 Orders.SECURE
                 */
                this.mountAuthorization(router, path, aegisSet);
            }
        });
    }

    private void mountAuthenticate(final Router router, final String path, final Set<Aegis> aegisSet) {
        final AuthenticationHandler resultHandler;
        if (VValue.ONE == aegisSet.size()) {
            // 1 = handler
            final Aegis aegis = aegisSet.iterator().next();
            resultHandler = this.bolt.authenticate(this.vertx, aegis);
        } else {
            // 1 < handler
            final ChainAuthHandler handler = ChainAuthHandler.all();
            aegisSet.stream()
                .map(item -> this.bolt.authenticate(this.vertx, item))
                .filter(Objects::nonNull)
                .forEach(handler::add);
            resultHandler = handler;
        }
        if (Objects.nonNull(resultHandler)) {
            router.route(path).order(KWeb.ORDER.SECURE)
                .handler(resultHandler)
                .failureHandler(AuthenticateEndurer.create());
        }
    }

    private void mountAuthorization(final Router router, final String path, final Set<Aegis> aegisSet) {
        final AuthorizationHandler resultHandler;
        if (VValue.ONE == aegisSet.size()) {
            // 1 = handler
            final Aegis aegis = aegisSet
                .iterator().next();
            resultHandler = this.bolt.authorization(this.vertx, aegis);
        } else {
            // 1 = handler ( sorted )
            final Aegis aegis = new TreeSet<>(Comparator.comparingInt(Aegis::getOrder))
                .iterator().next();
            resultHandler = this.bolt.authorization(this.vertx, aegis);
        }
        if (Objects.nonNull(resultHandler)) {
            router.route(path).order(KWeb.ORDER.SECURE_AUTHORIZATION)
                .handler(resultHandler)
                .failureHandler(AuthenticateEndurer.create());
        }
    }
}
