package cn.vertxup.rbac.wall.zaas;

import cn.vertxup.rbac.service.accredit.AccreditStub;
import cn.vertxup.rbac.service.jwt.JwtStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.annotations.Aas;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.ZaaS;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Aas
public class CommonZaaS implements ZaaS {

    private static final Annal LOGGER = Annal.get(CommonZaaS.class);
    @Inject
    private transient JwtStub jwtStub;
    @Inject
    private transient AccreditStub accredit;

    @Override
    public Future<JsonObject> store(final JsonObject data) {
        final String userKey = data.getString("user");
        Sc.infoAuth(LOGGER, AuthMsg.TOKEN_STORE, userKey);
        return this.jwtStub.store(userKey, data);
    }

    @Override
    public Future<Boolean> verify(final JsonObject data) {
        final String token = data.getString("jwt");
        final JsonObject extracted = Ux.Jwt.extract(data);
        Sc.infoAuth(LOGGER, AuthMsg.TOKEN_INPUT, token, extracted.encode());
        return this.jwtStub.verify(extracted.getString("user"), token);
    }

    @Override
    public Future<Boolean> access(final JsonObject data) {
        /*
         * User defined accessor
         */
        return this.accredit.authorize(data);
    }
}
