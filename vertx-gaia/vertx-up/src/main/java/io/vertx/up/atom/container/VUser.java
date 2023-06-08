package io.vertx.up.atom.container;

import io.horizon.uca.log.Annal;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.Authorization;

/**
 * Talk holder for set default user.
 */
public class VUser implements User {
    private static final Annal LOGGER = Annal.get(VUser.class);

    private transient final JsonObject principal;

    public VUser(final JsonObject principal) {
        this.principal = principal;
    }

    @Override
    public User merge(final User user) {
        this.principal.mergeIn(user.principal());
        return this;
    }

    @Override
    public User isAuthorized(
        final String authority,
        final Handler<AsyncResult<Boolean>> resultHandler) {
        LOGGER.info(INFO.VIRTUAL_USER, this.principal);
        return this;
    }

    @Override
    public User clearCache() {
        LOGGER.info(INFO.VIRTUAL_USER, this.principal);
        return null;
    }

    @Override
    public JsonObject principal() {
        return this.principal;
    }

    @Override
    public void setAuthProvider(
        final AuthProvider authProvider) {
        LOGGER.info(INFO.VIRTUAL_USER, this.principal);
    }

    @Override
    public JsonObject attributes() {
        return null;
    }

    @Override
    public User isAuthorized(final Authorization authorization, final Handler<AsyncResult<Boolean>> handler) {
        return null;
    }
}
