package cn.originx.migration.backup;

import cn.originx.migration.tookit.AbstractStatic;
import cn.vertxup.rbac.domain.tables.daos.OUserDao;
import cn.vertxup.rbac.domain.tables.daos.RUserRoleDao;
import cn.vertxup.rbac.domain.tables.daos.SRoleDao;
import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

public class BackupUser extends AbstractStatic {

    public BackupUser(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("003.12. 备份账号信息");

        final String folder = "user";
        final JsonObject configNo = config.copy();
        configNo.put("all", Boolean.TRUE);
        return Ux.future(config)
            /* SUser */
            .compose(this.backupT(SUserDao.class, folder)::procAsync)
            /* SRole */
            .compose(this.backupT(SRoleDao.class, folder)::procAsync)
            .compose(proccesed -> Ux.future(configNo))
            /* OUser */
            .compose(this.backupT(OUserDao.class, folder)::procAsync)
            /* RUserRole */
            .compose(this.backupT(RUserRoleDao.class, folder)::procAsync);
    }
}
