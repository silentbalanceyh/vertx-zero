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
import io.vertx.up.unity.Ux;
import io.vertx.up.log.Annal;
import io.vertx.up.secure.Security;
import io.vertx.up.fn.Fn;

import javax.inject.Inject;

public class AuthService implements AuthStub {

    private static final Annal LOGGER = Annal.get(AuthService.class);
    @Inject
    private transient CodeStub codeStub;
    @Inject
    private transient LoginStub loginStub;
    @Inject
    private transient TokenStub tokenStub;
    @Inject
    private transient Security security;

    @Override
    @SuppressWarnings("all")
    public Future<JsonObject> authorize(final JsonObject filters) {
        Sc.infoAuth(LOGGER, AuthMsg.CODE_FILTER, filters.encode());
        return Ux.Jooq.on(OUserDao.class).<OUser>fetchOneAsync(filters).compose(item -> Fn.match(

                // Provide correct parameters, OUser record existing.
                () -> Fn.fork(() -> this.codeStub.authorize(item.getClientId())),

                // Could not identify OUser record, error throw.
                Fn.branch(null == item,
                        () -> Ux.thenError(_401CodeGenerationException.class, this.getClass(),
                                filters.getString(AuthKey.F_CLIENT_ID), filters.getString(AuthKey.F_CLIENT_SECRET)))
        ));
    }

    @Override
    public Future<JsonObject> token(final JsonObject params, final Session session) {
        final String code = params.getString(AuthKey.AUTH_CODE);
        final String clientId = params.getString(AuthKey.CLIENT_ID);
        Sc.infoAuth(LOGGER, AuthMsg.CODE_VERIFY, clientId, code);
        return tokenStub.execute(clientId, code, session)
                // Store token information
                .compose(security::store);
    }

    @Override
    public Future<JsonObject> login(final JsonObject params) {
        final String username = params.getString(AuthKey.USER_NAME);
        final String password = params.getString(AuthKey.PASSWORD);
        Sc.infoAuth(LOGGER, AuthMsg.LOGIN_INPUT, username);
        return loginStub.execute(username, password);
    }
}
