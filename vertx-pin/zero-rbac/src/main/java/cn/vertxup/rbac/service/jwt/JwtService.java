package cn.vertxup.rbac.service.jwt;

import cn.vertxup.rbac.domain.tables.daos.OAccessTokenDao;
import cn.vertxup.rbac.domain.tables.pojos.OAccessToken;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScSession;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;

/*
 * Jwt Token Service for:
 * 1) Stored token information into jwt token
 * 2) Verify token based on stored access_token in database.
 */
public class JwtService implements JwtStub {
    @Override
    public Future<JsonObject> store(final String userKey, final JsonObject data) {
        /*
         * Jwt Token response building
         */
        final JsonObject response = Sc.jwtToken(data);
        /*
         * Jwt OAccessToken
         */
        final OAccessToken accessToken = Sc.jwtToken(response, userKey);
        return Ux.Jooq.on(OAccessTokenDao.class)
                .insertAsync(accessToken)
                .compose(item -> ScSession.initAuthorization(data))
                .compose(initialized -> Ux.future(response));
    }

    @Override
    public Future<Boolean> verify(final String userKey, final String token) {
        return Ux.Jooq.on(OAccessTokenDao.class)
                .<OAccessToken>fetchAsync("token", token.getBytes(Values.DEFAULT_CHARSET))
                .compose(tokens -> Sc.jwtToken(tokens, userKey));
    }
}
