package io.mature.extension.migration.restore;

import cn.vertxup.rbac.domain.tables.daos.OUserDao;
import cn.vertxup.rbac.domain.tables.daos.RUserRoleDao;
import cn.vertxup.rbac.domain.tables.daos.SRoleDao;
import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import io.horizon.eon.em.Environment;
import io.mature.extension.migration.tookit.AbstractStatic;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

public class RestoreUser extends AbstractStatic {

    public RestoreUser(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("003.2. 还原账号信息");

        final String folder = "user";
        final JsonObject configNo = config.copy();
        configNo.put("all", Boolean.TRUE);
        return Ux.future(config)
            /* SUser */
            .compose(this.restoreT(SUserDao.class, folder)::procAsync)
            /* SRole */
            .compose(this.restoreT(SRoleDao.class, folder)::procAsync)
            .compose(proccesed -> Ux.future(configNo))
            /* OUser */
            .compose(this.restoreT(OUserDao.class, folder)::procAsync)
            /* RUserRole */
            .compose(this.restoreT(RUserRoleDao.class, folder)::procAsync);
    }
}
