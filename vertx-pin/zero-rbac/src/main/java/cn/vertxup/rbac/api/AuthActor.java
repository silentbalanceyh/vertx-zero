package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.login.AuthStub;
import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.refine.Sc;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.typed.UObject;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import jakarta.inject.Inject;

import java.util.Objects;

/*
 * Auth Actor
 */
@Queue
public class AuthActor {

    @Inject
    private transient AuthStub stub;

    @Address(Addr.Auth.LOGIN)
    public Future<JsonObject> login(final JsonObject user, final XHeader header) {
        final JsonObject params = user.copy();
        return Sc.imageVerify(header.session(), params, this.stub::login);
    }

    @Address(Addr.Auth.AUTHORIZE)
    public Future<JsonObject> authorize(final JsonObject data) {
        return this.stub.authorize(UObject.create(data).denull()
            .remove(AuthKey.RESPONSE_TYPE)
            .convert(AuthKey.CLIENT_ID, AuthKey.F_CLIENT_ID)
            .convert(AuthKey.CLIENT_SECRET, AuthKey.F_CLIENT_SECRET)
            .to());
    }

    @Address(Addr.Auth.TOKEN)
    public Future<JsonObject> token(final JsonObject data, final Session session) {
        return this.stub.token(data.copy(), session);
    }


    @Address(Addr.Auth.CAPTCHA_IMAGE_VERIFY)
    public Future<Boolean> imageVerity(final JsonObject request, final XHeader header) {
        Fn.out(Objects.isNull(header.session()), _501NotSupportException.class, this.getClass());
        return Sc.imageVerify(header.session(), request, (normalized) -> Ux.futureT());
    }

    /*
     * Default: 180 x 40
     */
    @Address(Addr.Auth.CAPTCHA_IMAGE)
    public Future<Buffer> generateImage(final XHeader header) {
        Fn.out(Objects.isNull(header.session()), _501NotSupportException.class, this.getClass());
        return Sc.imageOn(header.session(), 180, 40);
    }
}
