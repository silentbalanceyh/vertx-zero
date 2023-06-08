package cn.vertxup.rbac.service.login;

import cn.vertxup.rbac.domain.tables.daos.OAccessTokenDao;
import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import cn.vertxup.rbac.service.business.UserStub;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.cv.AuthMsg;
import io.vertx.mod.rbac.error._401PasswordWrongException;
import io.vertx.mod.rbac.error._423UserDisabledException;
import io.vertx.mod.rbac.error._449UserNotFoundException;
import io.vertx.mod.rbac.logged.ScUser;
import io.vertx.mod.rbac.refine.Sc;
import io.vertx.up.atom.typed.UObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.util.Objects;

import static io.vertx.mod.rbac.refine.Sc.LOG;

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
                LOG.Auth.warn(LOGGER, AuthMsg.LOGIN_USER, username);
                return Fn.outWeb(_449UserNotFoundException.class, this.getClass(), username);
            }
            /* Locked User */
            final Boolean isLock = Objects.isNull(fetched.getActive()) ? Boolean.FALSE : fetched.getActive();
            if (!isLock) {
                LOG.Auth.warn(LOGGER, AuthMsg.LOGIN_LOCKED, username);
                return Fn.outWeb(_423UserDisabledException.class, this.getClass(), username);
            }
            /* Password Wrong */
            if (Objects.isNull(password) || !password.equals(fetched.getPassword())) {


                // Lock On when password invalid
                LOG.Auth.warn(LOGGER, AuthMsg.LOGIN_PWD, username);
                return Sc.lockOn(username)
                    .compose(nil -> Fn.outWeb(_401PasswordWrongException.class, this.getClass(), username));
            }


            // Lock Off when login successfully
            LOG.Auth.info(LOGGER, AuthMsg.LOGIN_SUCCESS, username);
            return Sc.lockOff(username).compose(nil -> Ux.future(fetched));
        }).compose(user -> this.userStub.fetchOUser(user.getKey()).compose(Ux::futureJ).compose(ouserJson -> {
            final JsonObject userJson = Ut.serializeJson(user);
            final JsonObject merged = Ut.valueAppend(userJson, ouserJson);
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
