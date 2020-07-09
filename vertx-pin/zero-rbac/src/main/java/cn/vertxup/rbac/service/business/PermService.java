package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.SActionDao;
import cn.vertxup.rbac.domain.tables.daos.SPermissionDao;
import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SPermission;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.jq.UxJooq;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Future<JsonArray> syncPerm(final JsonArray permissions, final String group, final String sigma) {
        /*
         * Fetch all permissions from database and calculated removed list
         */
        final JsonObject condition = new JsonObject();
        condition.put(KeField.GROUP, group);
        condition.put(KeField.SIGMA, sigma);
        final UxJooq permDao = Ux.Jooq.on(SPermissionDao.class);
        return permDao.<SPermission>fetchAndAsync(condition).compose(existing -> {
            /*
             * Process filter to get removed
             */
            final List<SPermission> saved = Ux.fromJson(permissions, SPermission.class);
            final Set<String> keeped = saved.stream().map(SPermission::getKey).collect(Collectors.toSet());
            final String[] removedKeys = existing.stream()
                    .filter(item -> !keeped.contains(item.getKey()))
                    .map(SPermission::getKey).collect(Collectors.toList())
                    .toArray(new String[]{});
            return permDao.deleteByIdAsync(removedKeys).compose(nil -> {
                /*
                 * Save Action for SPermission by `key` only
                 */
                final List<Future<SPermission>> futures = new ArrayList<>();
                Ut.itList(saved).map(permission -> permDao.<SPermission>findByIdAsync(permission.getKey())
                        .compose(queired -> {
                            if (Objects.isNull(queired)) {
                                /*
                                 * Insert entity object into database
                                 */
                                return permDao.insertAsync(permission);
                            } else {
                                /*
                                 * Update the `key` hitted object into database
                                 */
                                return permDao.saveAsync(permission.getKey(), permission);
                            }
                        })).forEach(futures::add);
                return Ux.thenCombineT(futures).compose(Ux::fnJArray);
            });
        });
    }

    @Override
    public Future<JsonObject> savingPerm(final JsonArray removed, final JsonObject relation) {
        /*
         * Removed Permission Id from S_ACTION
         * Update all the action permissionId = null by key
         */
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
             * Build relation between actionId -> permissionId
             */
            final List<Future<SAction>> actionList = new ArrayList<>();
            Ut.<String>itJObject(relation, (permissionId, actionId) -> actionList.add(
                    jooq.<SAction>findByIdAsync(actionId).compose(action -> {
                        action.setPermissionId(permissionId);
                        return Ux.future(action);
                    }).compose(jooq::updateAsync)
            ));
            return Ux.thenCombineT(actionList);
        }).compose(nil -> Ux.future(relation));
    }
}
