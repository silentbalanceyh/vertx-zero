package cn.vertxup.rbac.service.login;

import cn.vertxup.rbac.domain.tables.daos.OUserDao;
import cn.vertxup.rbac.domain.tables.pojos.OUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;
import io.vertx.tp.error._401CodeGenerationException;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.ZaaS;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;
import java.util.Objects;

public class AuthService implements AuthStub {

    private static final Annal LOGGER = Annal.get(AuthService.class);
    @Inject
    private transient CodeStub codeStub;
    @Inject
    private transient LoginStub loginStub;
    @Inject
    private transient TokenStub tokenStub;
    @Inject
    private transient ZaaS aaS;

    @Override
    @SuppressWarnings("all")
    public Future<JsonObject> authorize(final JsonObject filters) {
        Sc.infoAuth(LOGGER, AuthMsg.CODE_FILTER, filters.encode());
        return Ux.Jooq.on(OUserDao.class).<OUser>fetchOneAsync(filters).compose(item -> {
            if (Objects.isNull(item)) {
                // Could not identify OUser record, error throw.
                return Ux.thenError(_401CodeGenerationException.class, this.getClass(),
                    filters.getString(AuthKey.F_CLIENT_ID), filters.getString(AuthKey.F_CLIENT_SECRET));
            } else {
                // Provide correct parameters, OUser record existing.
                return this.codeStub.authorize(item.getClientId());
            }
        });
    }

    @Override
    public Future<JsonObject> token(final JsonObject params, final Session session) {
        final String code = params.getString(AuthKey.AUTH_CODE);
        final String clientId = params.getString(AuthKey.CLIENT_ID);
        Sc.infoAuth(LOGGER, AuthMsg.CODE_VERIFY, clientId, code);
        return this.tokenStub.execute(clientId, code, session)
            // Store token information
            .compose(this.aaS::store);
    }

    @Override
    public Future<JsonObject> login(final JsonObject params) {
        final String username = params.getString(AuthKey.USER_NAME);
        final String password = params.getString(AuthKey.PASSWORD);
        Sc.infoAuth(LOGGER, AuthMsg.LOGIN_INPUT, username);
        return this.loginStub.execute(username, password);
    }
}
