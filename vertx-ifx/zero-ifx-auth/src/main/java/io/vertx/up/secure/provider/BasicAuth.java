package io.vertx.up.secure.provider;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BasicAuth implements AuthenticationProvider {
    @Override
    public void authenticate(final JsonObject jsonObject, final Handler<AsyncResult<User>> handler) {
        System.out.println(jsonObject);
    }
}
