package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.RRolePermDao;
import cn.vertxup.rbac.domain.tables.daos.SPermSetDao;
import cn.vertxup.rbac.domain.tables.daos.SPermissionDao;
import cn.vertxup.rbac.domain.tables.pojos.RRolePerm;
import cn.vertxup.rbac.domain.tables.pojos.SPermSet;
import cn.vertxup.rbac.domain.tables.pojos.SPermission;
import io.horizon.atom.common.Refer;
import io.horizon.eon.VString;
import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RightsService implements RightsStub {
    @Override
    public Future<JsonArray> saveRoles(final String roleId, final JsonArray data) {
        // 1. make up role-perm entity
        final List<RRolePerm> rolePerms = Ut.itJString(data)
            .filter(Ut::isNotNil)
            .map(perm -> new JsonObject().put(KName.Rbac.PERM_ID, perm).put(KName.Rbac.ROLE_ID, roleId))
            .map(rolePerm -> Ux.fromJson(rolePerm, RRolePerm.class))
            .collect(Collectors.toList());
        // 2. delete old ones and insert new ones
        return this.removeRoles(roleId)
            .compose(result -> Ux.Jooq.on(RRolePermDao.class)
                .insertAsync(rolePerms)
                .compose(Ux::futureA)
            );
    }

    @Override
    public Future<Boolean> removeRoles(final String roleId) {
        return Ux.Jooq.on(RRolePermDao.class)
            .deleteByAsync(new JsonObject().put(KName.Rbac.ROLE_ID, roleId));
    }

    @Override
    public Future<JsonArray> fetchAsync(final String sigma) {
        /*
         * Build condition of `sigma`
         */
        final JsonObject condition = new JsonObject();
        condition.put(KName.SIGMA, sigma);
        /*
         * Permission Groups processing
         *「Upgrade」
         * Old method because `GROUP` is in S_PERMISSION
         * return Ux.Jooq.on(SPermissionDao.class).countByAsync(condition, "group", "identifier");
         * New version: S_PERM_SET processing
         */
        return Ux.Jooq.on(SPermSetDao.class).fetchJAsync(condition);
    }

    /*
     * Input data format for `PERM_SET` and `PERMISSION`
     *
     * 1) Permission combine ( key as unique )
     * 2) PermSet combine ( name, code as unique )
     *
     */
    @Override
    public Future<JsonArray> saveDefinition(final JsonArray permissions, final SPermSet permSet) {
        final String sigma = permSet.getSigma();

        /*
         * 1) Permission Compared ( Update / Add )
         * -- Combined for final on PERM_SET
         */
        final Refer permSetRef = new Refer();
        final Refer mapRef = new Refer();
        return this.calcPermission(permissions, sigma).compose(mapRef::future).compose(compared -> {
            /*
             * ADD / UPDATE ( Processing )
             */
            final List<Future<List<SPermission>>> combined = new ArrayList<>();
            final UxJooq jooq = Ux.Jooq.on(SPermissionDao.class);

            /*

             * ADD / UPDATE evaluation
             */
            combined.add(jooq.insertAsync(compared.get(ChangeFlag.ADD)));
            combined.add(jooq.updateAsync(compared.get(ChangeFlag.UPDATE)));
            return Fn.compressL(combined).compose(processed ->
                /*
                 * Codes here for future usage
                 */
                permSetRef.future(processed.stream().map(SPermission::getCode).collect(Collectors.toSet()))
            );
        }).compose(nil -> {

            /*
             * 2) Perm Set fetching here for original
             */
            final JsonObject criteria = new JsonObject();
            criteria.put(KName.SIGMA, sigma);
            criteria.put(KName.NAME, permSet.getName());
            return Ux.Jooq.on(SPermSetDao.class).<SPermSet>fetchAndAsync(criteria);
        }).compose(originalSet -> {
            /*
             * 3) Processing for ( ADD, UPDATE ) records of Perm Set
             */
            final Set<String> current = permSetRef.get();
            final Set<String> original = originalSet.stream().map(SPermSet::getCode).collect(Collectors.toSet());
            /*
             * Add New ( current - original )
             * Update ( current & original )
             */
            final Set<String> added = Ut.elementDiff(current, original);
            final Set<String> updated = Ut.elementIntersect(current, original);
            /*
             * Future queue for ( ADD / UPDATE )
             */
            final List<Future<List<SPermSet>>> futures = new ArrayList<>();
            futures.add(this.insertPerm(permSet, added));
            futures.add(this.updatePerm(permSet, originalSet, updated));

            final ConcurrentMap<ChangeFlag, List<SPermission>> map = mapRef.get();
            final List<SPermission> deleted = map.get(ChangeFlag.DELETE);
            futures.add(this.deletePerm(permSet, deleted));

            return Fn.compressL(futures);
        }).compose(Ux::futureA);
    }

    // ======================= Basic Three Method =============================
    private Future<List<SPermSet>> deletePerm(final SPermSet permSet, final List<SPermission> permissions) {
        final JsonObject criteria = new JsonObject();
        criteria.put(KName.SIGMA, permSet.getSigma());
        criteria.put(KName.NAME, permSet.getName());
        criteria.put("code,i", Ut.toJArray(
            permissions.stream().map(SPermission::getCode).collect(Collectors.toSet())
        ));
        criteria.put(VString.EMPTY, Boolean.TRUE);
        return Ux.Jooq.on(SPermSetDao.class).deleteByAsync(criteria).compose(nil -> Ux.future(new ArrayList<>()));
    }

    private Future<List<SPermSet>> updatePerm(final SPermSet permSet, final List<SPermSet> permSetList,
                                              final Set<String> codes) {
        permSetList.forEach(each -> {
            each.setUpdatedBy(permSet.getUpdatedBy());
            each.setUpdatedAt(permSet.getUpdatedAt());
        });
        return Ux.Jooq.on(SPermSetDao.class).updateAsync(permSetList.stream()
            .filter(item -> codes.contains(item.getCode()))
            .collect(Collectors.toList())
        );
    }

    private Future<List<SPermSet>> insertPerm(final SPermSet permSet, final Set<String> codes) {
        final List<SPermSet> list = new ArrayList<>();
        codes.forEach(each -> {
            final SPermSet inserted = new SPermSet();
            inserted.setKey(UUID.randomUUID().toString());
            inserted.setCode(each);
            inserted.setType(permSet.getType());
            /*
             * Copy
             */
            inserted.setName(permSet.getName());
            inserted.setActive(Boolean.TRUE);
            inserted.setSigma(permSet.getSigma());
            inserted.setLanguage(permSet.getLanguage());
            inserted.setCreatedAt(LocalDateTime.now());
            inserted.setCreatedBy(permSet.getUpdatedBy());
            list.add(inserted);
        });
        return Ux.Jooq.on(SPermSetDao.class).insertAsync(list);
    }

    /*
     * 1. Fetch all original permissions first, extract data part from json `data` node for all permissions
     *    ( unique condition: code + sigma )
     * 2. Compare original permissions and latest permissions for
     * -- ADD
     * -- UPDATE
     * -- DELETE ( No Permission because of condition, code in here )
     */
    private Future<ConcurrentMap<ChangeFlag, List<SPermission>>> calcPermission(final JsonArray permissions, final String sigma) {
        final Set<String> permCodes = Ut.valueSetString(permissions, KName.CODE);
        final JsonObject criteria = new JsonObject();
        criteria.put(KName.SIGMA, sigma);
        criteria.put("code,i", Ut.toJArray(permCodes));
        return Ux.Jooq.on(SPermissionDao.class).<SPermission>fetchAndAsync(criteria).compose(original -> {
            final List<SPermission> current = Ux.fromJson(permissions, SPermission.class);
            /*
             * Compared for three types
             * 1) Old List - original
             * 2) New List - current
             */
            return Ux.future(Ux.compare(original, current, SPermission::getKey));
        });
    }
}
