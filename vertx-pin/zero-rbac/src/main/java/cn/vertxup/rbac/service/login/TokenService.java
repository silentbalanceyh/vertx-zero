package cn.vertxup.rbac.service.login;

import cn.vertxup.rbac.service.business.GroupStub;
import cn.vertxup.rbac.service.business.UserStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Session;
import io.vertx.mod.rbac.acl.relation.Junc;
import io.vertx.mod.rbac.atom.ScConfig;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.init.ScPin;
import io.vertx.up.atom.typed.UObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TokenService implements TokenStub {
    @Inject
    private transient CodeStub codeStub;
    @Inject
    private transient UserStub userStub;
    @Inject
    private transient GroupStub groupStub;

    @Override
    public Future<JsonObject> execute(final String clientId, final String code, final Session session) {
        return this.codeStub.verify(clientId, code)
            /* Fetch role keys */
            .compose(Junc.role()::identAsync)

            /* Build Data in Token */
            .compose(roles -> UObject.create()
                .append("user", clientId)
                /*
                 * Permission Pool is configured in RBAC module, it's different from Session here.
                 * The critical difference is that:
                 * 1) When user passed 401/403, the session id will be generated and session id
                 * will be changed.
                 * 2) When user logged successfully, `habitus` field will be stored into token
                 * instead of Http Session
                 * 3) Http Session is for each request instead of logged, but `habitus` means
                 * user's status after logged, and it will be tracked the session of `original`
                 * when user logged into the system.
                 * 4) Let's habitus length be 128 and it will be a key of logged user here.
                 * */
                .append(KName.HABITUS, Ut.randomString(128))
                /*
                 * Store session id instead of habitus in future
                 */
                .append("session", Objects.nonNull(session) ? session.id() : null)
                .append("role", roles).toFuture()
            )

            /* Whether enable group feature */
            .compose(this::fetchGroup);
    }

    private Future<JsonObject> fetchGroup(final JsonObject response) {
        /*
         * Extract configuration of groupSupport
         */
        final ScConfig config = ScPin.getConfig();
        if (config.getSupportGroup()) {

            /*
             * Extract clientId
             */
            final String userKey = response.getString("user");
            return Junc.group().identAsync(userKey)
                .compose(this::fetchRoles)
                .compose(groups -> UObject.create(response)
                    .append("group", groups).toFuture());
        } else {
            return Future.succeededFuture(response);
        }
    }

    private Future<JsonArray> fetchRoles(final JsonArray groups) {
        /* Future List */
        final List<Future<JsonObject>> futures = new ArrayList<>();
        groups.stream().filter(Objects::nonNull)
            .map(item -> (JsonObject) item)
            .forEach(item -> futures.add(this.groupStub.fetchRoleIdsAsync(item.getString(AuthKey.F_GROUP_ID))
                .compose(roles -> UObject.create(item).append("role", roles).toFuture())
            ));
        return Fn.combineA(futures);
    }
}
