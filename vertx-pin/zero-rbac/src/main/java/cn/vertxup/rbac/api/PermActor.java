package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.daos.RRolePermDao;
import cn.vertxup.rbac.service.business.PermStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Queue
public class PermActor {

    @Inject
    private transient PermStub stub;

    /*
     * Calculate Permission Groups
     */
    @Address(Addr.Authority.PERMISSION_GROUP)
    public Future<JsonArray> calculate(final XHeader header) {
        return this.stub.groupAsync(header.getSigma());
    }

    @Address(Addr.Authority.PERMISSION_SAVING)
    public Future<JsonObject> savingPerm(final JsonObject processed,
                                         final XHeader header) {
        final String sigma = header.getSigma();
        Sc.infoWeb(this.getClass(), "Permission Update: {0}, sigma = {1}",
                processed.encode(), sigma);
        /*
         * Two steps, the input json is:
         * Action Part:
         * {
         *     "removed": [
         *         "action1",
         *         "action2"
         *     ],
         *     "relation":{
         *         "action3": "permission1",
         *         "action4": "permission2"
         *     }
         * }
         * Permission Part:
         * {
         *     "group": "xxxx",
         *     "data": [
         *     ]
         * }
         *
         * The steps is as following:
         *
         * 1. Permission Sync ( Removed, Add New, Update )
         * 2. Action Sync ( Add New, Relation Processing )
         */
        // Permission Data
        final JsonArray permissions = Ut.sureJArray(processed.getJsonArray(KeField.DATA));
        final String group = processed.getString("group");
        // Action Data
        final JsonArray removed = Ut.sureJArray(processed.getJsonArray("removed"));
        final JsonObject relation = Ut.sureJObject(processed.getJsonObject("relation"));

        return this.stub.syncPerm(permissions, group, sigma)                // Permission Process
                .compose(nil -> this.stub.savingPerm(removed, relation))    // Action Process
                .compose(nil -> Ux.future(relation));
    }

    @Address(Addr.Authority.PERMISSION_BY_ROLE)
    public Future<JsonArray> fetchAsync(final String roleId) {
        if (Ut.notNil(roleId)) {
            return Ux.Jooq.on(RRolePermDao.class)
                    .fetchAsync(KeField.ROLE_ID, roleId)
                    .compose(Ux::fnJArray);
        } else return Ux.futureJArray();
    }
}
