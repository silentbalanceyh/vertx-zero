package io.vertx.up.secure.provider;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.atom.secure.Against;
import io.vertx.up.fn.Fn;

import java.lang.reflect.Method;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BasicAuth implements AuthenticationProvider {
    private transient final Aegis aegis;

    private BasicAuth(final Aegis aegis) {
        this.aegis = aegis;
    }

    public static AuthenticationProvider create(final Aegis aegis) {
        return Fn.poolThread(Pool.POOL_401, () -> new BasicAuth(aegis), BasicAuth.class.getName());
    }

    @Override
    public void authenticate(final JsonObject jsonObject, final Handler<AsyncResult<User>> handler) {
        final Against against = this.aegis.getAuthorizer();
        final Method method = against.getAuthenticate();

    }
}
