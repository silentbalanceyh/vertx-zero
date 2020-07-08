package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.SActionDao;
import cn.vertxup.rbac.domain.tables.daos.SPermissionDao;
import cn.vertxup.rbac.domain.tables.pojos.SAction;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.jq.UxJooq;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class PermService implements PermStub {
    @Override
    public Future<JsonArray> groupAsync(final String sigma) {
        /*
         * Build condition of `sigma`
         */
        final JsonObject condition = new JsonObject();
        condition.put(KeField.SIGMA, sigma);
        /*
         * Permission Groups processing
         */
        return Ux.Jooq.on(SPermissionDao.class)
                .groupAsync(condition, "group", "identifier");
    }

    @Override
    public Future<JsonObject> savingPerm(final JsonObject processed, final String sigma) {
        /*
         * Removed Permission Id from S_ACTION
         * Update all the action permissionId = null by key
         */
        final JsonArray removed = Ut.sureJArray(processed.getJsonArray("removed"));
        final List<Future<SAction>> entities = new ArrayList<>();
        final UxJooq jooq = Ux.Jooq.on(SActionDao.class);
        Ut.itJString(removed).map(key -> jooq.<SAction>findByIdAsync(key)

                /*
                 * Set all queried permissionId of each action to null
                 * Here should remove permissionId to set resource to freedom
                 */
                .compose(action -> {
                    action.setPermissionId(null);
                    return Ux.future(action);
                })
                .compose(jooq::updateAsync)
        ).forEach(entities::add);
        return Ux.thenCombineT(entities).compose(actions -> {

            /*
             * Get data from processed
             */
            final JsonObject data = Ut.sureJObject(processed.getJsonObject(KeField.DATA));

            /*
             * Build relation between actionId -> permissionId
             */
            final List<Future<SAction>> actionList = new ArrayList<>();
            Ut.<String>itJObject(data, (permissionId, actionId) -> actionList.add(
                    jooq.<SAction>findByIdAsync(actionId).compose(action -> {
                        action.setPermissionId(permissionId);
                        return Ux.future(action);
                    }).compose(jooq::updateAsync)
            ));
            return Ux.thenCombineT(actionList).compose(nil -> Ux.future(data));
        });
    }
}
