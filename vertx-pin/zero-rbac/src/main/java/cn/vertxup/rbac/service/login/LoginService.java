package cn.vertxup.rbac.service.login;

import cn.vertxup.rbac.domain.tables.daos.OAccessTokenDao;
import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import cn.vertxup.rbac.service.business.UserStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._401PasswordWrongException;
import io.vertx.tp.error._449UserNotFoundException;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.permission.ScPrivilege;
import io.vertx.up.atom.unity.Uson;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Objects;

public class LoginService implements LoginStub {

    @Inject
    private transient UserStub userStub;

    @Override
    @SuppressWarnings("all")
    public Future<JsonObject> execute(final String username, final String password) {
        return Ux.Jooq.on(SUserDao.class)
                /* Find the user record with username */
                .<SUser>fetchOneAsync(AuthKey.USER_NAME, username)
                .compose(pojo -> Fn.match(() -> Fn.fork(
                        // Login successfully
                        () -> Ux.log(this.getClass()).on(AuthMsg.LOGIN_SUCCESS).info(username),
                        () -> Ux.future(pojo)),
                        // User Not Found
                        Fn.branch(null == pojo,
                                () -> Ux.log(this.getClass()).on(AuthMsg.LOGIN_USER).info(username),
                                () -> Ux.thenError(_449UserNotFoundException.class, this.getClass(), username)
                        ),
                        // Password Wrong
                        Fn.branch(null != pojo && (Objects.isNull(password) ||
                                        !password.equals(pojo.getPassword())),
                                () -> Ux.log(this.getClass()).on(AuthMsg.LOGIN_PWD).info(username, password),
                                () -> Ux.thenError(_401PasswordWrongException.class, this.getClass(), username))
                ))
                .compose(Ux::futureJ)
                .compose(item -> Ux.thenCombine(item,
                        // Secondar query
                        (user) -> Arrays.asList(
                                // Fetch Oauth user
                                this.userStub.fetchOUser(user.getString(KeField.KEY))
                        ),
                        Ut::ifMerge // SUser / OUser - Avoid duplicated merging
                ))
                .compose(item -> Uson.create(item).pickup(
                        KeField.KEY,                /* client_id parameter */
                        AuthKey.SCOPE,              /* scope parameter */
                        AuthKey.STATE,              /* state parameter */
                        AuthKey.F_CLIENT_SECRET,    /* client_secret parameter */
                        AuthKey.F_GRANT_TYPE        /* grant_type parameter */
                ).denull().toFuture());
    }

    @Override
    public Future<Boolean> logout(final String token, final String habitus) {
        /*
         * Delete Token from `ACCESS_TOKEN`
         */
        return Ux.Jooq.on(OAccessTokenDao.class)
                .deleteAsync(new JsonObject().put("token", token))
                .compose(nil -> ScPrivilege.open(habitus))
                .compose(ScPrivilege::clear);
    }
}
