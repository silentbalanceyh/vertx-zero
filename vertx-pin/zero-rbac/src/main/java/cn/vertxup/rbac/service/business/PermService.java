package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.RRolePermDao;
import cn.vertxup.rbac.domain.tables.daos.SActionDao;
import cn.vertxup.rbac.domain.tables.daos.SPermSetDao;
import cn.vertxup.rbac.domain.tables.daos.SPermissionDao;
import cn.vertxup.rbac.domain.tables.pojos.RRolePerm;
import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SPermSet;
import cn.vertxup.rbac.domain.tables.pojos.SPermission;
import cn.vertxup.rbac.service.accredit.ActionStub;
import io.horizon.eon.VString;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.logged.ScRole;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PermService implements PermStub {
    @Inject
    private transient ActionStub actionStub;

    @Override
    public Future<JsonObject> syncAsync(final JsonArray removed, final JsonObject relation,
                                        final String userKey) {
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

                /*
                 * Remove relation between
                 * Action / Permission
                 */
                action.setPermissionId(null);
                action.setUpdatedBy(userKey);
                action.setUpdatedAt(LocalDateTime.now());
                return Ux.future(action);
            })
            .compose(jooq::updateAsync)
        ).forEach(entities::add);
        return Fn.combineT(entities).compose(actions -> {

            /*
             * Build relation between actionId -> permissionId
             */
            final List<Future<SAction>> actionList = new ArrayList<>();
            Ut.<String>itJObject(relation, (permissionId, actionId) -> actionList.add(
                jooq.<SAction>fetchByIdAsync(actionId).compose(action -> {

                    /*
                     * Update relation between
                     * Action / Permission
                     */
                    action.setPermissionId(permissionId);
                    action.setUpdatedBy(userKey);
                    action.setUpdatedAt(LocalDateTime.now());
                    return Ux.future(action);
                }).compose(jooq::updateAsync)
            ));
            return Fn.combineT(actionList);
        }).compose(nil -> Ux.future(relation));
    }

    @Override
    public Future<JsonArray> syncAsync(final JsonArray permissions, final String roleId) {
        return Fn.runOr(Ux.futureA(), () -> {
            final JsonObject condition = new JsonObject();
            condition.put(KName.Rbac.ROLE_ID, roleId);
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
                    final ScRole role = ScRole.login(roleId);
                    return role.refresh(permissions).compose(nil -> Ux.future(inserted));
                }).compose(Ux::futureA);
            });
        }, roleId);
    }

    @Override
    public Future<JsonObject> searchAsync(final JsonObject query, final String sigma) {
        /*
         * Result for searching on S_PERMISSIONS
         */
        return Ux.Jooq.on(SPermSetDao.class).<SPermSet>fetchAsync(KName.SIGMA, sigma).compose(setList -> {
            /*
             * Extract perm codes to set
             */
            final Set<String> codes = setList.stream().map(SPermSet::getCode).collect(Collectors.toSet());

            /*
             * Search permissions that related current
             */
            final JsonObject criteriaRef = query.getJsonObject(Ir.KEY_CRITERIA);
            /*
             * Combine condition here
             */
            final JsonObject criteria = new JsonObject();
            criteria.put(KName.SIGMA, sigma);
            criteria.put("code,!i", Ut.toJArray(codes));
            criteria.put(VString.EMPTY, Boolean.TRUE);
            if (Ut.isNotNil(criteriaRef)) {
                criteria.put("$0", criteriaRef.copy());
            }
            /*
             * criteria ->
             * SIGMA = ??? AND CODE NOT IN (???)
             * */
            query.put(Ir.KEY_CRITERIA, criteria);

            /*
             * Replace for criteria
             */
            return Ux.Jooq.on(SPermissionDao.class).searchAsync(query);
        });
    }

    @Override
    public Future<JsonObject> fetchAsync(final String key) {

        /* Read permission and actions */
        return Ux.Jooq.on(SPermissionDao.class).<SPermission>fetchByIdAsync(key)

            /* Secondary Fetching, Fetch action here */
            .compose(permission -> this.actionStub.fetchAction(permission.getKey())

                /* futureJM to combine two result to JsonObject */
                .compose(Ux.futureJM(permission, KName.ACTIONS))
            );
    }

    @Override
    public Future<JsonObject> createAsync(final JsonObject body) {
        final JsonArray actions = body.getJsonArray(KName.ACTIONS);
        body.remove(KName.ACTIONS);
        return Ux.Jooq.on(SPermissionDao.class).<SPermission>insertAsync(body)

            /* Synced Action */
            .compose(permission -> this.actionStub.saveAction(permission, actions)

                /* futureJM to combine two result to JsonObject */
                .compose(Ux.futureJM(permission, KName.ACTIONS))
            );
    }

    @Override
    public Future<JsonObject> updateAsync(final String key, final JsonObject body) {
        final JsonArray actions = body.getJsonArray(KName.ACTIONS);
        body.remove(KName.ACTIONS);
        return Ux.Jooq.on(SPermissionDao.class).<SPermission, String>updateAsync(key, body)

            /* Synced Action */
            .compose(permission -> this.actionStub.saveAction(permission, actions)

                /* futureJM to combine two result to JsonObject */
                .compose(Ux.futureJM(permission, KName.ACTIONS))
            );
    }

    @Override
    public Future<Boolean> deleteAsync(final String key, final String userKey) {
        return Ux.Jooq.on(SPermissionDao.class).deleteByIdAsync(key)
            .compose(nil -> this.actionStub.removeAction(key, userKey));
    }
}
