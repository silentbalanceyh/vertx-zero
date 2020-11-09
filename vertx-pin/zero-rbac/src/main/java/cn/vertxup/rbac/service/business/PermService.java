package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.RRolePermDao;
import cn.vertxup.rbac.domain.tables.daos.SActionDao;
import cn.vertxup.rbac.domain.tables.daos.SPermSetDao;
import cn.vertxup.rbac.domain.tables.daos.SPermissionDao;
import cn.vertxup.rbac.domain.tables.pojos.RRolePerm;
import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SPermSet;
import cn.vertxup.rbac.domain.tables.pojos.SPermission;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
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
         *「Upgrade」
         * Old method because `GROUP` is in S_PERMISSION
         * return Ux.Jooq.on(SPermissionDao.class).countByAsync(condition, "group", "identifier");
         * New version: S_PERM_SET processing
         */
        return Ux.Jooq.on(SPermSetDao.class).fetchJAsync(condition);
    }

    @Override
    public Future<JsonObject> savingPerm(final JsonArray removed, final JsonObject relation) {
        /*
         * Removed Permission Id from S_ACTION
         * Update all the action permissionId = null by key
         */
        final List<Future<SAction>> entities = new ArrayList<>();
        final UxJooq jooq = Ux.Jooq.on(SActionDao.class);
        Ut.itJString(removed).map(key -> jooq.<SAction>fetchByIdAsync(key)
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
                    jooq.<SAction>fetchByIdAsync(actionId).compose(action -> {
                        action.setPermissionId(permissionId);
                        return Ux.future(action);
                    }).compose(jooq::updateAsync)
            ));
            return Ux.thenCombineT(actionList);
        }).compose(nil -> Ux.future(relation));
    }

    @Override
    public Future<JsonArray> syncPerm(final JsonArray permissions, final String group, final String sigma) {
        /*
         * 1. permissions ->
         * -- ADD = List
         * -- UPDATE = List
         * -- DELETE = List
         */
        final Set<String> permCodes = Ut.mapString(permissions, KeField.CODE);
        final JsonObject condition = new JsonObject();
        condition.put(KeField.SIGMA, sigma);
        return null;
    }

    @Override
    public Future<JsonArray> syncPerm(final JsonArray permissions, final String roleId) {
        return Fn.getEmpty(Ux.futureA(), () -> {
            final JsonObject condition = new JsonObject();
            condition.put(KeField.ROLE_ID, roleId);
            /*
             * Delete all the relations that belong to roleId
             * that the user provided here
             * */
            final UxJooq dao = Ux.Jooq.on(RRolePermDao.class);
            return dao.deleteByAsync(condition).compose(processed -> {
                /*
                 * Build new relations that belong to the role
                 */
                final List<RRolePerm> relations = new ArrayList<>();
                Ut.itJArray(permissions, String.class, (permissionId, index) -> {
                    final RRolePerm item = new RRolePerm();
                    item.setRoleId(roleId);
                    item.setPermId(permissionId);
                    relations.add(item);
                });
                return dao.insertAsync(relations).compose(inserted -> {
                    /*
                     * Refresh cache pool with Sc interface directly
                     */
                    return Sc.cachePermission(roleId, permissions)
                            .compose(nil -> Ux.future(inserted));
                }).compose(Ux::futureA);
            });
        }, roleId);
    }

    @Override
    public Future<JsonObject> searchUnReady(final JsonObject query, final String sigma) {
        /*
         * Result for searching on S_PERMISSIONS
         */
        return Ux.Jooq.on(SPermSetDao.class).<SPermSet>fetchAsync(KeField.SIGMA, sigma).compose(setList -> {
            /*
             * Extract perm codes to set
             */
            final Set<String> codes = setList.stream().map(SPermSet::getCode).collect(Collectors.toSet());

            /*
             * Search permissions that related current
             */
            final JsonObject criteriaRef = query.getJsonObject(Inquiry.KEY_CRITERIA);
            /*
             * Combine condition here
             */
            final JsonObject criteria = new JsonObject();
            criteria.put(KeField.SIGMA, sigma);
            criteria.put("code,!i", Ut.toJArray(codes));
            criteria.put(Strings.EMPTY, Boolean.TRUE);
            if (Ut.notNil(criteriaRef)) {
                criteria.put("$0", criteriaRef.copy());
            }
            /*
             * criteria ->
             * SIGMA = ??? AND CODE NOT IN (???)
             * */
            query.put(Inquiry.KEY_CRITERIA, criteria);

            /*
             * Replace for criteria
             */
            return Ux.Jooq.on(SPermissionDao.class).searchAsync(query);
        });
    }

    // =============================== Private Method for Permissions ===============================
    private Future<Boolean> removeAsync(final List<String> removedKeys, final String sigma) {
        /*
         * 1. Remove permission records
         */
        final Future<Boolean> removedFuture;
        if (removedKeys.isEmpty()) {
            return Ux.future(Boolean.TRUE);
        } else {
            final String[] ids = removedKeys.toArray(new String[]{});
            return Ux.Jooq.on(SPermissionDao.class).deleteByIdAsync(ids).compose(permTrue -> {
                /*
                 * 2. Remove permission role records
                 */
                final JsonObject condition = new JsonObject();
                condition.put("permId,i", Ut.toJArray(removedKeys));
                return Ux.Jooq.on(RRolePermDao.class).deleteByAsync(condition).compose(relationTrue -> {
                    /*
                     * 3. Update all SAction of permission that set to null
                     */
                    final JsonObject condAction = new JsonObject();
                    condAction.put("permissionId,i", Ut.toJArray(removedKeys));
                    condAction.put(KeField.SIGMA, sigma);
                    final UxJooq actionDao = Ux.Jooq.on(SActionDao.class);
                    return actionDao.<SAction>fetchAndAsync(condAction).compose(actions -> {
                        actions.forEach(action -> action.setPermissionId(null));
                        return actionDao.updateAsync(actions);
                    });
                });
            }).compose(nil -> Ux.future(Boolean.TRUE));
        }
    }

    /*
     * 1. Fetch all original permissions first, extract data part from json `data` node for all permissions
     *    ( unique condition: code + sigma )
     * 2. Compare original permissions and latest permissions for
     * -- ADD
     * -- UPDATE
     * -- DELETE
     */
    private Future<ConcurrentMap<ChangeFlag, List<SPermission>>> fetchPermissions(final JsonArray permissions, final String sigma) {
        final Set<String> permCodes = Ut.mapString(permissions, KeField.CODE);
        final JsonObject criteria = new JsonObject();
        criteria.put(KeField.SIGMA, sigma);
        criteria.put("code,i", Ut.toJArray(permCodes));
        return Ux.Jooq.on(SPermissionDao.class).<SPermission>fetchAndAsync(criteria).compose(original -> {
            final List<SPermission> current = Ux.fromJson(permissions, SPermission.class);
            return null;
        });
    }
}
