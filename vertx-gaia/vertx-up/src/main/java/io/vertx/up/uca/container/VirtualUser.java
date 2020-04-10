package io.vertx.up.uca.container;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.up.log.Annal;

/**
 * Talk holder for set default user.
 */
public class VirtualUser implements User {
    private static final Annal LOGGER = Annal.get(VirtualUser.class);

    private transient final JsonObject principal;

    public VirtualUser(final JsonObject principal) {
        this.principal = principal;
    }

    @Override
    public User isAuthorized(
            final String authority,
            final Handler<AsyncResult<Boolean>> resultHandler) {
        LOGGER.info(Info.VIRTUAL_USER, principal);
        return this;
    }

    @Override
    public User clearCache() {
        LOGGER.info(Info.VIRTUAL_USER, principal);
        return null;
    }

    @Override
    public JsonObject principal() {
        return principal;
    }

    @Override
    public void setAuthProvider(
            final AuthProvider authProvider) {
        LOGGER.info(Info.VIRTUAL_USER, principal);
    }
}
