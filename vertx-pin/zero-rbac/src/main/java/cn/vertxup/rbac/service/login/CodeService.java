package cn.vertxup.rbac.service.login;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.error._401CodeExpiredException;
import io.vertx.mod.rbac.error._401CodeWrongException;
import io.vertx.mod.rbac.refine.Sc;
import io.vertx.up.atom.typed.UObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.util.Objects;

public class CodeService implements CodeStub {

    @Override
    public Future<JsonObject> authorize(final String clientId) {
        // Generate random authorization code
        final String authCode = Sc.valueCode();

        // Whether existing state
        final JsonObject response = new JsonObject();
        // Enable SharedClient to store authCode
        return Sc.cacheCode(clientId, authCode)
            .compose(item -> UObject.create(response)
                .append(AuthKey.AUTH_CODE, item)
                .toFuture());
    }

    @Override
    @SuppressWarnings("all")
    public Future<String> verify(final String clientId, final String code) {
        // Shared code in pool here to get code
        return Sc.cacheCode(clientId).compose(item -> {
            if (Objects.isNull(item)) {
                // 401: Authorization Code Expired, The item is null, it means that code is expired
                return Fn.outWeb(_401CodeExpiredException.class, this.getClass(), clientId, code);
            }
            if (!code.equals(item)) {
                // 401: Wrong code provided ( Api Client )
                return Fn.outWeb(_401CodeWrongException.class, this.getClass(), code);
            }
            // Successfully
            return Ux.future(clientId);
        });
    }
}
