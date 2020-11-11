package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.SPermSetDao;
import cn.vertxup.rbac.domain.tables.daos.SPermissionDao;
import cn.vertxup.rbac.domain.tables.pojos.SPermission;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

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
     * 1) Permission combine ( code, sigma as unique )
     * 2) PermSet combine ( name, code as unique )
     */
    @Override
    public Future<JsonArray> saveDefinition(final JsonArray permissions, final String group, final String sigma) {
        /*
         *
         */
        return null;
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
            /*
             * Compared for three types
             * 1) Old List - original
             * 2) New List - current
             */
            final List<SPermission> addQueue = new ArrayList<>();
            final List<SPermission> updateQueue = new ArrayList<>();
            final List<SPermission> removeQueue = new ArrayList<>();
            return null;
        });
    }
}
