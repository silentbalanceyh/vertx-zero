package io.vertx.up.secure.authenticate;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BasicAuth implements AuthenticationProvider {
    private transient final Aegis aegis;

    private BasicAuth(final Aegis aegis) {
        this.aegis = aegis;
    }

    public static AuthenticationProvider provider(final Aegis aegis) {
        return Fn.poolThread(Pool.POOL_401, () -> new BasicAuth(aegis), BasicAuth.class.getName());
    }

    @Override
    public void authenticate(final JsonObject credentials, final Handler<AsyncResult<User>> resultHandler) {
        resultHandler.handle(Future.succeededFuture(User.create(credentials)));
    }
}
