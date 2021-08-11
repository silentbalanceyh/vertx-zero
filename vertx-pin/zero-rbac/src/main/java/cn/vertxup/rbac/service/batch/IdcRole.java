package cn.vertxup.rbac.service.batch;

import cn.vertxup.rbac.domain.tables.daos.RUserRoleDao;
import cn.vertxup.rbac.domain.tables.daos.SRoleDao;
import cn.vertxup.rbac.domain.tables.pojos.RUserRole;
import cn.vertxup.rbac.domain.tables.pojos.SRole;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/*
 * Helper for `IdcService`
 */
class IdcRole {

    private final transient String sigma;

    private IdcRole(final String sigma) {
        this.sigma = sigma;
    }

    static IdcRole create(final String sigma) {
        return Fn.pool(Pool.ROLE, sigma, () -> new IdcRole(sigma));
    }

    Future<List<SRole>> fetchAsync() {
        return Ux.Jooq.on(SRoleDao.class).fetchAsync(KName.SIGMA, this.sigma);
    }

    /*
     * Get role map by `username` -> List<SRole>
     */
    ConcurrentMap<String, List<SRole>> toMap(final JsonArray users, final List<SRole> roles) {
        final ConcurrentMap<String, List<SRole>> roleMap = new ConcurrentHashMap<>();
        if (Objects.nonNull(users)) {
            /*
             * Grouped list by `key`
             */
            final ConcurrentMap<String, String> grouped = new ConcurrentHashMap<>();
            roles.stream().filter(Objects::nonNull).forEach(role -> grouped.put(role.getName(), role.getKey()));
            /*
             * Calculated for name = A,B,C
             */
            Ut.itJArray(users).forEach(user -> {
                final String roleNames = user.getString("roles");
                if (Ut.notNil(roleNames)) {
                    final List<String> validRoles = Arrays.stream(roleNames.split(","))
                            .map(String::trim)
                            .filter(Ut::notNil)
                            .filter(grouped::containsKey)
                            .collect(Collectors.toList());
                    final String username = user.getString(KName.USERNAME);
                    final List<SRole> roleList = roles.stream()
                            .filter(Objects::nonNull)
                            .filter(role -> validRoles.contains(role.getName()))
                            .collect(Collectors.toList());
                    roleMap.put(username, roleList);
                }
            });
        }
        return roleMap;
    }

    public Future<JsonArray> saveRel(final List<SUser> users, final ConcurrentMap<String, List<SRole>> roleMap) {
        if (users.isEmpty()) {
            return Ux.future(new JsonArray());
        } else {
            final Set<String> userKeys = users.stream().map(SUser::getKey).collect(Collectors.toSet());
            final JsonObject condition = new JsonObject();
            /*
             * Remove old relation ship between ( role - user )
             */
            condition.put(KName.USER_ID + ",i", Ut.toJArray(userKeys));
            return Ux.Jooq.on(RUserRoleDao.class).deleteByAsync(condition).compose(deleted -> {
                /*
                 * Build for each user
                 */
                final List<RUserRole> relationList = new ArrayList<>();
                users.forEach(user -> {
                    final List<SRole> roles = roleMap.get(user.getUsername());
                    Ut.itList(roles, (role, index) -> {
                        final RUserRole relation = new RUserRole();
                        relation.setRoleId(role.getKey());
                        relation.setUserId(user.getKey());
                        relation.setPriority(index);
                        relationList.add(relation);
                    });
                    /*
                     * Building relation ship
                     */
                    Sc.infoWeb(this.getClass(), "Will build username = {1}, role size = {0}",
                            String.valueOf(roles.size()), user.getUsername());
                });
                return Ux.Jooq.on(RUserRoleDao.class).insertAsync(relationList)
                        .compose(inserted -> Ux.future(users))
                        .compose(Ux::futureA);
            });
        }
    }
}
