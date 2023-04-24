package cn.originx.migration.backup;

import cn.originx.migration.tookit.AbstractStatic;
import cn.vertxup.erp.domain.tables.daos.*;
import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

public class BackupOrg extends AbstractStatic {

    public BackupOrg(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {

        this.banner("003.11. 备份组织架构");
        final String folder = "org";
        return Ux.future(config)
            /* ECompany */
            .compose(this.backupT(ECompanyDao.class, folder)::procAsync)
            /* EDept */
            .compose(this.backupT(EDeptDao.class, folder)::procAsync)
            /* ETeam */
            .compose(this.backupT(ETeamDao.class, folder)::procAsync)
            /* ECustomer */
            .compose(this.backupT(ECustomerDao.class, folder)::procAsync)
            /* EIdentity */
            .compose(this.backupT(EIdentityDao.class, folder)::procAsync)
            /* EEmployee */
            .compose(this.backupT(EEmployeeDao.class, folder)::procAsync)
            .compose(nil -> Ux.future(config));
    }
}
