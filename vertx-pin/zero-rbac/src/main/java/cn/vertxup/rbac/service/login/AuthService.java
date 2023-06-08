package cn.vertxup.rbac.service.login;

import cn.vertxup.rbac.domain.tables.daos.OUserDao;
import cn.vertxup.rbac.domain.tables.pojos.OUser;
import cn.vertxup.rbac.service.jwt.JwtStub;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.cv.AuthMsg;
import io.vertx.mod.rbac.error._401CodeGenerationException;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import jakarta.inject.Inject;

import java.util.Objects;

import static io.vertx.mod.rbac.refine.Sc.LOG;

public class AuthService implements AuthStub {

    private static final Annal LOGGER = Annal.get(AuthService.class);
    @Inject
    private transient CodeStub codeStub;
    @Inject
    private transient LoginStub loginStub;
    @Inject
    private transient TokenStub tokenStub;
    @Inject
    private transient JwtStub jwtStub;

    @Override
    @SuppressWarnings("all")
    public Future<JsonObject> authorize(final JsonObject filters) {
        LOG.Auth.info(LOGGER, AuthMsg.CODE_FILTER, filters.encode());
        return Ux.Jooq.on(OUserDao.class).<OUser>fetchOneAsync(filters).compose(item -> {
            if (Objects.isNull(item)) {
                // Could not identify OUser record, error throw.
                return Fn.outWeb(_401CodeGenerationException.class, this.getClass(),
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
        LOG.Auth.info(LOGGER, AuthMsg.CODE_VERIFY, clientId, code);
        return this.tokenStub.execute(clientId, code, session).compose(data -> {
            // Store token information
            final String userKey = data.getString(KName.USER);
            LOG.Auth.info(LOGGER, AuthMsg.TOKEN_STORE, userKey);
            return this.jwtStub.store(userKey, data);
        });
    }


    @Override
    public Future<JsonObject> login(final JsonObject params) {
        final String username = params.getString(AuthKey.USER_NAME);
        final String password = params.getString(AuthKey.PASSWORD);
        LOG.Auth.info(LOGGER, AuthMsg.LOGIN_INPUT, username);
        return this.loginStub.execute(username, password);
    }
}
