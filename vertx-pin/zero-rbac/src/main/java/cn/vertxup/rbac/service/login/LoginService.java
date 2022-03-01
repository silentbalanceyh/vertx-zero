package cn.vertxup.rbac.service.login;

import cn.vertxup.rbac.domain.tables.daos.OAccessTokenDao;
import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import cn.vertxup.rbac.service.business.UserStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._401PasswordWrongException;
import io.vertx.tp.error._423UserDisabledException;
import io.vertx.tp.error._449UserNotFoundException;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.logged.ScUser;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.unity.UObject;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;

public class LoginService implements LoginStub {

    private static final Annal LOGGER = Annal.get(LoginService.class);
    @Inject
    private transient UserStub userStub;

    @Override
    @SuppressWarnings("all")
    public Future<JsonObject> execute(final String username, final String password) {
        /* Find the user record with username */
        return Sc.lockVerify(username, () -> Ux.Jooq.on(SUserDao.class).<SUser>fetchOneAsync(AuthKey.USER_NAME, username).compose(fetched -> {
            /* Not Found */
            if (Objects.isNull(fetched)) {
                Sc.warnAuth(LOGGER, AuthMsg.LOGIN_USER, username);
                return Ux.thenError(_449UserNotFoundException.class, this.getClass(), username);
            }
            /* Locked User */
            final Boolean isLock = Objects.isNull(fetched.getActive()) ? Boolean.FALSE : fetched.getActive();
            if (!isLock) {
                Sc.warnAuth(LOGGER, AuthMsg.LOGIN_LOCKED, username);
                return Ux.thenError(_423UserDisabledException.class, this.getClass(), username);
            }
            /* Password Wrong */
            if (Objects.isNull(password) || !password.equals(fetched.getPassword())) {


                // Lock On when password invalid
                Sc.warnAuth(LOGGER, AuthMsg.LOGIN_PWD, username);
                return Sc.lockOn(username)
                    .compose(nil -> Ux.thenError(_401PasswordWrongException.class, this.getClass(), username));
            }


            // Lock Off when login successfully
            Sc.infoAudit(LOGGER, AuthMsg.LOGIN_SUCCESS, username);
            return Sc.lockOff(username).compose(nil -> Ux.future(fetched));
        }).compose(user -> this.userStub.fetchOUser(user.getKey()).compose(Ux::futureJ).compose(ouserJson -> {
            final JsonObject userJson = Ut.serializeJson(user);
            final JsonObject merged = Ut.elementAppend(userJson, ouserJson);
            return UObject.create(merged).pickup(
                KName.KEY,                /* client_id parameter */
                AuthKey.SCOPE,              /* scope parameter */
                AuthKey.STATE,              /* state parameter */
                AuthKey.F_CLIENT_SECRET,    /* client_secret parameter */
                AuthKey.F_GRANT_TYPE        /* grant_type parameter */
            ).denull().toFuture();
        }).compose(response -> {
            final String initPwd = Sc.valuePassword();
            if (initPwd.equals(user.getPassword())) {
                /* Password Init */
                response.put(KName.PASSWORD, false);
            }
            return Ux.future(response);
        })));
    }

    @Override
    public Future<Boolean> logout(final String token, final String habitus) {
        /*
         * Delete Token from `ACCESS_TOKEN`
         */
        return Ux.Jooq.on(OAccessTokenDao.class)
            .deleteByAsync(new JsonObject().put("token", token))
            .compose(nil -> ScUser.logout(habitus));
    }
}
