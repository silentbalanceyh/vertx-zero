package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.business.UserStub;
import cn.vertxup.rbac.service.login.LoginStub;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.Trash;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Constants;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;
import java.util.Objects;

@Queue
public class UserActor {

    @Inject
    private transient UserStub stub;

    @Inject
    private transient LoginStub loginStub;

    /*
     * User information get from the system to extract data here.
     */
    @Address(Addr.User.INFORMATION)
    public Future<JsonObject> information(final Envelop envelop) {
        final String userId = envelop.jwt("user");
        /*
         * Async for user information
         */
        return this.stub.fetchEmployee(userId);
    }

    @Address(Addr.User.PASSWORD)
    public Future<JsonObject> password(final Envelop envelop) {
        /*
         * Async for user password / modification
         */
        final String userId = envelop.jwt("user");
        final JsonObject params = Ux.getJson(envelop);
        return this.stub.updateUser(userId, params);
    }

    @Address(Addr.User.PROFILE)
    public Future<JsonObject> profile(final Envelop envelop) {
        final String userId = envelop.jwt("user");
        final JsonObject params = Ux.getJson(envelop);
        return this.stub.updateEmployee(userId, params);
    }

    @Address(Addr.Auth.LOGOUT)
    public Future<Boolean> logout(final Envelop envelop) {
        final String token = envelop.jwt();
        final String habitus = envelop.jwt("habitus");
        return this.loginStub.logout(token, habitus).compose(result -> {
            /*
             * Here we should do
             * 1. Session / Context Purging
             * 2. User clean
             * 3. Fix issue of 3.9.1
             * 4. Permission Pool / Auth Pool Clean
             */
            final RoutingContext context = envelop.context();
            context.clearUser();

            final Session session = context.session();
            if (Objects.nonNull(session) && !session.isDestroyed()) {
                session.destroy();
            }

            final Vertx vertx = context.vertx();
            final Promise<Boolean> logout = Promise.promise();
            vertx.sharedData().<String, Boolean>getAsyncMap(Constants.DEFAULT_JWT_AUTH_POOL, res -> {
                if (res.succeeded()) {
                    res.result().remove(token, handler -> logout.complete(handler.result()));
                } else {
                    logout.complete(Boolean.FALSE);
                }
            });
            return logout.future();
        });
    }

    @Address(Addr.User.GET)
    public Future<JsonObject> getById(final String key) {
        return this.stub.fetchUser(key);
    }

    @Address(Addr.User.ADD)
    public Future<JsonObject> create(final JsonObject data) {
        return this.stub.createUser(data);
    }

    @Address(Addr.User.UPDATE)
    public Future<JsonObject> update(final String key, final JsonObject data) {
        return this.stub.updateUser(key, data);
    }

    @Address(Addr.User.DELETE)
    public Future<Boolean> delete(final String key) {
        return Ke.channelAsync(Trash.class, () -> this.stub.deleteUser(key),
            tunnel -> this.stub.fetchUser(key)
                .compose(user -> tunnel.backupAsync("sec.user", user))
                .compose(backup -> this.stub.deleteUser(key)));
    }
}
