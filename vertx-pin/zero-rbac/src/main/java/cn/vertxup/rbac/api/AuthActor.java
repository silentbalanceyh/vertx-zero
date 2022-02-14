package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.login.AuthStub;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.unity.UObject;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;

/*
 * Auth Actor
 */
@Queue
public class AuthActor {

    @Inject
    private transient AuthStub stub;

    @Address(Addr.Auth.LOGIN)
    public Future<JsonObject> login(final JsonObject user, final Session session) {
        final JsonObject params = user.copy();
        return Sc.imageVerify(session.id(), params, this.stub::login);
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
    public Future<Boolean> imageVerity(final JsonObject request, final Session session) {
        return Sc.imageVerify(session.id(), request, (normalized) -> Ux.futureT());
    }

    @Address(Addr.Auth.CAPTCHA_IMAGE)
    public Future<Buffer> generateImage(final Session session) {
        return Sc.imageOn(session.id());
    }
}
