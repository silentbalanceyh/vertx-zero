package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.SPermSetDao;
import cn.vertxup.rbac.domain.tables.daos.SPermissionDao;
import cn.vertxup.rbac.domain.tables.pojos.SPermSet;
import cn.vertxup.rbac.domain.tables.pojos.SPermission;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.em.ChangeFlag;
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
public class PermGService implements PermGStub {
    @Override
    public Future<JsonArray> fetchAsync(final String sigma) {
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

    /*
     * Input data format for `PERM_SET` and `PERMISSION`
     *
     * 1) Permission combine ( key as unique )
     * 2) PermSet combine ( name, code as unique )
     */
    @Override
    public Future<JsonArray> saveDefinition(final JsonArray permissions, final String group,
                                            final String sigma, final String userKey) {
        return this.calcPermission(permissions, sigma).compose(compared -> {
            /*
             * ADD / UPDATE ( Processing )
             */
            final List<Future<List<SPermission>>> combined = new ArrayList<>();
            final UxJooq jooq = Ux.Jooq.on(SPermissionDao.class);
            combined.add(jooq.insertAsync(compared.get(ChangeFlag.ADD)));
            combined.add(jooq.updateAsync(compared.get(ChangeFlag.UPDATE)));
            /*
             * Combined for final on PERM_SET
             */
            final Refer permSetRef = new Refer();
            return Ux.thenCombineArrayT(combined)
                    .compose(processed -> {
                        /*
                         * Calculate to permission codes
                         */
                        final Set<String> permCodes = processed.stream()
                                .map(SPermission::getCode).collect(Collectors.toSet());
                        return Ux.future(permCodes);
                    })
                    .compose(permSetRef::future)
                    .compose(nil -> {
                        /*
                         * Fetch PERM_SET by group
                         */
                        final JsonObject criteria = new JsonObject();
                        criteria.put(KeField.SIGMA, sigma);
                        criteria.put(KeField.NAME, group);
                        return Ux.Jooq.on(SPermSetDao.class).<SPermSet>fetchAsync(criteria);
                    })
                    .compose(permSet -> {
                        return null;
                    });
        });
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
        final Set<String> permCodes = Ut.mapString(permissions, KeField.CODE);
        final JsonObject criteria = new JsonObject();
        criteria.put(KeField.SIGMA, sigma);
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
